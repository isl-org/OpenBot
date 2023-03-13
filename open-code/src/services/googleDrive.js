import {localStorageKeys} from "../utils/constants";

/**
 * function that upload project data on Google Drive
 * @param data
 * @param uniqueId
 * @returns {Promise<void>}
 */
export const uploadToGoogleDrive = async (data) => {
    const accessToken = localStorage.getItem(localStorageKeys.accessToken);
    //if folder id then check if exist in googleDrive then directly upload file or else create folder and upload else  then directly create folder
    let folderId = getFolderId();
    console.log("getFolderId:::::", folderId);
    if (folderId) {
        await fetch(`https://www.googleapis.com/drive/v3/files/${folderId}?access_token=${accessToken}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(async (res) => {
            console.log("res::::",res)
                if (!(res.ok)) {
                    await CreateFolder(accessToken, folderId).then((folderId) => {
                            uploadFileToFolder(accessToken, data, folderId)
                        }
                    );
                } else {
                    uploadFileToFolder(accessToken, data, folderId);
                    //TODO update file in folder;
                }
            }
        );
    } else {
        await CreateFolder(accessToken, folderId).then((folderId) => {
                uploadFileToFolder(accessToken, data, folderId)
            }
        );
    }


};


const uploadFileToFolder = (accessToken, data, folderId) => {
    const metadataFields = 'appProperties';
    console.log("dat::::::::", data, folderId);
    const fileMetadata = {
        name: data.projectName,
        parents: [folderId],
        mimeType: "text/xml",
        content_type: "application/json; charset=UTF-8",
        appProperties: {
            date: data.createdDate,
            id: data.id,
        },
    };
    console.log("fileMetaDTA::::", fileMetadata)
    const boundary = "foo_bar_baz";
    const metadataPart = `--${boundary}\r\nContent-Type: application/json; charset=UTF-8\r\n\r\n${JSON.stringify(fileMetadata)}\r\n`;
    const mediaPart = `--${boundary}\r\nContent-Type: ${fileMetadata.mimeType}\r\n\r\n${data.xmlValue}\r\n`;
    const requestBody = `${metadataPart}${mediaPart}--${boundary}--\r\n`;
    const headers = {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": `multipart/related; boundary=${boundary}`,
        "Content-Length": requestBody?.length,
    };
    console.log("headers ,request body", headers, requestBody)
    fetch(`https://www.googleapis.com/upload/drive/v3/files/?uploadType=multipart&fields=${metadataFields}`, {
        method: "POST",
        headers: headers,
        body: requestBody
    })
        .then(response => response.json())
        .then(file => {
            console.log("response data", file)
            console.log(`File '${file.name}' uploaded to folder '${folderId}' with ID '${file.id}'`);
            setGoogleParamsInLocal(folderId);
        })
        .catch(error => {
            console.error(error);
        });
};


export function getFromGoogleDrive() {
    const accessToken = localStorage.getItem(localStorageKeys.accessToken);
    const fileId = "your-file-id-here";
    const headers = {
        Authorization: `Bearer ${accessToken}`,
    };
    fetch(`https://www.googleapis.com/drive/v3/files/${fileId}?alt=media`, {
        method: "GET",
        headers: headers,
    })
        .then((response) => response.text())
        .then((data) => {
            console.log(data);
        })
        .catch((error) => {
            console.log(error);
        });
}


/**
 * Create folder
 * @constructor
 * @param accessToken
 * @param folderId
 */
async function CreateFolder(accessToken, folderId) {
    const folderMetadata = {
        name: "openCode-openBot",
        mimeType: "application/vnd.google-apps.folder"
    };
    //data require to create folder
    const data = {
        method: "POST",
        headers: {
            Authorization: `Bearer ${accessToken}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(folderMetadata)
    }
    //call api to create folder
    return await fetch("https://www.googleapis.com/drive/v3/files", data)
        .then(response => response.json())
        .then(folder => {
            console.log(`Folder '${folder.name}' created with ID '${folder.id}'`);
            folderId = folder.id;
            console.log("  console.log(\"folderId\", folderId)", folderId)
            const data = {
                folderId: folderId,
            }
            //set folderId:::: in local
            setGoogleParamsInLocal(folderId);
            return folderId;
        })
        .catch(error => {
            console.error(error);
        });

}

export const setGoogleParamsInLocal = (data) => {
    console.log("data::::", data)
    localStorage.setItem(localStorageKeys.folderId, data);
}

/**
 * get folder id from firebase
 */
function getFolderId() {
    console.log("Folder id", localStorage.getItem(localStorageKeys.folderId));
    return localStorage.getItem(localStorageKeys.folderId);
}

// export function getFromGoogleDrive()
// {
//     const accessToken = localStorage.getItem(localStorageKeys.accessToken);
//     const headers = {
//         Authorization: `Bearer ${accessToken}`,
//     };
//     fetch(`https://www.googleapis.com/drive/v3/files/${folderID}/${fileID}?alt=media`, {
//         method: "GET",
//         headers: headers,
//     })
//         .then((response) => response.text())
//         .then((data) => {
//             console.log(data);
//         })
//         .catch((error) => {
//             console.log(error);
//         });
// }

