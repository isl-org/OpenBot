import {finalPrompt} from "../utils/prompt";

/**
 * API to get the assistant response with streaming
 * @param userPrompt
 * @param currentXML
 * @param signal
 * @param onMessage
 * @returns {Promise<string>}
 */
export const getAIMessage = async (userPrompt, currentXML, signal, onMessage) => {
    const url = `https://api.openai.com/v1/chat/completions`;

    try {
        const response = await fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${process.env.REACT_APP_OPENAI_API_KEY}`,
            },

            body: JSON.stringify({
                "messages": [
                    {role: 'system', content: finalPrompt + "\nInput XML : " + currentXML},
                    {role: 'user', content: userPrompt}
                ],
                model: "gpt-4o-mini-2024-07-18",
                stream: true,
                response_format: {
                    type: "json_schema",
                    json_schema: {
                        name: "blockly_chat_assistant",
                        schema: {
                            type: "object",
                            strict: true,
                            properties: {
                                BLOCKLY_RESPONSE: {
                                    type: "string",
                                    description: `Always Provide only assistant explanation for blockly blocks in '"BLOCKLY_RESPONSE"' field. Do not provide XML code at any cost`
                                },
                                BLOCKLY_XML_CODE: {
                                    type: "string",
                                    description: `Always provide XML format code only for blockly code blocks in '"BLOCKLY_XML_CODE"' field`
                                },
                            },
                            required: ["BLOCKLY_RESPONSE", "BLOCKLY_XML_CODE"],
                            additionalProperties: false
                        },
                    }
                }
            }),
            signal
        });

        const reader = response.body.getReader();
        const decoder = new TextDecoder();
        let resultText = '';

        while (true) {
            const {done, value} = await reader.read();
            if (done) break;

            // Decode the stream chunk
            const chunk = decoder.decode(value, {stream: true});
            //  console.log("chunks----->>", chunk)
            // Stop if the request was aborted
            if (signal.aborted) {
                return "Request was cancelled.";

            }
            const parsedChunk = chunk
                .split("\n")
                .filter(line => line.trim()) // Filter out empty lines
                .map(line => line.replace(/^data: /, "")) // Remove "data: " prefix
                .map(line => {
                    try {
                        return JSON.parse(line);
                    } catch (e) {
                        console.error("JSON parse error:", e);
                        return null;
                    }
                })
                .filter(parsed => parsed !== null);
            for (const parsed of parsedChunk) {
                const content = parsed?.choices[0]?.delta?.content;
                if (content) {
                    onMessage(content);  // Update UI with new content
                    resultText += content;
                }
                if (parsed?.choices[0]?.finish_reason === "stop") {
                    onMessage("Done");
                    return resultText;
                }
            }
        }

        return resultText;

    } catch (error) {
        if (error.name === 'AbortError') {
            return "Request was cancelled.";
        } else {
            console.error('Error occurred:', error);
            return "Error occurred while processing your request.";
        }
    }
};
