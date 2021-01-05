import {formatTime} from 'src/utils/formatTime';
import {Session} from 'src/utils/useDatasets';

export function DatasetInfo(props: { sessions: Session[] }) {
    return (
        <>
            <div>Sessions: {props.sessions.length}</div>
            <div>Length: {formatTime(props.sessions.reduce((sum, cur) => sum + cur.seconds, 0))}</div>
            <div>Frames: {props.sessions.reduce((sum, cur) => sum + cur.ctrl.length, 0)}</div>
        </>
    );
}
