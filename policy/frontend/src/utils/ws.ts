export interface WsMessage {
    event: string;
    [key: string]: any;
}
type ConnectionState = 'connecting' | 'connected' | 'closed';

type MessageCallback = (msg: WsMessage, event: MessageEvent) => void;
type ConnectionStateCallback = (state: ConnectionState) => void;

const onMessageCallbacks = new Set<MessageCallback>();
const stateChangeCallbacks = new Set<ConnectionStateCallback>();
let socket = initSocket();

export function send(data: any) {
    const msg = JSON.stringify(data);
    console.log('ws.send', msg)
    socket.send(msg);
}

export function onMessage(callback: MessageCallback) {
    onMessageCallbacks.add(callback);
    return () => {
        onMessageCallbacks.delete(callback);
    }
}


export function onStateChange(callback: ConnectionStateCallback) {
    stateChangeCallbacks.add(callback);
    return () => {
        stateChangeCallbacks.delete(callback);
    }
}

function initSocket() {
    stateChangeCallbacks.forEach(cb => cb('connecting'));
    const proto = window.location.protocol.replace('http', 'ws');
    const ws = new WebSocket(`${proto}//${window.location.host}/ws`);
    ws.addEventListener('open', () => {
        console.log('ws.open');
        stateChangeCallbacks.forEach(cb => cb('connected'));
    });
    ws.addEventListener('message', (event) => {
        console.log('ws.message', event.data);
        const data = JSON.parse(event.data);
        onMessageCallbacks.forEach((cb) => {
            cb(data, event);
        })
    });
    ws.addEventListener('close', () => {
        console.warn('ws.closed');
        stateChangeCallbacks.forEach(cb => cb('closed'));
        setTimeout(() => {
            socket = initSocket();
        }, 3000);
    });
    ws.addEventListener('error', () => {
        console.error('ws.error');
    });

    return ws;
}
