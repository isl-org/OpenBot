// server.js

const RemoteKeyboard = require('./remote_keyboard');
const {Commands} = require('./commands');
const WebSocket = require('ws');
const {ws} = require("http2-proxy");
const {processKey} = require("./remote_keyboard");
const wss = new WebSocket.Server({ port: 8080 }, () => {
    console.log("Signaling server is now listening on port 8080");
});

// Broadcast to all.
wss.broadcast = (ws, data) => {
    let obj = JSON.parse(data);
    let key = Object.keys(obj)[0];
    //discarding the keypress
    if(key === "KEYPRESS"){
        return;
    }
    wss.clients.forEach((client) => {
        if (client !== ws && client.readyState === WebSocket.OPEN) {
            client.send(data);
        }
    });
};
const sendToBot = (message) => {
    wss.broadcast(ws, message);
}

const command = new Commands(sendToBot);
const remoteKeyboard = new RemoteKeyboard(command.getCommandHandler());
wss.on('connection', (ws) => {
    console.log(`Client connected. Total connected clients: ${wss.clients.size}`);

    ws.on("message", function message(data, isBinary) {
        const message = isBinary ? data : data.toString();
        wss.broadcast(ws, message);
        console.log(JSON.parse(data));
        let obj = JSON.parse(data);
        switch (Object.keys(obj)[0]) {
            case "KEYPRESS" :
                remoteKeyboard.processKey(obj.KEYPRESS)
                break;
            default:
                break;
        }
    });

    ws.onclose = () => {
        console.log(`Client disconnected. Total connected clients: ${wss.clients.size}`);
    };
});
