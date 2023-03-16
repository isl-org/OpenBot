import {Constants, localStorageKeys} from "../utils/constants";
import {getCurrentProject, updateLocalProjects} from "./workspace";
import {getDocs} from "@firebase/firestore";
import {collection} from "firebase/firestore";
import {auth, db} from "./firebase";
import axios from "axios";

/**
 * function that upload project data on Google Drive
 * @param data
 * @param uniqueId
 * @returns {Promise<void>}
 */
export const uploadToGoogleDrive = async (data, setFileId, setFolderId) => {
    const accessToken = localStorage.getItem(localStorageKeys.accessToken);
    //if folder id then check if exist in googleDrive then directly upload file or else create folder and upload else  then directly create folder
    const folderId = getFolderId();
    if (folderId) {
        await fetch(`${Constants.baseUrl}/files/${folderId}?access_token=${accessToken}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(async (res) => {
                console.log("res check folder exists or not ::", res)
                if (res.status === 200) {
                    // const data = await res.json();
                    // console.log("data if folder exist::::",res?.trashed)
                    uploadFileToFolder(accessToken, data, folderId, setFileId);

                } else {
                    await CreateFolder(accessToken, folderId, setFolderId).then((folderId) => {
                            console.log("folderId,", folderId)
                            uploadFileToFolder(accessToken, data, folderId, setFileId)
                        }
                    );
                }
            }
        );
    } else {
        console.log("when folder is not exist")
        await CreateFolder(accessToken, folderId, setFolderId).then((folderId) => {
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

    if (data.fileId) {
        // Check if a file with the specified fileId exists
        //TODO folderID me jake check karo
        fetch(`${Constants.baseUrl}/files/${data.fileId}`, {
            method: 'GET',
            headers: {
                Authorization: `Bearer ${accessToken}`,
                'Content-Type': 'application/json',
            }
        }).then(response => {
            console.log("response if file exist or not ::::", response)
            if (response.status === 200) {
                console.log('File exists');
                console.log("data.fileID file exist", data.fileId)
                // If a file with the specified fileId exists, update the existing file
                UpdateFile(response, requestBody, data, metadataFields,);
            } else if (response.status === 404) {
                console.log('File  does not exists');
                // If a file with the specified fileId doesn't exist, create a new file
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
async function CreateFolder(accessToken, folderId, setFolderId) {

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
    return await fetch(`${Constants.baseUrl}/files/`, data)
        .then(response => response.json())
        .then(folder => {
            console.log("folder:::::::", folder)
            console.log(`Folder '${folder.name}' created with ID '${folder.id}'`);
            SetGoogleFileId(folder.id, setFolderId, "folderId");
            return folder.id;
        })
        .catch(error => {
            console.error(error);
        });
}


/**
 * function to set folder or file id in current project
 * @param fileID
 */
export const SetGoogleFileId = (fileID, setFileId, key) => {
    let currentProject = JSON.parse(localStorage.getItem(localStorageKeys.currentProject));
    currentProject[key] = fileID;
    localStorage.setItem(localStorageKeys.currentProject, JSON.stringify(currentProject));
    setFileId(fileID);
    updateLocalProjects();
}

/**
 * get folder id from firebase
 */
export function getFolderId() {
    return getCurrentProject()?.folderId ?? getFromFirBase();
}

async function getFromFirBase() {

    // getting all docs from firebase
    const projects = await getDocs(collection(db, auth.currentUser?.uid));
    return projects[0]?.folderId;
}


//create file in google drive openBot folder
async function CreateFile(data, folderId, metadataFields, headers, requestBody, setFileId) {
    console.log("CreateFile variables::::::::", data, folderId, metadataFields, headers, requestBody,)
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
            SetGoogleFileId(file.id, setFileId, "fileId")
        })
        .catch(error => {
            console.error(error);
        });
}


//upload file in google drive openBot folder
function UpdateFile(response, requestBody, data, metadataFields) {

    const accessToken = localStorage.getItem(localStorageKeys.accessToken);
    let headers = {
        Authorization: `Bearer ${accessToken}`,
        'Content-Type': 'application/xml',
    }

    return response.json().then(file => {
        // Make a PATCH request to update the file content
        fetch(`https://www.googleapis.com/upload/drive/v3/files/${data.fileId}?uploadType=media&fields=${metadataFields}`, {
            method: 'PATCH',
            headers: headers,
            body: requestBody,

        }).then(response => {
            console.log('response after trying to update:::', response);
        }).catch(error => {
            console.log('Error updating file:', error);
        });
    });
}


/**
 * get all projects from Google Drive
 */
export async function getAllFilesFromGoogleDrive() {
    // Authenticate the user and obtain an access token for the Google Drive API
    const accessToken = localStorage.getItem(localStorageKeys.accessToken);

    // Step 1: Get the ID of the folder with the specified name
    const searchResponse = await fetch(`${Constants.baseUrl}/files?q=name='${encodeURIComponent(Constants.FolderName)}'+and+mimeType='application/vnd.google-apps.folder'+and+trashed=false&access_token=${accessToken}`);
    const searchResult = await searchResponse.json();
    const folderId = searchResult?.files[0]?.id;

    // Step 2: Retrieve all files in the folder with their metadata
    const filesResponse = await fetch(`${Constants.baseUrl}/files?q=mimeType != 'application/vnd.google-apps.folder' and trashed = false and parents in '${folderId}'&fields=files(id,name,createdTime,modifiedTime,appProperties,mimeType)&access_token=${accessToken}`);
    const filesResult = await filesResponse.json();

    // Step 3: get xmlValue and append to each file.
    await filesResult.files.map(async (file) => {
        let xmlData = await getSelectedProjectFromGoogleDrive(folderId, file.id, accessToken);
        file.xmlValue = xmlData;
    })
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
export function deleteFileFromGoogleDrive(fileId) {
    const folderId = getFolderId()
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


