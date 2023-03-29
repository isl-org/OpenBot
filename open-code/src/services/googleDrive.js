import {Constants, errorToast, localStorageKeys} from "../utils/constants";
import {getCurrentProject} from "./workspace";

/**
 * function that upload project data on Google Drive
 * @param data
 * @param uniqueId
 * @returns {Promise<void>}
 */
export const uploadToGoogleDrive = async (data, fileType) => {
    const accessToken = getAccessToken();
    const folderId = await getFolderId();
    let response;
    if (fileType === Constants.xml) {
        //if folder id then check if exist in googleDrive then directly upload file or else create folder and upload else  then directly create folder
        if (folderId) {
            response = await uploadFileToFolder(accessToken, data, folderId, "xml");
        } else {
            await CreateFolder(accessToken).then((folderId) => {
                    response = uploadFileToFolder(accessToken, data, folderId, "xml");
                }
            );
        }
    } else if (fileType === Constants.js) {

        //if folder id then check if exist in googleDrive then directly upload file or else create folder and upload else  then directly create folder
        if (folderId) {
            response = await uploadFileToFolder(accessToken, data, folderId, "js");
        } else {
            await CreateFolder(accessToken).then(async (folderId) => {
                    response = await uploadFileToFolder(accessToken, data, folderId, "js");
                }
            );
        }
    }

    return response;

}
/**
 * uploading file to folder
 * @param accessToken
 * @param data
 * @param folderId
 */
const uploadFileToFolder = async (accessToken, data, folderId, fileType) => {
    let metadataFields;
    let fileMetadata;
    let mediaPart;
    if (fileType === Constants.js) {
        metadataFields = '';
        fileMetadata = {
            name: getCurrentProject().projectName + ".js",
            parents: [folderId],
            mimeType: "text/javascript",
            content_type: "application/json; charset=UTF-8",
        };
    } else if (fileType === Constants.xml) {
        metadataFields = 'appProperties,id,name,createdTime';
        fileMetadata = {
            name: data.projectName + ".xml",
            parents: [folderId],
            mimeType: "text/xml",
            content_type: "application/json; charset=UTF-8",
            appProperties: {
                date: data.createdDate,
                id: data.id,
                storage: "drive",
                time: data.time,
            },
        };
    }
    const boundary = "foo_bar_baz";
    const metadataPart = `--${boundary}\r\nContent-Type: application/json; charset=UTF-8\r\n\r\n${JSON.stringify(fileMetadata)}\r\n`;
    if (fileType === Constants.xml) {
        mediaPart = `--${boundary}\r\nContent-Type: ${fileMetadata.mimeType}\r\n\r\n${data.xmlValue}\r\n`;
    } else if (fileType === Constants.js) {
        mediaPart = `--${boundary}\r\nContent-Type: ${fileMetadata.mimeType}\r\n\r\n${data}\r\n`;
    }
    const requestBody = `${metadataPart}${mediaPart}--${boundary}--\r\n`;
    const headers = {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": `multipart/related; boundary=${boundary}`,
        "Content-Length": requestBody?.length,
    };
    let res;
    if (fileType === Constants.xml) {
        // Check if a file with the specified fileId exists
        let fileExistWithFileID = await checkFileExistsInFolder(folderId, data.projectName, 'xml')
        if (fileExistWithFileID.exists) {
            //delete file and then create new file
            await deleteFileFromGoogleDrive(fileExistWithFileID.fileId).then(async () => {
                res = await CreateFile(data, folderId, metadataFields, headers, requestBody);
            });
        } else {
            // If a file with the specified fileId doesn't exist, create a new file
            res = await CreateFile(data, folderId, metadataFields, headers, requestBody);
        }
    } else if (fileType === Constants.js) {
        // Check if a file with the specified fileId exists
        let fileExistWithFileID = await checkFileExistsInFolder(folderId, getCurrentProject().projectName, 'js')
        if (fileExistWithFileID.exists) {
            //delete file and then create new file
            await deleteFileFromGoogleDrive(fileExistWithFileID.fileId);
            res = await CreateFile(data, folderId, metadataFields, headers, requestBody);
        } else {
            // If a file with the specified fileId doesn't exist, create a new file
            res = await CreateFile(data, folderId, metadataFields, headers, requestBody);
        }
    }

    return res;

};

