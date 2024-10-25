import {blocklyFinalPrompt,personaFinalPrompt} from "../utils/prompt";

/**
 * API to get the assistant response with streaming
 * @param userPrompt
 * @param persona
 * @param currentXML
 * @param signal
 * @param onMessage
 * @returns {Promise<string>}
 */
export const getAIMessage = async (userPrompt, persona, currentXML, signal, onMessage) => {
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
                    {
                        role: 'system',
                        content: persona ? personaFinalPrompt(persona) : blocklyFinalPrompt + "\nInput XML : " + currentXML
                    },
                    {role: 'user', content: userPrompt}
                ],
                model: "gpt-4o-mini-2024-07-18",
                stream: true,
                response_format: {
                    type: "json_schema",
                    json_schema: {
                        name: "blockly_chat_assistant",
                        description: `Response structure should follow the given json schema structure:
                        $$CONTENT$$ key  does not have any xml but  $$RESPONSE$$ key have only xml code in it.
                       `,
                        schema: {
                            type: "object",
                            strict: true,
                            properties: {
                                $$CONTENT$$: {
                                    type: "string",
                                    description: `Make sure you Provide only an explanation in clear, simple text. This section should describe the purpose and usage of the Blockly blocks. Do not include any XML code or technical details in this part—just the explanation`
                                },
                                $$RESPONSE$$: {
                                    type: "string",
                                    description: `Ensure you Provide only valid XML code for the Blockly blocks. Do not include any explanations in this section—only XML code.`
                                },
                            },
                            required: ["$$CONTENT$$", "$$RESPONSE$$"],
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
