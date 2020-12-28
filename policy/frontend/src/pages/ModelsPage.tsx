import {List, Panel} from 'rsuite';
import {GridView} from 'src/components/GridView';
import {Plot} from 'src/components/Plot';
import {Model, useModels} from 'src/utils/useModels';
import {Link, Route, Switch, useParams, useRouteMatch} from 'react-router-dom';

export function ModelsPage() {
    const match = useRouteMatch();
    return (
        <Switch>
            <Route path={match.url + '/:name'} component={ModelDetails}/>
            <Route component={ListView}/>
        </Switch>
    );
}

export function ListView() {
    const models = useModels();
    return (
        <>
            <h3>Models</h3>
            <Panel shaded bodyFill>
                <List bordered>
                    {models.value.map((m: Model) => (
                        <ModelInfo key={m.name} {...m}/>
                    ))}
                </List>
            </Panel>
        </>
    );
}

function ModelInfo(props: Model) {
    return (
        <List.Item>
            <Link to={props.path}>{props.name}</Link>
        </List.Item>
    );
}

function ModelDetails() {
    const {name} = useParams<any>();
    const models = useModels();
    const model = models.value.find(m => m.name === name);
    if (!model) {
        return null;
    }
    return <>
        <Panel header={model.name} shaded>
            Params:
            <pre>{JSON.stringify(model.params, null, 2)}</pre>
            Last log:
            <pre>{JSON.stringify(model.logs[model.logs.length-1], null, 2)}</pre>
        </Panel>
        <GridView>
            <Plot logs={model.logs} metric="MeanAbsoluteError"/>
            <Plot logs={model.logs} metric="direction_metric"/>
            <Plot logs={model.logs} metric="angle_metric"/>
            <Plot logs={model.logs} metric="loss"/>
        </GridView>
    </>;
}
