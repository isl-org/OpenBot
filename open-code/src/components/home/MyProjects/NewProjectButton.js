import React, {useContext, useState} from 'react';
import {useNavigate} from "react-router-dom";
import {NewProjectStyles} from "./styles";
import cross from "../../../assets/Model/Cross.png";
import {Box, Modal} from "@mui/material";
import {StoreContext} from "../../../context/Context";
function NewProjectButton(props) {
    let navigate = useNavigate();
    const [open, setOpen] = useState(false);
    const {projectName,setProjectName} = useContext(StoreContext)
    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);
    const OpenNewProjectHandle = () => {
        let path = `playground`;
        navigate(path);
        handleOpen();
    }


    return (
        <>
            <div style={NewProjectStyles.Content} onClick={handleOpen}>
                <div style={NewProjectStyles.Button}>
                    <div style={NewProjectStyles.AddIconImage}>
                        <div style={NewProjectStyles.plus}>+</div>
                    </div>
                </div>
            </div>
            <Modal
                open={open}
                onClose={handleClose}
                className={"model"}
            >
                {/*<Box style={NewProjectStyles.model}>*/}
                    <div style={NewProjectStyles.model} >
                    <div style={NewProjectStyles.ModelHeading}>
                        <div>Create a New Project</div>
                        <img src={cross} style={NewProjectStyles.CrossIcon} onClick={handleClose}/>
                    </div>
                    <div style={NewProjectStyles.Input}>
                        <label style={NewProjectStyles.lable}> Give your project a name. </label>
                        <input style={NewProjectStyles.InputArea} type="text" value={projectName}
                               onChange={(e) => setProjectName(e.target.value)}/>
                    </div>

                    <div style={NewProjectStyles.SaveBtn} onClick={OpenNewProjectHandle}>Create</div>
                    </div>
                {/*</Box>*/}
            </Modal>
        </>

    );
}

export default NewProjectButton;
