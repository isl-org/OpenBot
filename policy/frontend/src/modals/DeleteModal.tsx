import {Button, Modal, ModalProps} from 'rsuite';
import {jsonRpc} from '../utils/ws';

export function DeleteModal({path, ...props}: ModalProps & {path: string}) {
    return (
        <Modal backdrop="static" size="xs" {...props}>
            <Modal.Header>
                <Modal.Title>Are you really want to delete this session?</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <img src={`${path}/preview.gif`} alt="preview"/>
            </Modal.Body>
            <Modal.Footer>
                <Button onClick={() => deleteSession(path)} appearance="primary">
                    Yes
                </Button>
                <Button onClick={props.onHide} appearance="subtle">
                    No
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

function deleteSession(path: string) {
    jsonRpc('deleteSession', {path})
}
