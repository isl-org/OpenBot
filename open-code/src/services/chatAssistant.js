/**
 *
 * @param userPrompt
 * @param signal
 * @returns {Promise<string | string>}
 */
export const getAIMessage = (userPrompt, signal) => {
    return fetch(`http://localhost:8080/openAI/generate-code-assistance`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ userPrompt: userPrompt }),
        signal: signal
    }).then(res => res.json())
        .then((res) => {
            return res.aiResponse;
        })
        .catch((error) => {
            if (error.name === 'AbortError') {
                console.log('Request cancelled');
            } else {
                console.error(error);
                return "Error occurred while processing your request.";
            }
        });
};