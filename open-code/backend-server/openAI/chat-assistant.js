// routes.js
const express = require('express');
const { OpenAI } = require('openai');
const fs = require('fs').promises;

const router = express.Router();

const openai = new OpenAI({
    apiKey: process.env.OPENAI_API_KEY,
});
let blocks;
async function readBlocksJson() {
    try {
        const data = await fs.readFile('utils/blocks.json', 'utf8');
        return JSON.parse(data);
    } catch (err) {
        console.error('Error reading blocks.json file:', err);
        return null;
    }
}
readBlocksJson().then(data => {
    if (data) {
        blocks = data;
        console.log('Successfully loaded blocks.json.');
    } else {
        console.error('Failed to load blocks.json.');
    }
}).catch(err => {
    console.error('Error loading blocks.json:', err);
});
router.post('/chat', async (req, res) => {
    const { userPrompt } = req.body;
    console.log("userPrompt::", userPrompt)
    if (!userPrompt) {
        return res.status(400).json({ error: 'Missing required data (user prompt).' });
    }
    if (!blocks) {
        return res.status(500).json({ error: 'Failed to load Blockly blocks.' });
    }

    const systemMessage = `Based on the following Blockly block JSON:\n\n${JSON.stringify(blocks)}\n\nWhat does the code do when you ${userPrompt}?`;

    try {
        const response = await openai.chat.completions.create({
            model: 'gpt-3.5-turbo',
            messages: [
                { role: 'system', content: systemMessage },
                { role: 'user', content: userPrompt }
            ],
            max_tokens: 150,
        });

        const aiResponse = response.choices[0].message.content.trim();
        res.json({ aiResponse });
    } catch (error) {
        console.error('Error with OpenAI API request:', error.response ? error.response.data : error.message);
        res.status(500).json({ error: 'An error occurred while processing your request.' });
    }
});

module.exports = router;
