import {finalPrompt} from "../utils/prompt";
/**
 * API to get the assistant response
 * @param {string} userPrompt - The prompt provided by the user
 * @param {AbortSignal} signal - Abort signal to cancel the request
 * @param currentXML
 * @returns {Promise<string>} - The AI response or an error message
 */
export const getAIMessage = async (userPrompt, currentXML, signal) => {

    const url = `https://api.openai.com/v1/chat/completions`; // backend API endpoint
    // const personaItem = persona.filter((item) => item.key === personaIndex);
    try {
        console.log("key::", process.env.REACT_APP_OPENAI_API_KEY);
        const response = await fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${process.env.REACT_APP_OPENAI_API_KEY}`, // Ensure you have the right API key here
            },
            body: JSON.stringify({
                "messages": [
                    {role: 'system', content: finalPrompt + "\nInput XML : " + currentXML},
                    {
                        role: 'user',
                        content: userPrompt
                    }
                ],
                "model": "gpt-4o-mini",
                "stream": false
            }),
            signal
        });

        const result = await response.json();
        return result.choices[0].message.content;
    } catch (error) {
        if (error.name === 'AbortError') {
            console.log('Request cancelled');
            return "Request was cancelled.";
        } else {
            console.error('Error occurred:', error);
            return "Error occurred while processing your request.";
        }
    }
};
