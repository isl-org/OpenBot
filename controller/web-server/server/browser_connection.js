const WebSocket = require('ws');

const browserWs = new WebSocket('ws://localhost:8080');

browserWs.on('open', () => {
    console.log('Browser connected to signaling server.');
});

// Function to send commands to the browser client.
function sendCommandToBrowserClient(command) {
    browserWs.send(command);
}

module.exports = { sendCommandToBrowserClient };
