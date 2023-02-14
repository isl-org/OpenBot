import React, {useContext, useState} from 'react';
import {Box, Modal} from "@mui/material";
import DeleteStyles from "./deleteModel.module.css"
import {useNavigate} from "react-router-dom";
import {ThemeContext} from "../../App";
function DeleteModel(props) {
    const theme=useContext(ThemeContext)
    let navigate = useNavigate();
    const{setDeleteProject}=props
    const [open, setOpen] = useState(true);
    const handleClose = () => {
        setDeleteProject(false);
        return setOpen(false);
    };
    const handleDeleteProject = () => {
        let path = `/`;
        navigate(path);
    }

    return (
        <div >
        <Modal
            open={open}
            onClose={handleClose}
        >
            <Box className={DeleteStyles.model+" "+(theme.theme==="dark"? DeleteStyles.darkDeleteModel : DeleteStyles.lightDeleteModel)}>
                <div className={DeleteStyles.ModelHeading }> Delete this file? </div>
                <div className={DeleteStyles.Input+" "+(theme.theme==="dark"? DeleteStyles.darkInputModel : DeleteStyles.lightInputModel)}>You cannot restore this file later.</div>
                <div className={DeleteStyles.btnGroup}>
                    <div className={DeleteStyles.CancelBtn}  onClick={handleClose}>Cancel</div>
                    <div onClick={handleDeleteProject} className={DeleteStyles.DeleteBtn}>Delete</div>
                </div>
            </Box>

        </Modal>
        </div>
    );
}

export default DeleteModel;