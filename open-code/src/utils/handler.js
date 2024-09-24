export const handler = (message) => {
    const parsedMessage = JSON.parse(message);
    const blocklyResponse = parsedMessage.BLOCKLY_RESPONSE;
    return blocklyResponse;
}
