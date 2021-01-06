import {Link} from 'react-router-dom';
import {Panel} from 'rsuite';
import {ButtonBar} from 'src/components/ButtonBar';
import {DatasetInfo} from 'src/components/DatasetInfo';
import {GridView} from 'src/components/GridView';
import {DatasetModalWithButton} from 'src/modals/DatasetModal';
import {Dataset, useDatasets} from 'src/utils/useDatasets';

export function HomePage() {
    const datasets = useDatasets();
    return (
        <>
            <h3>Train datasets</h3>
            <GridView>
                {datasets.value.train.map(ds => (
                    <DatasetInfoWithPanel key={ds.name} {...ds}/>
                ))}
            </GridView>
            <ButtonBar>
                <DatasetModalWithButton defaultDir="train_data"/>
            </ButtonBar>

            <h3>Test datasets</h3>
            <GridView>
                {datasets.value.test.map(ds => (
                    <DatasetInfoWithPanel key={ds.name} {...ds}/>
                ))}
            </GridView>
            <ButtonBar>
                <DatasetModalWithButton defaultDir="test_data"/>
            </ButtonBar>
        </>
    );
}

function DatasetInfoWithPanel(props: Dataset) {
    return (
        <Panel header={<Link to={props.path}>{props.name}</Link>} shaded>
            <DatasetInfo sessions={props.sessions}/>
        </Panel>
    );
}
