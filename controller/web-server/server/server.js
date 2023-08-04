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
        console.log(JSON.parse(message));
        let msg = JSON.parse(message);
        if(Object.keys(msg)[0] === 'id'){
            createOrJoinRoom(msg.id,ws);
        }
        else if(Object.keys(msg)[0] === 'driveCmd'){

        }
        else if('command'){

        }

        sendToBot(ws,message);
    });

    ws.onclose = () => {
        console.log(`Client disconnected. Total connected clients: ${wss.clients.size}`);
    };
});

// Function to ask for client's id
const askIdOfClient = (ws) => {
    console.log("Asking for client's id");
    let request = {
        id: "request-id"
    };
    ws.send(JSON.stringify(request));
};

const createOrJoinRoom = (id, ws) => {

    console.log("inside creating room");
    // Check if the room with the given id exists
    if (!rooms.has(id) || rooms.get(id).clients[1] !== null) {
        // Room does not exist or is full (has two clients already)
        let room = {
            clients: [ws, null]
        };
        rooms.set(id, room);
    } else {
        // Room exists and has space for the new client
        let room = rooms.get(id);
        room.clients[1] = ws;
        rooms.set(id, room);
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

const sendToBot = (ws,message) => {
    wss.broadcast(ws, message);
};
