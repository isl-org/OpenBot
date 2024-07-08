import Blockly from "blockly/core";

export const extractXmlFromResponse = async (message, workspace) => {
    const regex = /<xml xmlns="https:\/\/developers.google.com\/blockly\/xml">[\s\S]*?<\/xml>/;
    const match = message.match(regex);
    if (match) {
        console.log("match::",match)
        Blockly.Xml.domToWorkspace(Blockly.utils.xml.textToDom(match[0]), workspace);
    } else {
        console.log('No match found');
    }
};
