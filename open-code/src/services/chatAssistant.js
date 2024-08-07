import {getCurrentProject} from "./workspace";

/**
 * Api to get the assistant response
 * @param userPrompt
 * @param signal
 * @returns {Promise<string | string>}
 */
export const getAIMessage = (userPrompt, signal) => {
    console.log("process.env.REACT_APP_SERVER_DOMAIN::",process.env.REACT_APP_SERVER_DOMAIN)
    return fetch(`${process.env.REACT_APP_SERVER_DOMAIN}/openAI/generate-code-assistance`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({userPrompt: userPrompt, currentXML: getCurrentProject().xmlValue}),
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