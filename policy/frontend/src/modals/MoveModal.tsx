import {useRef} from 'react';
import {Button, ControlLabel, Form, FormGroup, Modal, ModalProps, Tree} from 'rsuite';
import {Dataset, useDatasets} from 'src/utils/useDatasets';
import {send} from 'src/utils/ws';

export function MoveModal({path, ...props}: ModalProps & {path: string}) {
    const datasets = useDatasets();
    const data = makeData(datasets.value);
    const fromValue = useRef<any>();
    function onChange(values: any) {
        fromValue.current = values;
    }
    function onSubmit() {
        const {new_path} = fromValue.current;
        send({
            action: 'moveSession',
            payload: {
                path,
                new_path,
            },
        })
    }
    return (
        <Modal backdrop="static" size="xs" {...props}>
            <Form onChange={onChange} onSubmit={onSubmit}>
                <Modal.Header>
                    <Modal.Title>Move session</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <FormGroup>
                        <ControlLabel>To folder:</ControlLabel>
                        <Tree
                            name="new_path"
                            data={data}
                            defaultValue="uploaded"
                            defaultExpandAll
                            disabledItemValues={['train', 'test']}
                            block
                        />
                    </FormGroup>
                </Modal.Body>
                <Modal.Footer>
                    <Button type="submit" appearance="primary">
                        Move
                    </Button>
                    <Button onClick={props.onHide} appearance="subtle">
                        Cancel
                    </Button>
                </Modal.Footer>
            </Form>
        </Modal>
    );
}

function makeData(datasets: { test: Dataset[]; train: Dataset[] }) {
    return [{
        value: 'uploaded',
        label: 'uploaded',
    }, {
        value: 'train',
        label: 'train',
        children: datasets.train.map(value => ({
            value: value.path,
            label: value.name,
        })),
    }, {
        value: 'test',
        label: 'test',
        children: datasets.test.map(value => ({
            value: value.path,
            label: value.name,
        })),
    }];
}
