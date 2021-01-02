import {useEffect} from 'react';
import {Link, Route, Switch, useRouteMatch} from 'react-router-dom';
import {Dropdown, Panel} from 'rsuite';
import {SessionPage} from 'src/pages/SessionPage';
import {formatTime} from 'src/utils/formatTime';
import {Session} from 'src/utils/useDatasets';
import {GridView} from '../components/GridView';
import {DeleteModal} from '../modals/DeleteModal';
import {MoveModal} from '../modals/MoveModal';
import {useRpc} from '../utils/useRpc';
import {useToggle} from '../utils/useToggle';
import {subscribe} from '../utils/ws';

const defaultValue =  [] as Session[];

export function SessionListPage() {
    const match = useRouteMatch();
    return (
        <Switch>
            <Route path={match.url + '/:path'} component={SessionPage}/>
            <Route component={ListView}/>
        </Switch>
    );
}

function ListView() {
    const match = useRouteMatch();
    const {value, reload} = useRpc(defaultValue, 'listDir',  {path: match.url});
    useEffect(() => subscribe('session', reload), [reload])
    return <>
        <h3>Sessions in {match.url}</h3>
        <GridView>
            {!value.length && (
                <Panel shaded>
                    No sessions
                </Panel>
            )}
            {value.map(s => (
                <Panel key={s.path} bodyFill shaded>
                    <SessionComp {...s}/>
                </Panel>
            ))}
        </GridView>
    </>;
}

function SessionComp(props: Session) {
    const [showMove, toggleMove] = useToggle(false);
    const [showDel, toggleDel] = useToggle(false);
    return (
        <>
            <img src={`${props.path}/preview.gif`} alt="preview"/>
            <Panel>
                <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                    <Link to={props.path}>{props.name}</Link>
                    <Dropdown title="Actions" >
                        <Dropdown.Item onSelect={toggleMove}>Move...</Dropdown.Item>
                        <Dropdown.Item onSelect={toggleDel}>Remove...</Dropdown.Item>
                    </Dropdown>
                </div>
                <div>Length: {formatTime(props.seconds)}</div>
                <div>Frames: {props.ctrl.length}</div>
                {props.error && <div>Error: {props.error}</div>}
            </Panel>
            {showMove && <MoveModal path={props.path} show={showMove} onHide={toggleMove}/>}
            <DeleteModal path={props.path} show={showDel} onHide={toggleDel}/>
        </>
    );
}
