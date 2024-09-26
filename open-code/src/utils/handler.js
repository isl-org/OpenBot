export const handler = (message) => {
    try {
        const parsedMessage = JSON.parse(message);
        return parsedMessage?.BLOCKLY_RESPONSE ?? undefined;
    } catch (error) {
        console.error('JSON parsing error:', error);
    }
};

export function cleanAndFormatResponse(response) {
    // Replace multiple newlines with a single space
    let cleanedResponse = response.replace(/\n{2,}/g, ' ');

    // Replace single newlines that are not followed by a numbered list or bullet point with a space
    cleanedResponse = cleanedResponse.replace(/(?<!\d)\n(?!\d|\s*-\s*)/g, ' ');

    // Ensure that numbered lists are formatted correctly
    cleanedResponse = cleanedResponse.replace(/(\d+)\.\s+/g, '');

    // Properly format bullet points
    cleanedResponse = cleanedResponse.replace(/(\s*-\s+)/g, '');

    // Add double newlines after sentences for paragraph breaks
    cleanedResponse = cleanedResponse.replace(/(?<!\d)\.\s+/g, '');

    // Remove extra spaces
    cleanedResponse = cleanedResponse.replace(/\s{2,}/g, ' ').trim();

    return cleanedResponse.replace(/\n\n/g, ' ');
}