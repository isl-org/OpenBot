import {styled} from 'goober';
import {useState} from 'react';
import {
    Button,
    ControlLabel,
    Form,
    FormControl,
    FormGroup,
    HelpBlock,
    Icon,
    IconButton,
    Input,
    InputGroup,
    Modal,
    ModalProps,
    Notification,
    Radio,
    RadioGroup,
} from 'rsuite';
import {useModelFiles} from 'src/utils/useModels';
import {useToggle} from 'src/utils/useToggle';
import {jsonRpc} from 'src/utils/ws';

const FluidGroup = styled(FormGroup as any)`
    &.rs-form-group .rs-form-control-wrapper {
        overflow: hidden;
        float: none;
    }
`;

export function PublishModalWithButton(props: { model: string }) {
    const [show, toggle] = useToggle()
    return <>
        <IconButton appearance="ghost" onClick={toggle} icon={<Icon icon="send"/>}>
            Push to phone
        </IconButton>
        <HelpBlock tooltip>
            Will be accessible only on your local network.
            The android app will download it automatically.
        </HelpBlock>
        <PublishModal {...props} show={show} toggle={toggle}/>
    </>;
}

type DatasetModalProps = ModalProps & { model: string, toggle: () => void };

export function PublishModal({model, toggle, ...props}: DatasetModalProps) {
    const [checkpoint, setType] = useState('best');
    const [name, setName] = useState(model);
    const published = useModelFiles();
    const warning = published.value.some(p => p.name === name + '.tflite');

    async function save() {
        try {
            await jsonRpc('publishModel', {model, checkpoint, name})
            toggle();
        } catch (e) {
            Notification.warning({
                title: `${e}`,
            });
        }
    }

    return (
        <Modal backdrop="static" size="sm" {...props}>
            <Modal.Header>
                <Modal.Title>Publish tflite model</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form layout="horizontal">
                    <FluidGroup>
                        <ControlLabel>Name:</ControlLabel>
                        <FormControl value={name} onChange={setName} accepter={FileName}/>
                        {warning && <HelpBlock>Note: this file exists and will be overwritten</HelpBlock>}
                    </FluidGroup>
                    <FormGroup>
                        <ControlLabel>Checkpoint: </ControlLabel>
                        <RadioGroup inline appearance="picker" value={checkpoint} onChange={setType}>
                            <Radio value="best">Best</Radio>
                            <Radio value="last">Last</Radio>
                        </RadioGroup>
                    </FormGroup>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button onClick={() => save()} appearance="primary">
                    Save
                </Button>
                <Button onClick={toggle} appearance="subtle">
                    Cancel
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

function FileName(props: any) {
    return (
        <InputGroup>
            <Input  {...props}/>
            <InputGroup.Addon>.tflite</InputGroup.Addon>
        </InputGroup>

    );
}
