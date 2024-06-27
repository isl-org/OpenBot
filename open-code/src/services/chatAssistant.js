/**
 * Sends a user prompt to the AI service and retrieves the generated response.
 *
 * @param {string} userPrompt - The prompt provided by the user for which an AI-generated response is requested.
 * @returns {Promise<string>} - A promise that resolves to the AI-generated response or an error message if the request fails.
 */
export const getAIMessage = (userPrompt) => {
    return fetch(`http://localhost:8080/openAI/generate-code-assistance`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ userPrompt: userPrompt })
    }).then(res => res.json())
        .then((res) => {
            return res.aiResponse;
        })
        .catch((error) => {
            console.error(error);
            return "Error occurred while processing your request.";
        });
};