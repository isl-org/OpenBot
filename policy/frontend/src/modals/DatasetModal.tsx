import {useState} from 'react';
import {useHistory} from 'react-router-dom';
import {
    Button,
    ControlLabel,
    Form,
    FormControl,
    FormGroup,
    Icon,
    IconButton,
    Modal,
    ModalProps,
    Notification,
    Radio,
    RadioGroup,
} from 'rsuite';
import {useToggle} from 'src/utils/useToggle';
import {jsonRpc} from '../utils/ws';

export function DatasetModalWithButton(props: { defaultDir: string }) {
    const [show, toggle] = useToggle()
    return <>
        <IconButton appearance="ghost" onClick={toggle} icon={<Icon icon="plus"/>}>Create dataset</IconButton>
        <DatasetModal {...props} show={show} toggle={toggle}/>
    </>;
}

type DatasetModalProps = ModalProps & { defaultDir: string, name?: string, toggle: () => void };

export function DatasetModal({defaultDir, name, toggle, ...props}: DatasetModalProps) {
    const title = name ? 'Move/Rename dataset' : 'Create dataset';
    const [dir, setDir] = useState(defaultDir);
    const [newName, setName] = useState(name || '');
    const history = useHistory();

    async function save(newDir: string, newName: string, oldDir?: string, oldName?: string) {
        try {
            if (oldName) {
                await jsonRpc('renameDataset', {newDir, newName, oldDir, oldName})
                history.replace(`/${newDir}/${newName}`)
                toggle();
            } else {
                await jsonRpc('createDataset', {newDir, newName})
                toggle();
            }
        } catch (e) {
            Notification.warning({
                title: `${e}`,
            });
        }
    }

    return (
        <Modal backdrop="static" size="sm" {...props}>
            <Modal.Header>
                <Modal.Title>{title}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form layout="horizontal">
                    <FormGroup>
                        <ControlLabel>Name:</ControlLabel>
                        <FormControl value={newName} onChange={setName}/>
                    </FormGroup>
                    <FormGroup>
                        <ControlLabel>Dir: </ControlLabel>
                        <RadioGroup inline appearance="picker" value={dir} onChange={setDir}>
                            <Radio value="train_data">Train</Radio>
                            <Radio value="test_data">Test</Radio>
                        </RadioGroup>
                    </FormGroup>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button onClick={() => save(dir, newName, defaultDir, name)} appearance="primary">
                    Save
                </Button>
                <Button onClick={toggle} appearance="subtle">
                    Cancel
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

