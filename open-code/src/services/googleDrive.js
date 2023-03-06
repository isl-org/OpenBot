const CLIENT_ID = '265415454186-bpdnqdqfn4k4vfa9pfvn2khs31sjgq9n.apps.googleusercontent.com';
const CLIENT_SECRET = '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';
const REFRESH_TOKEN = '1//04QKMVxkXLvJsCgYIARAAGAQSNwF-L9IrLt8_INLScl9Gy-zcJWdySTySfTT0s6NzJNdJqgQn1KlQHPVz8zwUk0YHKpb-GtDVpMU';
const url = "https://www.googleapis.com/upload/drive/v3/files?uploadType=multipart";


/**
 * function that saves blocks data on google drive
 * @param data
 * @param uniqueId
 * @returns {Promise<void>}
 */

export async function uploadToGoogleDrive(data, uniqueId) {

    await fetch('https://oauth2.googleapis.com/token', {
        method: "POST",
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({
            grant_type: "refresh_token",
            client_id: CLIENT_ID,
            client_secret: CLIENT_SECRET,
            refresh_token: REFRESH_TOKEN,
        })
    })
        .then(res => res.json())
        .then(response => {
            const accessToken = response.access_token;
            const fileMetadata = {
                name: data.projectTitle,
                id: uniqueId,
                mimeType: "text/xml",
                date: data.date
            };

            const blockXml = new Blob([data.xmlText], { type: "text/xml" });
            const BlockData = new FormData();
            BlockData.append("metadata", new Blob([JSON.stringify(fileMetadata)], {type: "application/json"}));
            BlockData.append("file", blockXml,data.projectTitle + ".xml");

            fetch(url, {
                method: 'POST',
                headers: {"Authorization": `Bearer ${accessToken}`, 'Content-Type': "application/json"},
                body: BlockData,
            })
                .then(response => response.json())
                .then(data => console.log(data))
                .catch(error => console.error(error));
        })
}
