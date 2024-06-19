// routes.js
const express = require('express');
const {OpenAI} = require('openai');
const {Constants} = require("../utils/constants");
const fs = require('fs').promises;
const router = express.Router();

const openai = new OpenAI({
    apiKey: process.env.OPENAI_API_KEY,
});

/**
 * function to read blockly json from static file
 * @returns {Promise<any|null>}
 */
async function readBlocksJson() {
    try {
        const data = await fs.readFile('utils/blocks.json', 'utf8');
        return JSON.parse(data);
    } catch (err) {
        console.error('Error reading blocks.json file:', err);
        return null;
    }
}

/**
 * API to fetch response as per the user prompt
 */
router.post('/chat', async (req, res) => {
    const {userPrompt} = req.body;
    console.log("userPrompt::", userPrompt)
    if (!userPrompt) {
        return res.status(400).json({error: 'Missing required data (user prompt).'});
    }

    // const playgroundInfo = Constants.playgroundInfo

    let blocklyJSON = await readBlocksJson();
   const systemMessage = `Based on the following Blockly block JSON:\n\n${JSON.stringify(blocklyJSON)}\n\nProvide a step-by-step implementation to achieve the following: ${userPrompt}. Do not include the JSON in the response. `;



    try {
        const response = await openai.chat.completions.create({
            model: 'gpt-4o',
            messages: [
                {role: 'system', content: systemMessage},
                {role: 'user', content: userPrompt}
            ],
            max_tokens: 1500,
        });

        const aiResponse = response.choices[0].message.content.trim();
        res.json({aiResponse});
    } catch (error) {
        console.error('Error with OpenAI API request:', error.response ? error.response.data : error.message);
        res.status(500).json({error: 'An error occurred while processing your request.'});
    }
});

module.exports = router;
