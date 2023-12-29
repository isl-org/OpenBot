export interface JsonRpcRequest {
    jsonrpc: "2.0";
    method: string;
    params: any;
}
export type JsonRpcResponse = JsonRpcResult | JsonRpcError;
export interface JsonRpcResult {
    jsonrpc: "2.0";
    id: number;
    result: any;
}
export interface JsonRpcError {
    jsonrpc: "2.0";
    id: number;
    error: any;
}
type ConnectionState = 'connecting' | 'connected' | 'closed';

type MessageCallback = (msg: JsonRpcRequest | JsonRpcResponse, event: MessageEvent) => void;
type TopicCallback = (msg: any, req: JsonRpcRequest, event: MessageEvent) => void;
type ConnectionStateCallback = (state: ConnectionState) => void;

const onMessageCallbacks = new Set<MessageCallback>();
const stateChangeCallbacks = new Set<ConnectionStateCallback>();
let socket = initSocket();
let jsonRpcId = 1;

export function jsonRpc<T>(method: string, ...params: any[]) {
    return new Promise<T>((resolve, reject) => {
        const id = jsonRpcId++;
        const done = onMessage((msg: any) => {
            if (msg.jsonrpc === '2.0' && msg.id === id) {
                done();
                if (msg.error) {
                    reject(msg.error);
                } else {
                    resolve(msg.result);
                }
            }
        });
        send({
            jsonrpc: '2.0',
            method,
            params,
            id,
        });
        setTimeout(() => {
            done();
            reject("JsonRpc timeout");
        }, 30000);
    });
}

export function send(data: any) {
    if (socket.readyState === WebSocket.OPEN) {
        const msg = JSON.stringify(data);
        console.log('ws.send', msg);
        socket.send(msg);
    } else {
        const done = onStateChange((state) => {
            if (state === 'connected') {
                done();
                send(data);
            }
        });
    }
}

export function subscribe(topic: string, callback: TopicCallback) {
    function realCallback(data: JsonRpcRequest | JsonRpcResponse, event: MessageEvent) {
        if ('method' in data && data.method === topic) {
            callback(data.params, data, event);
        }
    }

    onMessageCallbacks.add(realCallback);
    jsonRpc('subscribe', topic);
    const removeStateChange = onStateChange((state) => {
        if (state === 'connected') {
            jsonRpc('subscribe', topic);
        }
    })
    return () => {
        onMessageCallbacks.delete(realCallback);
        jsonRpc('unsubscribe', topic);
        removeStateChange();
    }
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
        ws.close();
    });
    ws.addEventListener('error', () => {
        console.error('ws.error');
    });

    return ws;
}
