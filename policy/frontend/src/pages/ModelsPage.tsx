import {List, Panel} from 'rsuite';
import {GridView} from 'src/components/GridView';
import {Plot} from 'src/components/Plot';
import {useModel, useModels} from 'src/utils/useModels';
import {Link, Route, Switch, useParams, useRouteMatch} from 'react-router-dom';

export function ModelsPage() {
    const match = useRouteMatch();
    return (
        <Switch>
            <Route path={`${match.url}/:name`} component={ModelDetails}/>
            <Route component={ListView}/>
        </Switch>
    );
}

export function ListView() {
    const match = useRouteMatch();
    const models = useModels();
    return (
        <>
            <h3>Trained models</h3>
            <Panel shaded bodyFill>
                <List bordered>
                    {models.value.map(name => (
                        <List.Item key={name}>
                            <Link to={`${match.url}/${name}`}>{name}</Link>
                        </List.Item>
                    ))}
                </List>
            </Panel>
        </>
    );
}

function ModelDetails() {
    const {name} = useParams<any>();
    const modelInfo = useModel(name);
    if (modelInfo.pending) {
        return null;
    }
    const model = modelInfo.value!;
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
