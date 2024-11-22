const WebSocket = require('ws');

const wss = new WebSocket.Server({ port: 8080 }, () => {
    console.log("Signaling server is now listening on port 8080");
});
let rooms = new Map();
wss.on('connection', (ws) => {
    console.log(`Client connected. Total connected clients: ${wss.clients.size}`);
    askIdOfClient(ws);
    ws.on("message", function message(data, isBinary) {

        const message = isBinary ? data : data.toString();
        // console.log(JSON.parse(message));
        let msg = JSON.parse(message);

        if (Object.keys(msg)[0] === 'roomId') {
            createOrJoinRoom(msg.roomId, ws);
            ws.id = msg.roomId;
            return;
        }
        else if (Object.keys(msg)[0] === 'driveCmd') {
            let driveCmd = msg.driveCmd;
            console.log(driveCmd);
        }
        else if ('command') {
            // You may add additional checks for different message types.
        }

        if (msg.roomId === undefined) {
            sendToBot(ws, message);
            return;
        }

        // Broadcast the message to clients within the same room based on the roomId.
        sendToRoom(msg.roomId, message);
    });


    const sendToRoom = (roomId, message) => {
        console.log("roomId: ", roomId);
        let room = rooms.get(roomId);

        if (room) {
            // Broadcast the message to all non-null clients in the room.
            broadcastToRoom(room, message);
        } else {
            console.log("Room not found for roomId:", roomId);
        }
    }


    ws.onclose = (socket) => {
        console.log(`Client disconnected. Total connected clients: ${wss.clients.size}`);
        let room = rooms.get(ws.id);
        if (room === undefined){
            return
        }
        room[0]?.close()
        rooms[1]?.close();
        rooms.delete(ws.id);
        console.log(rooms)
    };

});

// Function to ask for client's roomId
const askIdOfClient = (ws) => {
    let request = {
        roomId: "request-roomId"
    };
    ws.send(JSON.stringify(request));
};

const createOrJoinRoom = (roomId, ws) => {
    // Check if the room with the given roomId exists
    if (!rooms.has(roomId) || rooms.get(roomId).clients[1] !== null) {
        // Room does not exist or is full (has two clients already)
        let room = {
            clients: [ws, null]
        };
        rooms.set(roomId, room);
    } else {
        // Room exists and has space for the new client
        console.log("joining to the room",rooms);
        let room = rooms.get(roomId);
        room.clients[1] = ws;
        rooms.set(roomId, room);
    }
};

// Broadcast to all.
wss.broadcast = (ws, data) => {
    let obj = JSON.parse(data);
    let key = Object.keys(obj)[0];
    wss.clients.forEach((client) => {
        if (client !== ws && client.readyState === WebSocket.OPEN) {
            client.send(data);
        }
    });
};

// Broadcast to all clients in a specific room.
const broadcastToRoom = (room, message) => {
    room.clients.forEach((client) => {
        if (client && client.readyState === WebSocket.OPEN) {
            client.send(message);

        }
    });
};

const sendToBot = (ws, message) => {
    wss.clients.forEach((client) => {
        if (client !== ws && client.readyState === WebSocket.OPEN) {
            client.send(message);
        }
    });
}
