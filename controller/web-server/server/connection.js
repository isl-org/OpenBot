//connection.js

const socketIO = require('socket.io');
const { Commands } = require('./command');
const RemoteKeyboard = require('./remote_keyboard');

let ioInstance;

const sendData = (event, data) => {
    console.log("sendData ======",data);
    if (ioInstance) {
        console.log("sending msg to client");
        ioInstance.emit(event, data);
    } else {
        console.log('Error: Socket.IO server not started.');
    }
};
const commands = new Commands(sendData);
const remoteKeyboard = new RemoteKeyboard(commands.getCommandHandler());
const startConnection = (server) => {
    ioInstance = socketIO(server, {
        cors: {
            origin: '*',
        },
    });

    ioInstance.on('connection', (socket) => {
        console.log('A user connected');

        socket.on('chatMessage', (message) => {
            console.log('Received message:', message);
            remoteKeyboard.processKey(JSON.parse(message).KEYPRESS);
            const responseMessage = `Server received: ${message}`;
            socket.emit('chatResponse', responseMessage);
        });

        socket.on('disconnect', () => {
            console.log('A user disconnected');
        });

        socket.on('msg', (data) => {
            console.log('msg');
            ioInstance.emit('msg', data);
        });

        socket.on('signalingMessage', (data) => {
            console.log('signalingMessage');
            ioInstance.emit('signalingMessage', data);
        });
    });
};

const stopConnection = () => {
    if (ioInstance) {
        ioInstance.close();
        console.log('Socket.IO server stopped');
    }
};



module.exports = {
    start: startConnection,
    stop: stopConnection,
    sendData: sendData,
    io: ioInstance
};
