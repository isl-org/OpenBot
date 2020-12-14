import {useEffect} from 'react';
import {Link, Route, Switch, useParams, useRouteMatch} from 'react-router-dom';
import {Dropdown, Loader, Panel} from 'rsuite';
import {Session} from 'src/utils/useDatasets';
import {GridView} from '../components/GridView';
import {DeleteModal} from '../modals/DeleteModal';
import {MoveModal} from '../modals/MoveModal';
import {useFetch} from '../utils/useFetch';
import {useToggle} from '../utils/useToggle';
import {onMessage} from '../utils/ws';

const defaultValue = {
    basename: '',
    path: '',
    session: undefined as Session | undefined,
    file_list: [] as Session[],
}

export function UploadedPage() {
    const match = useRouteMatch();
    return (
        <Switch>
            <Route path={match.url + '/:path'} component={SessionPanel}/>
            <Route component={ListView}/>
        </Switch>
    );
}

function SessionPanel() {
    const {path} = useParams<any>();
    const info = useFetch(defaultValue, '/uploaded/' + path);
    const {session} = info.value;
    return (
        <Panel shaded>
            <h3>{path}</h3>
            {session ? <SessionComp {...session}/> : <Loader/>}
        </Panel>
    );
}

function ListView() {
    const {reload, value} = useFetch(defaultValue, '/uploaded');
    useEffect(() => onMessage((msg) => {
        if (msg.event === "deleteSessionSuccess") {
            reload();
        }
    }), [reload])
    return <>
        <h3>Uploaded sessions</h3>
        <GridView>
            {value.file_list.map(s => (
                <Panel key={s.path} style={{overflow: 'visible'}} shaded>
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
        <div>
            <img src={`${props.path}/preview.gif`} alt="preview"/>
            <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                <Link to={props.path}>{props.name}</Link>
                <Dropdown title="Actions">
                    <Dropdown.Item onSelect={toggleMove}>Move...</Dropdown.Item>
                    <Dropdown.Item onSelect={toggleDel}>Remove...</Dropdown.Item>
                </Dropdown>
            </div>
            <div>Frames: {props.frames}</div>
            {showMove && <MoveModal path={props.path} show={showMove} onHide={toggleMove}/>}
            <DeleteModal path={props.path} show={showDel} onHide={toggleDel}/>
        </div>
    );
}
