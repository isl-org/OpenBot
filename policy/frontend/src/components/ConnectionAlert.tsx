import {useEffect} from 'react';
import {Notification} from 'rsuite';
import {onStateChange} from '../utils/ws';


export function ConnectionAlert() {
    useEffect(() => onStateChange((state) => {
        if (state !== 'connected') {
            Notification.warning({
                title: 'Not connected',
                key: 'connection'
            });
        } else {
            Notification.close('connection');
        }
    }), [])
    return null;
}
