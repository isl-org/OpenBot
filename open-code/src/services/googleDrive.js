import {Constants, errorToast, localStorageKeys} from "../utils/constants";
import {FormatDate, getCurrentProject} from "./workspace";

/**
 * function that upload project data on Google Drive
 * @param data
 * @param fileType
 * @returns {Promise<void>}
 */
export const uploadToGoogleDrive = async (data, fileType) => {
    const accessToken = getAccessToken();
    let folderId = await getFolderId();

    //check folder id in Google Drive if exist then directly upload file or else create folder
    if (!folderId) {
        folderId = await CreateFolder(accessToken);
    }
    let response;

    if (fileType === Constants.xml || fileType === Constants.js || fileType === Constants.json) {
        response = await uploadFileToFolder(accessToken, data, folderId, fileType);
    } else if (fileType === Constants.tflite) {
        response = await uploadTfliteFile(accessToken, data, folderId);
    }

    return response;
};


/**
 * uploading file to folder
 * @param accessToken
 * @param data
 * @param folderId
 * @param fileType
 */
const uploadFileToFolder = async (accessToken, data, folderId, fileType) => {
    let fileMetadata = {
        name: getCurrentProject().projectName + ".js",
        parents: [folderId],
        mimeType: "text/javascript",
        content_type: "application/json; charset=UTF-8",
        appProperties: {
            date: FormatDate().currentDate,
            time: FormatDate().currentTime,
            updatedTime: new Date(),
            storage: "drive",
        },
    }
    let metadataFields = 'appProperties,id,name,createdTime';
    let mediaPart;
    if (fileType === Constants.js) {
        fileMetadata = {
            ...fileMetadata
        }
    } else if (fileType === Constants.xml) {
        fileMetadata = {
            ...fileMetadata,
            name: data.projectName + ".xml",
            mimeType: "text/xml",
        };
    } else if (fileType === Constants.json) {
        fileMetadata = {
            ...fileMetadata,
            name: "config.json",
            mimeType: "application/json",
        };
    }

    const boundary = "foo_bar_baz";
    const metadataPart = `--${boundary}\r\nContent-Type: application/json; charset=UTF-8\r\n\r\n${JSON.stringify(fileMetadata)}\r\n`;
    if (fileType === Constants.xml) {
        mediaPart = `--${boundary}\r\nContent-Type: ${fileMetadata.mimeType}\r\n\r\n${data.xmlValue}\r\n`;
    } else {
        mediaPart = `--${boundary}\r\nContent-Type: ${fileMetadata.mimeType}\r\n\r\n${data}\r\n`;
    }
    const requestBody = `${metadataPart}${mediaPart}--${boundary}--\r\n`;
    const headers = {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": `multipart/related; boundary=${boundary}`,
        "Content-Length": requestBody?.length,
    };

    let fileExistWithFileID;
    // Check if a file with the specified fileId exists
    if (fileType === Constants.xml) {
        // Check if a file with the specified fileId exists
        fileExistWithFileID = await checkFileExistsInFolder(folderId, data.projectName, 'xml')
    } else if (fileType === Constants.js) {
        // Check if a file with the specified fileId exists
        fileExistWithFileID = await checkFileExistsInFolder(folderId, getCurrentProject().projectName, 'js')
    } else if (fileType === Constants.json) {
        // Check if a file with the specified fileId exists
        fileExistWithFileID = await checkFileExistsInFolder(folderId, "config", 'json')
    }
    return await updateExistingFile(fileExistWithFileID, data, folderId, metadataFields, headers, requestBody)
};


/**
 * function to update existing drive file
 * @param fileExistWithFileID
 * @param data
 * @param folderId
 * @param metadataFields
 * @param headers
 * @param requestBody
 * @returns {Promise<*>}
 */
async function updateExistingFile(fileExistWithFileID, data, folderId, metadataFields, headers, requestBody) {
    let res;
    if (fileExistWithFileID.exists) {
        //delete file and then create new file
        await deleteFileFromGoogleDrive(fileExistWithFileID.fileId).then(async () => {
            res = await CreateFile(data, folderId, metadataFields, headers, requestBody);
        });
    } else {
        // If a file with the specified fileId doesn't exist, create a new file
        res = await CreateFile(data, folderId, metadataFields, headers, requestBody);
    }
    return res;
}


/**
 * check file exist or not
 * @param folderId
 * @param fileName
 * @param fileType
 * @returns {Promise<{exists: boolean, fileId}>}
 */
