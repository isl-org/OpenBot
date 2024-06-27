const express = require('express');
const {OpenAI} = require('openai');
const {finalPrompt} = require("../utils/prompt");
const router = express.Router();

/**
 * API to fetch response as per the user prompt
 */
router.post('/generate-code-assistance', async (req, res) => {

    const {userPrompt} = req.body;
    console.log("userPrompt::", userPrompt);

    if (!userPrompt) {
        return res.status(400).json({error: 'Missing required data userPrompt.'});
    }
    const openai = new OpenAI({
        apiKey: process.env.OPENAI_API_KEY,
    });

    try {
        const response = await openai.chat.completions.create({
            model: 'gpt-4o',
            messages: [
                {role: 'system', content: finalPrompt},
                {role: 'user', content: userPrompt}
            ],
            max_tokens: 1500,
        });

        const aiResponse = response.choices[0].message.content.trim();
        console.log(aiResponse)
        res.json({aiResponse});
    } catch (error) {
        console.error('Error with OpenAI API request:', error.response ? error.response.data : error.message);
        res.status(500).json({error: 'An error occurred while processing your request.'});
    }
});

module.exports = router;
