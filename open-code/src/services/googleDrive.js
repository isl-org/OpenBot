import {localStorageKeys} from "../utils/constants";

/**
 * function that upload project data on Google Drive
 * @param data
 * @param uniqueId
 * @returns {Promise<void>}
 */
export const uploadToGoogleDrive = (data) => {
    const accessToken = localStorage.getItem(localStorageKeys.accessToken);

    const fileMetadata = {
        name: data.projectName,
        mimeType: "text/xml",
        date: data.date,
        content_type: "application/json; charset=UTF-8"
    };

    const boundary = "foo_bar_baz";
    const headers = {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": `multipart/related; boundary=${boundary}`,
    };
    const metadataPart = `--${boundary}\r\nContent-Type: application/json; charset=UTF-8\r\n\r\n${JSON.stringify(fileMetadata)}\r\n`;
    const mediaPart = `--${boundary}\r\nContent-Type: ${fileMetadata.mimeType}\r\n\r\n${data.xmlValue}\r\n`;
    const requestBody = `${metadataPart}${mediaPart}--${boundary}--\r\n`;
    headers["Content-Length"]=requestBody.length
    fetch("https://www.googleapis.com/upload/drive/v3/files/?uploadType=multipart", {
        method: "POST",
        headers: headers,
        body: requestBody
    }).then((response) => {
        console.log("res::::::::", response);
    }).catch((error) => {
        console.log(error);
    });
};