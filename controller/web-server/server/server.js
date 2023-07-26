const express = require('express');
const http = require('http');
const socketIO = require('socket.io');
const cors = require('cors'); // Add this line
const app = express();
const server = http.createServer(app);
const io = socketIO(server,{
    cors: {
        origin: '*',
    }
})

const port = 8000;
app.use(cors()); // Enable CORS for all routes, you can customize the options if needed.
io.on('connection', (socket) => {
    console.log('A user connected');
    // Handle custom events
    socket.on('chatMessage', (message) => {
        console.log('Received message:', message);
        io.emit('chatMessage', message); // Broadcast the message to all connected clients
    });

    socket.on('disconnect', () => {
        console.log('A user disconnected');
    });
    socket.on("msg",(data)=>{
        console.log("msg");
        io.emit("msg",data);
    })
    socket.on("signalingMessage",(data)=>{
        console.log("signalingMessage");
        io.emit("signalingMessage",data);
    })
});

server.listen(port, () => {
    console.log(`Socket.IO server listening on port ${port}`);
});
