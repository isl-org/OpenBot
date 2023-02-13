import React, {useContext, useState} from 'react';
import {useNavigate} from "react-router-dom";
import cross from "../../../assets/images/icon/cross-icon.png";
import crossDark from "../../../assets/images/icon/dark-cross.png";
import {Modal} from "@mui/material";
import {StoreContext} from "../../../context/context";
import styles from "./newProject.module.css"
import {ThemeContext} from "../../../App";
function NewProjectButton() {


    let navigate = useNavigate();
    const [open, setOpen] = useState(false);
    const {projectName, setProjectName} = useContext(StoreContext)
    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);
    const OpenNewProjectHandle = () => {
        let path = `playground`;
        navigate(path);
        handleOpen();
    }
    const {theme} = useContext(ThemeContext)


    return (
        <>
            <div className={styles.Content + " " + (theme === "dark" ? styles.MainDark : styles.MainLight)}
                 onClick={handleOpen}>
                <div className={styles.Button + " " + (theme === "dark" ? styles.ButtonDark : styles.ButtonLight)}>
                    <div className={styles.AddIconImage}>
                        <div className={styles.plus}>+</div>
                    </div>
                </div>
            </div>
            <Modal
                open={open}
                onClose={handleClose}
                className={"model"}
            >
                <div className={styles.model + " " + (theme === "dark" ? styles.modelDark : styles.modelLight)}>
                    <div
                        className={styles.ModelHeading + " " + (theme === "dark" ? styles.ModelHeadingDark : styles.ModelHeadingLight)}>
                        <div>Create a New Project</div>
                        {(theme === "light" ?
                            <img alt="" src={cross} className={styles.CrossIcon} onClick={handleClose}/> :
                            <img alt="" src={crossDark} className={styles.CrossIcon} onClick={handleClose}/>)}
                    </div>
                    <div className={styles.Input}>
                        <label className={styles.label}> Give your project a name. </label>
                        <input className={styles.InputArea} type="text" value={projectName}
                               onChange={(e) => setProjectName(e.target.value)}/>
                    </div>
                    <div className={styles.SaveBtn} onClick={() => {
                        OpenNewProjectHandle();
                    }}>Create
                    </div>
                </div>
            </Modal>
        </>

    );
}

export default NewProjectButton;
