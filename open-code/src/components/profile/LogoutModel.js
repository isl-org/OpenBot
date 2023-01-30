import React, {useState} from 'react';
import {Box, Modal} from "@mui/material";
import {LogoutStyles} from "./styles";

function LogoutModel(props) {
    const{setLogOut}=props
    const [open, setOpen] = useState(true);
    console.log(props)
    const handleClose = () => {
        setLogOut(false)
        return setOpen(false)

    };
    return (
        <Modal
            open={open}
            onClose={handleClose}
        >
            <Box style={LogoutStyles.model}>
                <div style={LogoutStyles.ModelHeading}>Confirm Logout</div>
                <div style={LogoutStyles.Input}>Are you sure you want to logout?</div>
                <div style={LogoutStyles.btnGroup}>
                    <div style={LogoutStyles.CancelBtn} onClick={handleClose}>Cancel</div>
                    <div style={LogoutStyles.OkBtn}>Ok</div>
                </div>
            </Box>
        </Modal>
    );
}

export default LogoutModel;