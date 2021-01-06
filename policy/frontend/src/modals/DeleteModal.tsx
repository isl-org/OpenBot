import {Button, Modal, ModalProps, Notification} from 'rsuite';
import {useToggle} from 'src/utils/useToggle';
import {jsonRpc} from 'src/utils/ws';

type FolderType = 'dataset' | 'session';

export function DeleteModal({type, path, ...props}: ModalProps & {type: FolderType, path: string}) {
    const [show, toggle] = useToggle(false);

    async function handleDelete(type: FolderType, path: string) {
        try {
            if (type === 'session') {
                await jsonRpc('deleteSession', {path})
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
        <Modal backdrop="static" size="xs" show={show} {...props}>
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
                <Button onClick={props.onHide || toggle} appearance="subtle">
                    No
                </Button>
            </Modal.Footer>
        </Modal>
    </>;
}