/**
 * check file exist or not
 * @param folderIdx
 * @param fileName
 * @returns {Promise<{exists: boolean, fileId}>}
 */

export async function checkFileExistsInFolder(folderId, fileName, fileType) {
    let fileNameWithExtension = fileName;
    if (fileType === Constants.js) {
        fileNameWithExtension += `.${Constants.js}`;
    } else if (fileType === Constants.xml) {
        fileNameWithExtension += `.${Constants.xml}`;
    }
    const accessToken = getAccessToken();
    const response = await fetch(`${Constants.baseUrl}/files?q=name='${encodeURIComponent(fileNameWithExtension)}'+and+'${encodeURIComponent(folderId)}'+in+parents+and+trashed=false&access_token=${accessToken}`);
    const result = await response.json();
    if (result && result.files.length > 0) {
        return {exists: true, fileId: result.files[0].id};
    } else {
        return {exists: false};
    }
}


/**
 * Create folder
 * @constructor
 * @param accessToken
 * @param folderId
 */
async function CreateFolder(accessToken) {

    const folderMetadata = {
        name: Constants.FolderName,
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
    return await fetch(`${Constants.baseUrl}/files/`, data)
        .then(response => response.json())
        .then(folder => {
            makeFolderPublic(folder.id, accessToken);
            return folder.id;
        })
        .catch(error => {
            console.error(error);
        });
}


/**
 * get folder id from firebase
 */
export async function getFolderId() {
    // Authenticate the user and obtain an access token for the Google Drive API
    const accessToken = getAccessToken();
    // Step 1: Get the ID of the folder with the specified name

    const searchResponse = await fetch(`${Constants.baseUrl}/files?q=name='${encodeURIComponent(Constants.FolderName)}'+and+mimeType='application/vnd.google-apps.folder'+and+trashed=false&access_token=${accessToken}`);
    const searchResult = await searchResponse.json();
    const folderId = searchResult?.files[0]?.id || null;
    return folderId;
}


/**
 * getting access token from local storage
 * @returns {string}
 */
export function getAccessToken() {
    return localStorage.getItem(localStorageKeys.accessToken)
}


/**
 * create file in google drive openBot folder
 */
export function CreateFile(data, folderId, metadataFields, headers, requestBody) {
    let apiEndpoint = 'https://www.googleapis.com/upload/drive/v3/files/?uploadType=multipart';
    if (metadataFields) {
        apiEndpoint += `&fields=${metadataFields}`;
    }
    return fetch(apiEndpoint, {
        method: "POST",
        headers: headers,
        body: requestBody
    })
        .then(response => response.json()).catch(err => errorToast("error in upload"))
        .then(async (file) => {
            if (file.error) {
                errorToast(file.error.message);
            } else {
                const isJSFile = file?.name.endsWith('.js');
                file && SharingFileFromGoogleDrive(file?.id, isJSFile);
                if (isJSFile) {
                    let link = await getShareableLink(file.id, folderId);
                    return link;
                } else {
                    return true;
                }
            }

        })
        .catch(error => {
            errorToast("error in upload")
            console.error(error);
        });


}


/**
 * get all projects from Google Drive
 */
export async function getAllFilesFromGoogleDrive() {
    // Authenticate the user and obtain an access token for the Google Drive API
    const accessToken = getAccessToken();

    // Step 1: Get the ID of the folder with the specified name
    const folderId = await getFolderId();
    // Step 2: Retrieve all files in the folder with their metadata
    const filesResponse = await fetch(`${Constants.baseUrl}/files?q=mimeType='text/xml' and trashed=false and parents='${folderId}'&fields=files(id,name,createdTime,modifiedTime,appProperties,mimeType)&access_token=${accessToken}`);
    const filesResult = await filesResponse.json();

    // Step 3: get xmlValue and append to each file.
    await Promise.all(filesResult.files.map(async (file) => {
        file.xmlValue = await getSelectedProjectFromGoogleDrive(folderId, file.id, accessToken);
    }));

    return filesResult.files;
}


/**
 * get selected project data on clicking
 */
export async function getSelectedProjectFromGoogleDrive(folderId, fileId, accessToken) {
    const headers = {
        Authorization: `Bearer ${accessToken}`,
    };
    return await fetch(`${Constants.baseUrl}/files/${fileId}?parents=${folderId}&alt=media`, {
        method: "GET",
        headers: headers,
    })
        .then((response) => response.text())
        .then((data) => {
            return data;
        })
        .catch((error) => {
            console.log(error);
        });
}


/**
 * deleting file
 * @param fileId
 */
export async function deleteFileFromGoogleDrive(fileId) {
    const folderId = await getFolderId();
    const accessToken = getAccessToken();
    const headers = {
        Authorization: `Bearer ${accessToken}`,
        'Content-Type': 'application/json'
    }

    fetch(`${Constants.baseUrl}/files/${fileId}?supportsAllDrives=true&parents=${folderId}`, {
        method: "DELETE",
        headers: headers
    })
        .then(() => {
            console.log("File has been deleted successfully")
        })
        .catch((err) => {
            errorToast("Something went wrong.")
            console.log(err)
        })
}


/**
 * permissions for sharing Google Drive files
 * @param fileId
 */
export function SharingFileFromGoogleDrive(fileId, isJSFile) {
    const accessToken = getAccessToken();
    let permission;
    if (isJSFile === true) {
        permission = {
            'type': 'anyone',
            'role': 'reader'
        }

        const params = {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + accessToken,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(permission)
        };

// Share the file
        fetch(`https://www.googleapis.com/drive/v3/files/${fileId}/permissions?sendNotificationEmail=false&supportsAllDrives=true`, params)
            .then(response => {
                if (!response.ok) {
                    throw new Error('An error occurred while sharing the file.');
                }
            })
            .catch(error => console.error(error));
    }
}


/**
 * shareable link of Google Drive file
 * @param fileId
 * @param folderId
 * @returns {Promise<any>}
 */
export async function getShareableLink(fileId, folderId) {
    const accessToken = getAccessToken();
    const params = {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + accessToken
        }
    };
    return await fetch(`https://www.googleapis.com/drive/v3/files/${fileId}?parents=${folderId}&fields=webViewLink&supportsAllDrives=true`, params)
        .then(response => {
            if (!response.ok) {
                throw new Error('An error occurred.');
            }
            return response.json();
        })
        .then(data => {
            const shareableLink = data.webViewLink.replace('/view', '/edit?usp=sharing');
            const downloadLink = data.webContentLink
            console.log("download link::::::;", downloadLink);
            return shareableLink;
        })
        .catch(error => console.error(error));

}

