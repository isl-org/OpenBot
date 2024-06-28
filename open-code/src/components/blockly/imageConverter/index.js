import Blockly from 'blockly/core';

export const extractXmlFromResponse = async (message, workspace) => {
    console.log("message::", message)
    const regex = /<xml xmlns="https:\/\/developers.google.com\/blockly\/xml">[\s\S]*?<\/xml>/;
    const match = message.match(regex);
    if (match) {
        console.log("workspace:::", workspace)
        Blockly.Xml.domToWorkspace(
            Blockly.utils.xml.textToDom(match[0]),
            workspace
        );

        // primaryWorkspace.current.clear();
        const block = workspace.getTopBlocks()[0];

        console.log("block::", block);

        return await blockToPngBase64(block);

    } else {
        console.log('No match found');
    }
};

export async function blockToPngBase64(block) {
    try {
        svgToPng(block, (imgData) => {
            const pngImage = document.createElement('img');
            document.body.appendChild(pngImage);
            pngImage.src = imgData;
        });

        function svgToPng(svg, callback) {
            const url = getSvgUrl(svg);
            svgUrlToPng(url, (imgData) => {
                callback(imgData);
                URL.revokeObjectURL(url);
            });
        }

        function getSvgUrl(svg) {
            return URL.createObjectURL(new Blob([svg], {type: 'image/svg+xml'}));
        }

        function svgUrlToPng(svgUrl, callback) {
            const svgImage = document.createElement('img');
            // imgPreview.style.position = 'absolute';
            // imgPreview.style.top = '-9999px';
            document.body.appendChild(svgImage);
            svgImage.onload = function () {
                const canvas = document.createElement('canvas');
                canvas.width = svgImage.clientWidth;
                canvas.height = svgImage.clientHeight;
                const canvasCtx = canvas.getContext('2d');
                canvasCtx.drawImage(svgImage, 0, 0);
                const imgData = canvas.toDataURL('image/png');
                callback(imgData);
                // document.body.removeChild(imgPreview);
            };
            svgImage.src = svgUrl
            console.log("svgImage::",svgImage)

            return svgImage.src

        }

    } catch (error) {
        console.error("Error in blockToPngBase64:", error);
        throw error;
    }
}
