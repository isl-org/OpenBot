import {Panel} from 'rsuite';
import {GridView} from 'src/components/GridView';
import {Model, useModels} from 'src/utils/useModels';
import {Link} from 'react-router-dom';

export function ModelsPage() {
    const models = useModels();
    return (
        <>
            <h3>Models</h3>
            {models.value.map((m: Model) => (
                <ModelInfo key={m.name} {...m}/>
            ))}
        </>
    );
}

function ModelInfo(props: Model) {
    return (
        <Panel header={<Link to={props.name}>{props.name}</Link>} shaded>
            <GridView>
                <Plot model={props.name} rnd="" name="error"/>
                <Plot model={props.name} rnd="" name="direction"/>
                <Plot model={props.name} rnd="" name="angle"/>
                <Plot model={props.name} rnd="" name="loss"/>
            </GridView>
        </Panel>
    );
}

function Plot(props: {name: string, model: string, rnd: any}) {
    return (
        <img alt={`${props.name} plot`} src={`/models/${props.model}/logs/${props.name}.png?rnd=${props.rnd}`}/>
    );
}
