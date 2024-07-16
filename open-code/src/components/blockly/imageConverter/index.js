import Blockly from "blockly/core";

/**
 * addBlocksToWorkspace function extracts XML data from a given message and adds it to a Blockly workspace.
 * @param message
 * @param workspace
 * @returns {Promise<void>}
 */
export const addBlocksToWorkspace = async (message, workspace) => {
    const regex = /<xml xmlns="https:\/\/developers.google.com\/blockly\/xml">[\s\S]*?<\/xml>/;
    const match = message.match(regex);
    if (match) {
        try {
            // workspace.clear()
            workspace.clear()
            const xmlDom = Blockly.utils.xml.textToDom(match[0]);
            Blockly.Xml.domToWorkspace(xmlDom, workspace);
        } catch (error) {
            console.error("Error parsing XML or adding to workspace:", error);
        }
    } else {
        console.log('No match found');
    }

};