export async function checkFileExistsInFolder(folderId, fileName, fileType) {
    const accessToken = getAccessToken();
    let fileNameWithExtension = fileName;
    if (fileType === Constants.js) {
        fileNameWithExtension += `.${Constants.js}`;
    } else if (fileType === Constants.xml) {
        fileNameWithExtension += `.${Constants.xml}`;
    } else if (fileType === Constants.json) {
        fileNameWithExtension += `.${Constants.json}`;
    }
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
    return searchResult?.files[0]?.id || null;
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
        .then(response => response.json()).catch(() => errorToast("error in upload"))
        .then(async (file) => {
            if (file.error) {
                errorToast(file.error.message);
            } else {
                const isJSFile = file?.name.endsWith('.js');
                file && SharingFileFromGoogleDrive(file?.id, isJSFile);
                if (isJSFile) {
                    return await getShareableLink(file.id, folderId);
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
    if (folderId) {
        const filesResponse = await fetch(`${Constants.baseUrl}/files?q=trashed=false and parents='${folderId}'&fields=files(id,name,createdTime,modifiedTime,appProperties,mimeType)&access_token=${accessToken}`);
        const filesResult = await filesResponse.json();

        // Step 3: get xmlValue and append to each file.
        await Promise.all(filesResult.files?.map(async (file) => {
            if (file.id) {
                file.xmlValue = await getSelectedProjectFromGoogleDrive(folderId, file.id, accessToken);
            }
        }));

        return filesResult.files;
    } else {
        return [];
    }
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
        .catch((err) => {
            errorToast("Something went wrong.")
            console.log(err)
        })
}


/**
 * permissions for sharing Google Drive files
 * @param fileId
 * @param isJSFile
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
 * function to get shareable link of Google Drive file
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
            return data.webViewLink.replace('/view', '/edit?usp=sharing');
        })
        .catch(error => console.error(error));

}

/**
 * function to get download link of drive file
 * @param fileId
 * @param folderId
 * @returns {Promise<any>}
 */
export async function getDownloadedLink(fileId, folderId) {
    const accessToken = getAccessToken();
    const params = {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + accessToken
        }
    };
    return await fetch(`https://www.googleapis.com/drive/v3/files/${fileId}?parents=${folderId}&fields=webContentLink&supportsAllDrives=true`, params)
        .then(response => {
            if (!response.ok) {
                throw new Error('An error occurred.');
            }
            return response.json();
        })
        .then(data => {
            return data.webContentLink
        })
        .catch(error => console.error(error));

}

/**
 * Rename file's name in google drive
 * @param newFileName
 * @param oldName
 * @param fileType
 * @returns {Promise<void>}
 */
export async function fileRename(newFileName, oldName, fileType) {
    const folderId = await getFolderId();
    const accessToken = getAccessToken();
    let fileId = undefined;
    let body;
    if (fileType === Constants.xml) {
        fileId = await checkFileExistsInFolder(folderId, oldName, Constants.xml); //check according to file type
        body = {"name": newFileName + `.${Constants.xml}`} //add name with extension according to fileType
    } else {
        fileId = await checkFileExistsInFolder(folderId, oldName, Constants.js); //check according to file type
        body = {"name": newFileName + `.${Constants.js}`} //add name with extension according to fileType
    }

    await fetch(`https://www.googleapis.com/drive/v3/files/${fileId.fileId}?parents=${folderId}&fields=name`, {
        method: 'PATCH',
        headers: {
            'Authorization': 'Bearer ' + accessToken,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(body)
    })
        .then((res) => res.json())
        .catch(err => console.log(err))
}


/**
 * Google Drive's openBot-playground folder made public
 * @param folderId
 * @param accessToken
 * @returns {Promise<void>}
 */
export const makeFolderPublic = async (folderId, accessToken) => {
    const url = `${Constants.baseUrl}/files/${folderId}/permissions`;
    const options = {
        method: 'POST',
        headers: {
            Authorization: `Bearer ${accessToken}`,
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            type: 'anyone',  //public
            role: 'reader', // read rights
        }),
    };

    await fetch(url, options).catch(() =>
        errorToast("Something went wrong")
    );
};


/**
 * function to upload model file to drive
 * @param accessToken
 * @param data
 * @param folderId
 * @returns {Promise<any>}
 */
const uploadTfliteFile = async (accessToken, data, folderId) => {
    const url = 'https://www.googleapis.com/upload/drive/v3/files?uploadType=resumable';
    const metadata = {
        name: data.name + `.${Constants.tflite}`,
        mimeType: 'application/octet-stream',
        parents: [folderId]
    };

    const metadataStr = JSON.stringify(metadata);
    const headers = {
        'Authorization': `Bearer ${accessToken}`,
        'Content-Type': 'application/json; charset=UTF-8',
    };

    const initiateResponse = await fetch(url, {
        method: 'POST',
        headers: headers,
        body: metadataStr,
    }).catch((err) => {
        console.log(err);
    });

    const locationUrl = initiateResponse.headers.get('Location');
    const fileContentHeaders = {
        'Content-Type': 'application/octet-stream',
        'Content-Length': data.fileData.size,
    };

    return await fetch(locationUrl, {
        method: 'POST',
        headers: fileContentHeaders,
        body: data.fileData,
    }).then(response => response.json()).catch(() => errorToast("error in upload"))
        .then(async (file) => {
                const isTfliteFile = file?.name.endsWith('.tflite');
                file && SharingFileFromGoogleDrive(file?.id, isTfliteFile);
                if (isTfliteFile) {
                    return await getDownloadedLink(file.id, folderId);
                } else {
                    return true;
                }
            }
        )
        .catch((err) => {
            console.log(err);
        });
};