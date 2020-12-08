import {Button} from 'rsuite';
import {useWS} from '../utils/useWS';


export function TrainPage() {
    const socket = useWS((ev) => {
        // ev.data
    }, [])

    return (
        <div>
            Train page TODO
            <Button onClick={() => socket.send('start')}>Start</Button>
        </div>
    );
}
