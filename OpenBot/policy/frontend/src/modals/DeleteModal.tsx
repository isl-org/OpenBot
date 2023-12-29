import {Button, Modal, ModalProps, Notification} from 'rsuite';
import {useToggle} from 'src/utils/useToggle';
import {jsonRpc} from 'src/utils/ws';

type FolderType = 'dataset' | 'session' | 'model file';

export function DeleteModalWithButton({...props}: ModalProps & {type: FolderType, path: string}) {
    const [show, toggle] = useToggle(false);
    return <>
        <Button onClick={toggle}>Delete</Button>
        <DeleteModal show={show} onHide={toggle} {...props}/>
    </>;
}

export function DeleteModal({type, path, ...props}: ModalProps & {type: FolderType, path: string}) {
    async function handleDelete(type: FolderType, path: string) {
        try {
            if (type === 'session') {
                await jsonRpc('deleteSession', {path})
            } else if (type === 'model file') {
                await jsonRpc('deleteModelFile', {path})
            } else {
                await jsonRpc('deleteDataset', {path})
            }
        } catch (e) {
            Notification.warning({
                title: `${e}`
            });
        }
    }
    return <>
        <Modal backdrop="static" size="xs" {...props}>
            <Modal.Header>
                <Modal.Title>{path}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                Are you really want to delete this {type}?
                {type === 'session' && <img src={`${path}/preview.gif`} alt="preview"/>}
            </Modal.Body>
            <Modal.Footer>
                <Button onClick={() => handleDelete(type, path)} appearance="primary">
                    Yes
                </Button>
                <Button onClick={props.onHide} appearance="subtle">
                    No
                </Button>
            </Modal.Footer>
        </Modal>
    </>;
}
