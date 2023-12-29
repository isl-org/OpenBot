import {Link, Route, Switch, useRouteMatch} from 'react-router-dom';
import {Dropdown, Panel} from 'rsuite';
import {ButtonBar} from 'src/components/ButtonBar';
import {DatasetInfo} from 'src/components/DatasetInfo';
import {DatasetModal} from 'src/modals/DatasetModal';
import {SessionPage} from 'src/pages/SessionPage';
import {formatTime} from 'src/utils/formatTime';
import {Session, useDataset} from 'src/utils/useDatasets';
import {GridView} from '../components/GridView';
import {DeleteModal} from '../modals/DeleteModal';
import {MoveModal} from '../modals/MoveModal';
import {useToggle} from '../utils/useToggle';

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
    const [showMove, toggleMove] = useToggle();
    const [showDel, toggleDel] = useToggle();
    const match = useRouteMatch<any>();
    const dir = match.url.match(/\/(.*)\//)
        ?.pop();
    const {value} = useDataset(match.url);
    return <>
        <h3>Sessions in {match.url}</h3>
        <Panel shaded>
            <DatasetInfo sessions={value}/>
            {dir && (
                <ButtonBar>
                    <Dropdown title="Actions" placement="bottomEnd">
                        <Dropdown.Item onSelect={toggleMove}>Move/Rename...</Dropdown.Item>
                        <Dropdown.Item onSelect={toggleDel}>Delete...</Dropdown.Item>
                    </Dropdown>
                    <DatasetModal defaultDir={dir} name={match.params.dataset} show={showMove} toggle={toggleMove}/>
                    <DeleteModal type="dataset" path={match.url} show={showDel} onHide={toggleDel}/>
                </ButtonBar>
            )}
        </Panel>
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
    const [showMove, toggleMove] = useToggle();
    const [showDel, toggleDel] = useToggle();
    return (
        <>
            <img src={`${props.path}/preview.gif`} alt="preview"/>
            <Panel>
                <div style={{display: 'flex', justifyContent: 'space-between', alignItems: 'center'}}>
                    <Link to={props.path}>{props.name}</Link>
                    <Dropdown title="Actions" placement="bottomEnd">
                        <Dropdown.Item onSelect={toggleMove}>Move...</Dropdown.Item>
                        <Dropdown.Item onSelect={toggleDel}>Delete...</Dropdown.Item>
                    </Dropdown>
                </div>
                <div>Length: {formatTime(props.seconds)}</div>
                <div>Frames: {props.ctrl.length}</div>
                {props.error && <div>Error: {props.error}</div>}
            </Panel>
            {showMove && <MoveModal path={props.path} show={showMove} onHide={toggleMove}/>}
            <DeleteModal type="session" path={props.path} show={showDel} onHide={toggleDel}/>
        </>
    );
}
