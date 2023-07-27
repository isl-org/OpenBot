

const express = require('express');
const http = require('http');
const socketIO = require('socket.io');
const cors = require('cors');
const app = express();
const server = http.createServer(app);
const io = require('./connection'); // Import the bot_connection module

const port = 8000;
app.use(cors());

io.start(server); // Start the connection with the Socket.IO server

server.listen(port, () => {
    console.log(`Socket.IO server listening on port ${port}`);
});

// You can now use the io object for other operations in this file if needed.
// For example, you can handle other routes or events using io object.
