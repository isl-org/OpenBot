import {Constants, localStorageKeys} from "../utils/constants";

/**
 * function that upload project data on Google Drive
 * @param data
 * @param uniqueId
 * @returns {Promise<void>}
 */
export const uploadToGoogleDrive = async (data) => {
    const accessToken = localStorage.getItem(localStorageKeys.accessToken);
    const folderId = await getFolderId();
    console.log("folderId", folderId)
    //if folder id then check if exist in googleDrive then directly upload file or else create folder and upload else  then directly create folder
    if (folderId) {
        await uploadFileToFolder(accessToken, data, folderId);
    } else {
        await CreateFolder(accessToken).then((folderId) => {
                uploadFileToFolder(accessToken, data, folderId);
            }
        );
    }
}
/**
 * uploading file to folder
 * @param accessToken
 * @param data
 * @param folderId
 */
const uploadFileToFolder = async (accessToken, data, folderId) => {
    const metadataFields = 'appProperties,id,name,createdTime';
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

    // Check if a file with the specified fileId exists
    let fileExistWithFileID = await checkFileExistsInFolder(folderId, data.projectName)
    if (fileExistWithFileID.exists) {
        //delete file and then create new file
        await deleteFileFromGoogleDrive(fileExistWithFileID.fileId);
        CreateFile(data, folderId, metadataFields, headers, requestBody).then();

    } else {
        // If a file with the specified fileId doesn't exist, create a new file
        CreateFile(data, folderId, metadataFields, headers, requestBody).then();
    }
};

/**
 * check file exist or not
 * @param folderId
 * @param fileName
 * @returns {Promise<{exists: boolean, fileId}>}
 */
export async function checkFileExistsInFolder(folderId, fileName) {
    const accessToken = localStorage.getItem(localStorageKeys.accessToken);
    const response = await fetch(`${Constants.baseUrl}/files?q=name='${encodeURIComponent(fileName)}'+and+'${encodeURIComponent(folderId)}'+in+parents+and+trashed=false&access_token=${accessToken}`);
    const result = await response.json();
    console.log("result:::", result)
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
            console.log(`Folder '${folder.name}' created with ID '${folder.id}'`);
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
    const accessToken = localStorage.getItem(localStorageKeys.accessToken);
    // Step 1: Get the ID of the folder with the specified name
    const searchResponse = await fetch(`${Constants.baseUrl}/files?q=name='${encodeURIComponent(Constants.FolderName)}'+and+mimeType='application/vnd.google-apps.folder'+and+trashed=false&access_token=${accessToken}`);
    const searchResult = await searchResponse.json();
    const folderId = searchResult?.files[0]?.id || null;
    console.log(" get folderId:::", folderId)
    return folderId;
}


//create file in google drive openBot folder
async function CreateFile(data, folderId, metadataFields, headers, requestBody) {
    await fetch(`https://www.googleapis.com/upload/drive/v3/files/?uploadType=multipart&fields=${metadataFields}`, {
        method: "POST",
        headers: headers,
        body: requestBody
    })
        .then(response => response.json())
        .then(file => {
            console.log("response data", file)
            console.log(`File '${file.name}' uploaded to folder '${folderId}' with ID '${file.id}'`);
        })
        .catch(error => {
            console.error(error);
        });
}


/**
 * get all projects from Google Drive
 */
export async function getAllFilesFromGoogleDrive() {
    // Authenticate the user and obtain an access token for the Google Drive API
    const accessToken = localStorage.getItem(localStorageKeys.accessToken);

    // Step 1: Get the ID of the folder with the specified name
    const folderId = await getFolderId();
    // Step 2: Retrieve all files in the folder with their metadata
    const filesResponse = await fetch(`${Constants.baseUrl}/files?q=mimeType != 'application/vnd.google-apps.folder' and trashed = false and parents in '${folderId}'&fields=files(id,name,createdTime,modifiedTime,appProperties,mimeType)&access_token=${accessToken}`);
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
    const accessToken = localStorage.getItem(localStorageKeys.accessToken);
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
            console.log(err)
        })
}


//
// //upload file in google drive openBot folder
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

