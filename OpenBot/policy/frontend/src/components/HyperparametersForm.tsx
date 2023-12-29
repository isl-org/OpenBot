import {useEffect, useRef} from 'react';
import {Button, ControlLabel, Form, FormControl, FormGroup, Message, Panel, Placeholder, SelectPicker} from 'rsuite';
import {BoolParam} from 'src/components/BoolParam';
import {ButtonBar} from 'src/components/ButtonBar';
import {NumberParam} from 'src/components/NumberParam';
import {Hyperparametes} from 'src/utils/useProgress';
import {useRpc} from 'src/utils/useRpc';
import {jsonRpc} from 'src/utils/ws';

const models = [
    {label: 'cil', value: 'cil'},
    {label: 'cil_mobile', value: 'cil_mobile'},
    {label: 'cil_mobile_fast', value: 'cil_mobile_fast'},
    {label: 'pilot_net', value: 'pilot_net'},
];

const emptyForm = {} as Hyperparametes;

export function HyperparametersForm() {
    const formValue = useRef<Hyperparametes>(emptyForm);
    const {pending, value} = useRpc<Hyperparametes>(formValue.current, 'getHyperparameters');
    useEffect(() => {
        formValue.current = value;
    }, [value])

    if (pending) {
        return <Placeholder.Paragraph rows={20}/>;
    }

    function onChange(values: any) {
        Object.keys(values).forEach((k) => {
            if (k === 'MODEL') {
                return;
            }
            const v = values[k];
            values[k] = typeof v === 'string' ? Number(v) : v;
        });
        console.log(values)
        formValue.current = values;
    }

    return (
        <>
            <Message description="You may have to tune the learning rate and batch size depending on your available compute resources and dataset. As a general rule of thumb, if you increase the batch size by a factor of n, you can increase the learning rate by a factor of sqrt(n)."/>
            <Form onChange={onChange} formDefaultValue={value} layout="horizontal">
                <FormGroup>
                    <ControlLabel>MODEL</ControlLabel>
                    <FormControl name="MODEL" accepter={SelectPicker} data={models} block/>
                </FormGroup>
                <BoolParam
                    name="USE_LAST"
                    defaultChecked={value.USE_LAST}
                    help="Continue training from last checkpoint"
                />
                <NumberParam name="LEARNING_RATE" min={0} step={0.0001}/>
                <NumberParam name="TRAIN_BATCH_SIZE" min={1}/>
                <NumberParam name="TEST_BATCH_SIZE" min={1}/>
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
                <Message
                    title="Note"
                    description="If you have a trained model with the same parameters, the old model will be overwritten."
                />
            </Form>
            <ButtonBar>
                <Button onClick={() => jsonRpc('start', formValue.current)} appearance="primary">Start</Button>
            </ButtonBar>
        </>
    );
}