export async function fileRename(newFileName, oldName, fileType) {
    const folderId = await getFolderId();
    const accessToken = getAccessToken();
    let body;
    let fileId;
    if (fileType === Constants.xml) {
        fileId = await checkFileExistsInFolder(folderId, oldName, "xml");
        body = {
            "name": newFileName + `.${Constants.xml}`
        }
    } else {
        fileId = await checkFileExistsInFolder(folderId, oldName, "js");
        body = {
            "name": newFileName + `.${Constants.js}`
        }
    }
    console.log("fileId::::", fileId.fileId)
    await fetch(`https://www.googleapis.com/drive/v3/files/${fileId.fileId}?parents=${folderId}&fields=name`, {
        method: 'PATCH',
        headers: {
            'Authorization': 'Bearer ' + accessToken,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(body)

    })
        .then((res) => res.json())
        .then((data) => console.log(data))
        .catch(err => console.log(err))
}


export const makeFolderPublic = async (folderId, accessToken) => {
    const url = `${Constants.baseUrl}/files/${folderId}/permissions`;
    const options = {
        method: 'POST',
        headers: {
            Authorization: `Bearer ${accessToken}`,
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            type: 'anyone',
            role: 'reader',
        }),
    };

    await fetch(url, options).catch(() =>
        errorToast("Something went wrong ")
    );
};


//
// //upload file in Google Drive openBot folder
// function UpdateFile(response, requestBody, data, metadataFields) {
//
//     const accessToken = localStorage.getItem(localStorageKeys.accessToken);
//     let headers = {
//         Authorization: `Bearer ${accessToken}`,
//         'Content-Type': 'application/xml',
//     }
//
//     return response.json().then(file => {
//         // Make a PATCH request to update the file content
//         fetch(`https://www.googleapis.com/upload/drive/v3/files/${data.fileId}?uploadType=media&fields=${metadataFields}`, {
//             method: 'PATCH',
//             headers: headers,
//             body: requestBody,
//
//         }).then(response => {
//             console.log('response after trying to update:::', response);
//         }).catch(error => {
//             console.log('Error updating file:', error);
//         });
//     });
// }
