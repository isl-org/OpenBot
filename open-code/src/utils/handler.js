export const handler = (message) => {
    try {
        const parsedMessage = JSON.parse(message);
        return parsedMessage?.BLOCKLY_RESPONSE ?? null;
    } catch (error) {
        console.error('JSON parsing error:', error);
    }
};
