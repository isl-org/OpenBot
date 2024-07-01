import Blockly from "blockly/core";

/**
 * Convert an SVG of a block to a PNG data URI.
 * @param {Blockly.BlockSvg} block The block.
 * @returns {Promise<string>} A promise that resolves with the data URI.
 */
export async function blockToPngBase64(block) {
    return new Promise(async (resolve, reject) => {
        try {
            // Calculate block dimensions and create an SVG element.
            const bBox = block.getBoundingRectangle();
            const width = bBox.right - bBox.left;
            const height = bBox.bottom - bBox.top;

            const blockCanvas = block.getSvgRoot();

            let clone = blockCanvas.cloneNode(true);
            clone.removeAttribute("transform");
            Blockly.utils.dom.removeClass(clone, "blocklySelected");

            // Create wrapper for the cloned SVG
            let wrapper = document.createElementNS("http://www.w3.org/2000/svg", "g");
            wrapper.setAttribute("class", "zelos-renderer classic-theme");
            wrapper.appendChild(clone);

            let svg = document.createElementNS("http://www.w3.org/2000/svg", "svg");
            svg.setAttribute("xmlns", "http://www.w3.org/2000/svg");
            svg.appendChild(wrapper);
            svg.setAttribute("viewBox", `0 0 ${width} ${height}`);
            svg.setAttribute("width", width);
            svg.setAttribute("height", height);

            // Include styles from specific style tag and Blockly's styles
            console.log("document::",document.querySelectorAll('*[id]'))
            let zelosClassicStyle = document.getElementById("blockly-renderer-style-zelos-light");
            console.log("zelosClassicStyle:::",zelosClassicStyle);
            let blocklySvgStyle = document.querySelector("style.blocklySvg");
            console.log("blocklySvgStyle:::",blocklySvgStyle);

            let style = document.createElement("style");
            let styleText = '';
            if (zelosClassicStyle) {
                styleText += zelosClassicStyle.textContent;
            }
            if (blocklySvgStyle) {
                styleText += blocklySvgStyle.textContent;
            }
            style.textContent = styleText;
            console.log("style:::",style)
            svg.insertBefore(style, svg.firstChild);

            // Serialize SVG and convert to PNG.
            let svgAsXML = new XMLSerializer().serializeToString(svg);
            svgAsXML = svgAsXML.replace(/&nbsp/g, "&#160");
            const svgBlob = new Blob([svgAsXML], { type: 'image/svg+xml;charset=utf-8' });
            const svgUrl = URL.createObjectURL(svgBlob);
            console.log(svg);

            let img = new Image();
            img.onload = () => {
                let canvas = document.createElement("canvas");
                canvas.width = width;
                canvas.height = height;
                let context = canvas.getContext("2d");
                if (context) {
                    context.drawImage(img, 0, 0, width, height);
                    const dataUri = canvas.toDataURL("image/png");
                    resolve(dataUri);
                } else {
                    reject(new Error("Failed to get canvas context"));
                }
                // Release object URL after image has been loaded
                URL.revokeObjectURL(svgUrl);
            };
            img.onerror = (error) => {
                reject(error);
            };
            img.src = svgUrl;
        } catch (error) {
            reject(error);
        }
    });
}

export const extractXmlFromResponse = async (message, workspace) => {
    const regex = /<xml xmlns="https:\/\/developers.google.com\/blockly\/xml">[\s\S]*?<\/xml>/;
    const match = message.match(regex);
    if (match) {
        Blockly.Xml.domToWorkspace(Blockly.utils.xml.textToDom(match[0]), workspace);

        const block = workspace.getTopBlocks()[0];
        return await blockToPngBase64(block);
    } else {
        console.log('No match found');
    }
};
