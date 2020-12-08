import {useState} from 'react';
import {Link, Route, Switch, useParams, useRouteMatch} from 'react-router-dom';
import {Button, ControlLabel, Dropdown, Form, FormControl, FormGroup, Loader, Panel, TreePicker} from 'rsuite';
import {useFetch} from '../utils/useFetch';

const defaultValue = {
    basename: '',
    path: '',
    file_list: [],
}

export function UploadedPage() {
    const match = useRouteMatch();
    return (
        <Switch>
            <Route path={match.url + '/:path/move'} component={MoveForm}/>
            <Route path={match.url + '/:path'} component={SessionView}/>
            <Route component={GridView}/>
        </Switch>
    );
}

function SessionView() {
    const {path} = useParams();
    const info = useFetch(defaultValue, '/uploaded/' + path);
    const {session} = info.value;
    return (
        <Panel shaded>
            <h3>{path}</h3>
            {session ? <Session {...session}/> : <Loader/>}
        </Panel>
    );
}

function MoveForm() {
    const [formValue, setFormValue] = useState();
    const {path} = useParams();
    const train_datasets = ['openbot1'];
    const test_datasets = [];
    const data = [{
        value: 'uploaded',
        label: 'uploaded',
    }, {
        value: 'train',
        label:'train',
        children: train_datasets.map(value => ({
            value: 'train_dataset/' + value,
            label: value,
        })),
    }];
    function move(a, e) {
        const values = new FormData(e.currentTarget)
        console.log(values, formValue)
    }
    return <>
        <Panel shaded style={{marginBottom: 20}}>
            <Form onSubmit={move} onChange={setFormValue}>
                <h3>Move session</h3>
                <FormGroup>
                    <ControlLabel>To folder:</ControlLabel>
                    <FormControl
                        name="new_path"
                        accepter={TreePicker}
                        data={data}
                        defaultExpandAll
                    />
                </FormGroup>
                <Button type="button">Back</Button>
                <Button type="submit" appearance="primary">Save</Button>
            </Form>
        </Panel>
        <SessionView/>
    </>;
}

function GridView() {
    const uploaded = useFetch(defaultValue, '/uploaded');
    return <>
        <h3>Uploaded sessions</h3>
        <div className="list">
            {uploaded.value.file_list.map(s => (
                <Session {...s}/>
            ))}
        </div>
    </>;
}

function Session(props) {
    console.log(props)
    return (
        <div>
            <img src={`${props.path}/preview.gif`} alt="preview"/><br/>
            <Link to={props.path}>{props.name}</Link>

            <Dropdown title="Actions">
                <Dropdown.Item href={`#${props.path}/move`}>Move...</Dropdown.Item>
                <Dropdown.Item>Remove...</Dropdown.Item>
            </Dropdown>
            <pre>
                {JSON.stringify(props, null, 2)}
            </pre>
        </div>
    );
}
