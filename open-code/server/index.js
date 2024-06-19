// server.js
const express = require('express');
const bodyParser = require('body-parser');
require('dotenv').config();
const apiRoutes = require('./openAI/chat-assistant'); // Import the router

const app = express();

app.use(bodyParser.json());

// Use the API routes
app.use('/api', apiRoutes);

// Start the server
app.listen(process.env.PORT || 8080, () => {
    console.log(`Server is running on http://localhost:${port}`);
});
