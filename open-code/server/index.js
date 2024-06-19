// server.js
const express = require('express');
const bodyParser = require('body-parser');
require('dotenv').config();
const openAI = require('./openAI/chat-assistant'); // Import the router

const app = express();

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));
// Use the API routes
app.use('/openAI', openAI);

// Start the server
app.listen(process.env.PORT || 8080, () => {
    console.log(`Server is running on port 8080`);
});
