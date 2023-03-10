import {localStorageKeys} from "../utils/constants";

/**
 * function that upload project data on Google Drive
 * @param data
 * @param uniqueId
 * @returns {Promise<void>}
 */

let folderId=null;
export const uploadToGoogleDrive = (data) => {
    const accessToken = localStorage.getItem(localStorageKeys.accessToken);

    if (!folderId) {
        const folderMetadata = {
            name: "openCode-openBot",
            mimeType: "application/vnd.google-apps.folder"
        };

        fetch("https://www.googleapis.com/drive/v3/files", {
            method: "POST",
            headers: {
                Authorization: `Bearer ${accessToken}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(folderMetadata)
        })
            .then(response => response.json())
            .then(folder => {
                console.log(`Folder '${folder.name}' created with ID '${folder.id}'`);
                folderId = folder.id;

                uploadFileToFolder(accessToken, data, folderId);
            })
            .catch(error => {
                console.error(error);
            });
    } else {
        uploadFileToFolder(accessToken, data, folderId);
    }
};

const uploadFileToFolder = (accessToken, data, folderId) => {
    const fileMetadata = {
        name: data.projectName,
        parents: [folderId],
        mimeType: "text/xml",
        date: data.date,
        content_type: "application/json; charset=UTF-8",
        projectId: data.id,
        time:data.time
    };

    const boundary = "foo_bar_baz";
    const headers = {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": `multipart/related; boundary=${boundary}`,
    };
    const metadataPart = `--${boundary}\r\nContent-Type: application/json; charset=UTF-8\r\n\r\n${JSON.stringify(fileMetadata)}\r\n`;
    const mediaPart = `--${boundary}\r\nContent-Type: ${fileMetadata.mimeType}\r\n\r\n${data.xmlValue}\r\n`;
    const requestBody = `${metadataPart}${mediaPart}--${boundary}--\r\n`;
    headers["Content-Length"] = requestBody.length;

    fetch("https://www.googleapis.com/upload/drive/v3/files/?uploadType=multipart", {
        method: "POST",
        headers: headers,
        body: requestBody
    })
        .then(response => response.json())
        .then(file => {
            console.log(`File '${file.name}' uploaded to folder '${folderId}' with ID '${file.id}'`);
        })
        .catch(error => {
            console.error(error);
        });
};


export function getFromGoogleDrive()
{
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