// botConnection.js
const WebSocket = require('ws');

const botWs = new WebSocket('ws://battle-tiny-wrench.glitch.me');
const RemoteKeyboard = require('./remote_keyboard');
const Commands = require('./commands')
const {CommandHandler} = require("./commands");
botWs.on('open', () => {
    console.log('Bot connected to signaling server.');
});

// Function to send a message to the server (server.js).
function sendMessageToServer(message) {
    console.log("Sending Data =======" , message);
    botWs.send(JSON.stringify(message));
}





module.exports = { sendMessageToServer };

