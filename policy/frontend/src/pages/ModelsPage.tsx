import {distanceInWordsToNow} from 'date-fns';
import {styled} from 'goober';
import {Link, Route, Switch, useParams, useRouteMatch} from 'react-router-dom';
import {List, Panel} from 'rsuite';
import {GridView} from 'src/components/GridView';
import {Plot} from 'src/components/Plot';
import {DeleteModalWithButton} from 'src/modals/DeleteModal';
import {PublishModalWithButton} from 'src/modals/PublishModal';
import {useModel, useModelFiles, useModels} from 'src/utils/useModels';

export function ModelsPage() {
    const match = useRouteMatch();
    return (
        <Switch>
            <Route path={`${match.url}/:name`} component={ModelDetails}/>
            <Route component={ListView}/>
        </Switch>
    );
}

const TfliteList = styled(List as any)`
    .rs-list-item-content {
        display: flex;
        justify-content: space-between;
    }
`;

export function ListView() {
    const match = useRouteMatch();
    const models = useModels();
    const published = useModelFiles();
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
            <h3>Published model files</h3>
            <Panel shaded bodyFill>
                <TfliteList bordered>
                    <List.Item>
                        Published models only accessible only on your local network.<br/>
                        The android app will download these automatically.
                    </List.Item>
                    {published.value.map(({name, mtime}) => (
                        <List.Item key={name}>
                            <div>
                                <h6>{name}</h6>
                                {distanceInWordsToNow(new Date(mtime * 1000).toISOString())}
                            </div>
                            <DeleteModalWithButton path={name} type="model file"/>
                        </List.Item>
                    ))}
                </TfliteList>
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
            <PublishModalWithButton model={model.name}/>
        </Panel>
        <GridView>
            <Plot logs={model.logs} metric="MeanAbsoluteError"/>
            <Plot logs={model.logs} metric="direction_metric"/>
            <Plot logs={model.logs} metric="angle_metric"/>
            <Plot logs={model.logs} metric="loss"/>
        </GridView>
    </>;
}
