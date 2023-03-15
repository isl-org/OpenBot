import {localStorageKeys} from "../utils/constants";
import {updateLocalProjects} from "./workspace";

/**
 * function that upload project data on Google Drive
 * @param data
 * @param uniqueId
 * @returns {Promise<void>}
 */
export const uploadToGoogleDrive = async (data, setFileId) => {
    const accessToken = localStorage.getItem(localStorageKeys.accessToken);
    //if folder id then check if exist in googleDrive then directly upload file or else create folder and upload else  then directly create folder
    const folderId = getFolderId();

    if (folderId) {

        await fetch(`https://www.googleapis.com/drive/v3/files/${folderId}?access_token=${accessToken}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(async (res) => {
                console.log("res::")

                if (!(res.ok)) {
                    await CreateFolder(accessToken, folderId).then((folderId) => {
                            uploadFileToFolder(accessToken, data, folderId, setFileId)
                        }
                    );
                } else {
                    uploadFileToFolder(accessToken, data, folderId, setFileId);

                }
            }
        );
    } else {
        await CreateFolder(accessToken, folderId).then((folderId) => {
                uploadFileToFolder(accessToken, data, folderId, setFileId)
            }
        );
    }


};
/**
 * uploading file to folder
 * @param accessToken
 * @param data
 * @param folderId
 */
const uploadFileToFolder = (accessToken, data, folderId, setFileId) => {
    const metadataFields = 'appProperties,id,name,createdTime';
    console.log("fileId::::", data)
    const fileMetadata = {
        name: data.projectName,
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

    const boundary = "foo_bar_baz";
    const metadataPart = `--${boundary}\r\nContent-Type: application/json; charset=UTF-8\r\n\r\n${JSON.stringify(fileMetadata)}\r\n`;
    const mediaPart = `--${boundary}\r\nContent-Type: ${fileMetadata.mimeType}\r\n\r\n${data.xmlValue}\r\n`;
    const requestBody = `${metadataPart}${mediaPart}--${boundary}--\r\n`;
    const headers = {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": `multipart/related; boundary=${boundary}`,
        "Content-Length": requestBody?.length,
    };
    console.log("data.fileID", data.fileId)
    if (data.fileId) {
        // Check if a file with the specified fileId exists
        fetch(`https://www.googleapis.com/drive/v3/files/${data.fileId}`, {
            method: 'GET',
            headers: {
                Authorization: `Bearer ${accessToken}`,
                'Content-Type': 'application/json',
                'Content-Encoding': 'identity'
            }
        }).then(response => {
            console.log("response::::", response)
            if (response.status === 200) {
                // If a file with the specified fileId exists, update the existing file
                // You can use the Files.update method to update the existing file
                console.log('File exists');
                UpdateFile(response, headers, requestBody, data,metadataFields,);
            } else if (response.status === 404) {
                // If a file with the specified fileId doesn't exist, create a new file
                // You can use the Files.create method to create a new file
                CreateFile(data, folderId, metadataFields, headers, requestBody, setFileId).then();

            } else {
                // Handle other error cases
                console.log('Error checking file existence');
            }
        }).catch(error => {
            // Handle any network or API errors
            console.log('Error checking file existence:', error);
        });
    } else {
        console.log("createFIle")
        CreateFile(data, folderId, metadataFields, headers, requestBody, setFileId).then();
    }


};

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
            setGoogleFolderId(folder.id);
            return folderId;
        })
        .catch(error => {
            console.error(error);
        });
}


/**
 * set params in local
 * @param data
 */
export const setGoogleFolderId = (data) => {
    localStorage.setItem(localStorageKeys.folderId, data);
}

/**
 * set google files id in array
 * @param fileID
 */
export const SetGoogleFileId = (fileID, setFileId) => {

    let currentProject = JSON.parse(localStorage.getItem(localStorageKeys.currentProject));
    currentProject.fileId = fileID;
    console.log("currentProject::::", currentProject,)
    localStorage.setItem(localStorageKeys.currentProject, JSON.stringify(currentProject));
    setFileId(fileID);
    updateLocalProjects()
}

/**
 * get folder id from firebase
 */
export function getFolderId() {
    return localStorage.getItem(localStorageKeys.folderId);
}


//create file in google drive openBot folder
async function CreateFile(data, folderId, metadataFields, headers, requestBody, setFileId) {
    await fetch(`https://www.googleapis.com/upload/drive/v3/files/?uploadType=multipart&fields=${metadataFields}`, {
        method: "POST",
        headers: headers,
        body: requestBody
    })
        .then(response => response.json())
        .then(file => {
            console.log("response data", file)
            console.log(`File '${file.name}' uploaded to folder '${folderId}' with ID '${file.id}'`);
            console.log("data.fileId", data.fileId)
            SetGoogleFileId(file.id, setFileId)
        })
        .catch(error => {
            console.error(error);
        });
}


//upload file in google drive openBot folder
function UpdateFile(response, headers, requestBody, data,metadataFields) {
    console.log("data.fileID::::::",data.fileId,headers)
    return response.json().then(file => {
        console.log("file:::", requestBody)
        // Make a PATCH request to update the file content
        fetch(`https://www.googleapis.com/upload/drive/v3/files/${data.fileId}` , {
            method: 'PATCH',
            headers: headers,
            body: requestBody,

        }).then(response => {
            console.log('File updated successfully',response);
        }).catch(error => {
            console.log('Error updating file:', error);
        });
    });
}

/**
 * get all projects from Google Drive
 */
export async function getAllFilesFromGoogleDrive() {
    const folderId = getFolderId();
    const accessToken = localStorage.getItem(localStorageKeys.accessToken);
    if (folderId) {
        return await fetch(`https://www.googleapis.com/drive/v3/files?q='${folderId}' in parents&fields=*`, {
            headers: {
                Authorization: `Bearer ${accessToken}`,
            },
        })
            .then((response) => response.json())
            .then((data) => {
                return data.files;

            })
            .catch((error) => {
                console.error('Error:', error);
            });
    }
}

/**
 * get selected project data on clicking
 */
export async function getSelectedProjectFromGoogleDrive(fileId) {
    const folderId = getFolderId()
    const accessToken = localStorage.getItem(localStorageKeys.accessToken);
    const headers = {
        Authorization: `Bearer ${accessToken}`,
    };
    return await fetch(`https://www.googleapis.com/drive/v3/files/${fileId}?parents=${folderId}&alt=media`, {
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
export function deleteFileFromGoogleDrive(fileId) {
    const folderId = getFolderId()
    const accessToken = localStorage.getItem(localStorageKeys.accessToken);
    const headers = {
        Authorization: `Bearer ${accessToken}`,
        'Content-Type': 'application/json'
    }
    fetch(`https://www.googleapis.com/drive/v3/files/${fileId}?supportsAllDrives=true&parents=${folderId}`, {
        method: "DELETE",
        headers: headers
    })
        .then(() => {
            console.log("File has been deleted successfully")
        })
        .catch((err) => {
            console.log(err)
        })
}

/**
 * checking the validity of the accessToken
 */
export function checkAccessTokenValidity(){
    const accessToken = localStorage.getItem(localStorageKeys.accessToken);
    const decodedToken = JSON.parse(atob(accessToken.split('.')[1])); // Decode the token and parse the JSON string
    const expirationTime = new Date(decodedToken.exp * 1000); // Convert the UNIX timestamp to a Date object
    if (expirationTime < new Date()) {
        console.log('Access token has expired');
    } else {
        console.log('Access token is still valid');
    }
}

