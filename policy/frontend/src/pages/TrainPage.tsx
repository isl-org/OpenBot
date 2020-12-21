import {useEffect, useRef, useState} from 'react';
import {
    Button, ControlLabel,
    Form, FormControl, FormGroup,
    InputNumber, InputNumberProps,
    Message, HelpBlock,
    Panel, Progress,
    Toggle, ToggleProps,
} from 'rsuite';
import {ButtonBar} from '../components/ButtonBar';
import {GridView} from '../components/GridView';
import {useRpc} from '../utils/useRpc';
import {jsonRpc, subscribe} from '../utils/ws';

interface FormValues {
    TRAIN_BATCH_SIZE: number;
    TEST_BATCH_SIZE: number;
    LEARNING_RATE: number;
    NUM_EPOCHS: number;
    BATCH_NORM: boolean;
    FLIP_AUG: boolean;
    CMD_AUG: boolean;
}

const emptyForm = {} as FormValues;

export function TrainPage() {
    const [status, setStatus] = useState<'success' | 'fail' | 'active' | undefined>(undefined);
    const [epoch, setEpoch] = useState<number | undefined>(undefined);
    const [percent, setPercent] = useState<number | undefined>(undefined);
    const [model, setModel] = useState('');
    const [rnd, setRnd] = useState(0);
    const fromValue = useRef<FormValues>(emptyForm);
    function onChange(values: any) {
        Object.keys(values).forEach((k) => {
            const v = values[k];
            values[k] = typeof v === 'string' ? Number(v) : v;
        });
        console.log(values)
        fromValue.current = values;
    }
    const {pending, value} = useRpc<FormValues>(emptyForm, 'getHyperparameters');

    useEffect(() => {
        return subscribe('training', (msg) => {
            switch (msg.event) {
                case 'started':
                    setStatus('active');
                    setPercent(undefined);
                    break;
                case 'preview':
                    setRnd(Date.now());
                    break;
                case 'progress':
                    setEpoch(Math.round(msg.payload.epoch * 100));
                    setPercent(Math.round(msg.payload.train * 100));
                    break;
                case 'cancelled':
                    setStatus('fail');
                    break;
                case 'done':
                    setRnd(Date.now());
                    setStatus('success');
                    setModel(msg.payload.model);
                    break;
            }
        });
    }, [])

    if (pending) {
        return null;
    }

    return (
        <div className="trainPage">
            <h3>Model training</h3>
            <Panel shaded header="Hyperparameters">
                <Message description="You may have to tune the learning rate and batch size depending on your available compute resources and dataset. As a general rule of thumb, if you increase the batch size by a factor of n, you can increase the learning rate by a factor of sqrt(n)."/>
                <Form onChange={onChange} formDefaultValue={value} layout="horizontal">
                    <NumberParam name="TRAIN_BATCH_SIZE" min={1}/>
                    <NumberParam name="TEST_BATCH_SIZE" min={1}/>
                    <NumberParam name="LEARNING_RATE" min={0} step={0.0001}/>
                    <NumberParam
                        name="NUM_EPOCHS"
                        min={1}
                        help="For debugging and hyperparamter tuning, you can set the number of epochs to a small value like 10. If you want to train a model which will achieve good performance, you should set it to 50 or more. In our paper we used 100."
                    />
                    <Panel bordered header="Advanced parameters" collapsible>
                        <Message type="warning" description="Don't change these unless you know what you are doing" />
                        <BoolParam name="BATCH_NORM" defaultChecked={value.BATCH_NORM}/>
                        <BoolParam name="FLIP_AUG" defaultChecked={value.FLIP_AUG}/>
                        <BoolParam name="CMD_AUG" defaultChecked={value.CMD_AUG}/>
                    </Panel>
                </Form>
                <ButtonBar>
                    <Button onClick={() => jsonRpc('start', fromValue.current)} appearance="primary">Start</Button>
                    <Button onClick={() => jsonRpc('stop')}>Stop</Button>
                </ButtonBar>
            </Panel>
            {!!percent && (
                <Panel shaded header="Progress">
                    <div>
                        Current epoch:
                        <Progress.Line percent={epoch}/>
                    </div>
                    <div>
                        Training:
                        <Progress.Line percent={percent} status={status}/>
                    </div>
                </Panel>
            )}
            {!!rnd && (
                <Panel bodyFill shaded header="Train preview" collapsible defaultExpanded>
                    <img alt="preview thumbnails" src={"/models/train_preview.png?rnd="+rnd}/>
                </Panel>
            )}
            {model && (
                <Panel bodyFill shaded header="Test preview" collapsible defaultExpanded>
                    <img alt="preview thumbnails" src={`/models/${model}/logs/test_preview.png?rnd=${rnd}`}/>
                </Panel>
            )}
            {model && (
                <GridView>
                    <Plot model={model} rnd={rnd} name="error"/>
                    <Plot model={model} rnd={rnd} name="direction"/>
                    <Plot model={model} rnd={rnd} name="angle"/>
                    <Plot model={model} rnd={rnd} name="loss"/>
                </GridView>
            )}
        </div>
    );
}

function Plot(props: {name: string, model: string, rnd: any}) {
    return (
        <Panel bodyFill shaded>
            <img alt={`${props.name} plot`} src={`/models/${props.model}/logs/${props.name}.png?rnd=${props.rnd}`}/>
        </Panel>
    );
}

function NumberParam({help, ...props}: InputNumberProps) {
    return (
        <FormGroup>
            <ControlLabel>{props.name}</ControlLabel>
            <FormControl accepter={InputNumber} {...props}/>
            {help && <HelpBlock>{help}</HelpBlock>}
        </FormGroup>
    );
}

function BoolParam(props: ToggleProps) {
    return (
        <div className="bool-input">
            <ControlLabel>{props.name}</ControlLabel>
            <FormControl accepter={Toggle} {...props} />
        </div>
    );
}
