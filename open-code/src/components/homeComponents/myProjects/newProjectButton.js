import React, {useContext, useState} from 'react';
import {useNavigate} from "react-router-dom";
import {Modal} from "@mui/material";
import {StoreContext} from "../../../context/context";
import styles from "./newProject.module.css";
import {ThemeContext} from "../../../App";
import {Images} from "../../../utils/images";
import SimpleInputComponent from "../../inputComponent/simpleInputComponent";
import {localStorageKeys, Themes} from "../../../utils/constants";


function NewProjectButton(props) {
    const {isProject} = props
    let navigate = useNavigate();
    const [open, setOpen] = useState(false);
    const [isInputError, setIsInputError] = useState(true)
    const {
        projectName,
        setProjectName,
        setCurrentProjectXml,
        setCurrentProjectId,
    } = useContext(StoreContext)

    const handleOpen = () => {
        localStorage.setItem(localStorageKeys.currentProject, "");
        setCurrentProjectXml("")
        setProjectName();
        setCurrentProjectId();
        setOpen(true);
    }
    const handleClose = () => setOpen(false);
    const OpenNewProjectHandle = () => {
        if (!projectName || projectName <= 0) {
            setIsInputError(true)
        } else {
            setIsInputError(false)
            let path = `playground`;
            navigate(path);
            handleClose();
        }
    }
    const {theme} = useContext(ThemeContext)

    function handleProjectNameChange(name) {
        setProjectName(name);
    }

    return (
        <>
            <div className={styles.Content + " " + (theme === "dark" ? styles.MainDark : styles.MainLight)}
                 style={isProject > 0 ? {marginRight: "42px"} : {marginRight: 0}}
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
                        {(theme === Themes.light ?
                            <img alt="" src={Images.crossIcon} className={styles.CrossIcon} onClick={handleClose}/> :
                            <img alt="" src={Images.darkCrossIcon} className={styles.CrossIcon}
                                 onClick={handleClose}/>)}
                    </div>
                    <div className={styles.Input}>
                        <SimpleInputComponent inputType={"text"} extraStyle={`${styles.inputExtraStyle}`}
                                              inputTitle={"Give your project a name"}
                                              placeHolder={"Project Name"}
                                              value={projectName} extraMargin={styles.inputBoxMargin}
                                              onDataChange={handleProjectNameChange}/>
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
