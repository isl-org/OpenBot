import {useEffect} from 'react';
import {onMessage, send} from './ws';

const empty = [];

export function useWS(callback, deps) {
    useEffect(() => {
        if (callback) {
            return onMessage(callback);
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, deps || empty)
    return {onMessage, send};
}

