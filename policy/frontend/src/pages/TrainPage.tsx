import {useEffect, useState} from 'react';
import {Button, Panel, Progress} from 'rsuite';
import {onMessage, send} from '../utils/ws';

export function TrainPage() {
    const [status, setStatus] = useState<'success' | 'fail' | 'active' | undefined>(undefined);
    const [percent, setPercent] = useState<number | undefined>(undefined);

    useEffect(() => {
        return onMessage((msg) => {
            switch (msg.event) {
                case 'started':
                    setStatus('active');
                    setPercent(undefined);
                    break;
                case 'progress':
                    setPercent(msg.current * 10);
                    break;
                case 'cancelled':
                    setStatus('fail');
                    break;
                case 'done':
                    setStatus('success');
                    break;
            }
        });
    }, [])
    return (
        <div>
            <h3>Train page TODO</h3>
            <Panel shaded style={{marginTop: 20}}>
                <Button onClick={() => send({
                    action: 'start',
                    payload: 0,
                })} appearance="primary">Start</Button>
                <Button onClick={() => send({
                    action: 'stop',
                    payload: 0,
                })}>Stop</Button>
                <div>
                    <Progress.Line percent={percent} status={status}/>
                </div>
            </Panel>
        </div>
    );
}
