import React from 'react';
import {useNavigate} from "react-router-dom";
import {NewProjectStyles as useStyles} from "./styles";
import cross from "../../../assets/Model/Cross.png";
import {Box, Modal, Typography} from "@material-ui/core";

function NewProjectButton(props) {
    const classes = useStyles();
    let navigate = useNavigate();
    const [open, setOpen] = React.useState(false);
    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);
    const OpenNewProjectHandle = () => {
        let path = `workspace`;
        navigate(path);
        handleOpen();

    }


    return (
        <>
            <div className={classes.Content} onClick={handleOpen}>
                <div className={classes.Button}>
                    <div className={classes.AddIconImage}>
                        <div className={classes.plus}>+</div>
                    </div>
                </div>
            </div>
            <Modal
                open={open}
                onClose={handleClose}
            >
                <Box className={classes.model}>
                    <div className={classes.ModelHeading}>
                        <div>Create a New Project</div>
                        <img src={cross} className={classes.CrossIcon} onClick={handleClose}/>
                    </div>
                    <div className={classes.Input}>
                        <label className={classes.lable}> Give your project a name. </label>
                        <input className={classes.InputArea} type="text"/>
                    </div>

                    <div className={classes.Savebtn} onClick={OpenNewProjectHandle}>Create</div>

                </Box>
            </Modal>
        </>

    );
}

export default NewProjectButton;