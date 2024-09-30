export const handler = (message) => {
    try {
        const parsedMessage = JSON.parse(message);
        return parsedMessage?.BLOCKLY_RESPONSE ?? undefined;
    } catch (error) {
        console.error('JSON parsing error:', error);
    }
};

export function cleanAndFormatResponse(response) {

    // Replace multiple newlines (two or more) with double newlines to ensure paragraph breaks
    let cleanedResponse = response.replace(/\n{2,}/g, '\n\n');

    // Convert single newlines into spaces to prevent displaying "\n" in text
    cleanedResponse = cleanedResponse.replace(/([^\n])\n([^\n])/g, '$1 $2');

    // Ensure that numbered lists (e.g., "1. ...") are formatted correctly by keeping a newline before them
    cleanedResponse = cleanedResponse.replace(/(\d+)\.\s+/g, '\n$1. ');

    // Properly format bullet points (e.g., "- ") by keeping a newline before them
    cleanedResponse = cleanedResponse.replace(/\s*-\s+/g, '\n- ');

    // Add double newlines after periods for paragraph breaks, unless it's part of a list or bullet point
    cleanedResponse = cleanedResponse.replace(/(?<!\d)\.\s+/g, '.\n\n');

    // Clean up any extra spaces and ensure proper formatting
    cleanedResponse = cleanedResponse.replace(/\s{2,}/g, ' ').trim();

    return cleanedResponse;
}
