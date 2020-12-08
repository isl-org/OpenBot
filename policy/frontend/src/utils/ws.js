let socket = initSocket();

export function send(data) {
    socket.send(data);
}
export function onMessage(callback) {
    socket.addEventListener('message', callback)
    return () => socket.removeEventListener('message', callback);
}

function initSocket() {
    const proto = window.location.protocol.replace('http', 'ws');
    const ws = new WebSocket(`${proto}//${window.location.host}/ws`);
    ws.addEventListener('open', () => {
        console.log('ws', 'open');
    });
    ws.addEventListener('message', (ev) => {
        console.log('ws', 'message', ev.data);
    });
    ws.addEventListener('close', () => {
        console.warn('ws', 'closed');
        setTimeout(() => {
            socket = initSocket();
        }, 3000);
    });
    ws.addEventListener('error', () => {
        console.error('ws', 'error');
    });

    return ws;
}
