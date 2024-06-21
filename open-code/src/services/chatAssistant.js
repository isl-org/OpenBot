export const getAIMessage = (userPrompt) => {
    return fetch(`http://localhost:8080/openAI/generate-code-assistance`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ userPrompt: userPrompt })
    }).then(res => res.json())
        .then((res) => {
            return res.aiResponse; // Assuming the API response is a string
        })
        .catch((error) => {
            console.error(error);
            return "Error occurred while processing your request.";
        });
};