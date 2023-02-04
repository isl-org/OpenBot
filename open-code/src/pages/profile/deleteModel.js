import React, {useState} from 'react';
import {Box, Modal} from "@mui/material";
import {DeleteStyles} from "./styles"
import {useNavigate} from "react-router-dom";

function DeleteModel(props) {
    let navigate = useNavigate();
    const{setDeleteProject}=props
    const [open, setOpen] = useState(true);
    const handleClose = () => {
        setDeleteProject(false)
        return setOpen(false)

    };
    const handleDeleteProject = () => {
        let path = `/`;
        navigate(path);
    }
    return (
        <Modal
            open={open}
            onClose={handleClose}
        >
            <Box style={DeleteStyles.model}>
                <div style={DeleteStyles.ModelHeading}> Delete this file? </div>
                <div style={DeleteStyles.Input}>You cannot restore this file later.</div>
                <div style={DeleteStyles.btnGroup}>
                    <div style={DeleteStyles.CancelBtn} onClick={handleClose}>Cancel</div>
                    <div onClick={handleDeleteProject} style={DeleteStyles.DeleteBtn}>Delete</div>
                </div>
            </Box>
        </Modal>
    );
}

export default DeleteModel;