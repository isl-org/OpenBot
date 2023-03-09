import {localStorageKeys} from "../utils/constants";

export const uploadToGoogleDrive = (data, uniqueId) => {
    const accessToken = localStorage.getItem(localStorageKeys.accessToken)
    const fileMetadata = {
        name: data.projectName,
        id: uniqueId,
        mimeType: "text/xml",
        date: data.date,
    };

    const formData = new FormData();
    formData.append("metadata", new Blob([JSON.stringify(fileMetadata)], {type: "application/json"}));
    formData.append("file", JSON.stringify(data.xmlValue));

    fetch("https://www.googleapis.com/upload/drive/v3/files?uploadType=multipart", {
        method: "POST",
        headers: {
            Authorization: `Bearer ${accessToken}`,
            'Content-Type': "application/json",
        },
        body: formData
    }).then((response) => {
        console.log("res::::::::", response);
    }).catch((error) => {
        console.log(error);
    });
};

