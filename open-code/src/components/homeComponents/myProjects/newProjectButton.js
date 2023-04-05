import React, {useContext, useState} from 'react';
import {useNavigate} from "react-router-dom";
import {Modal, useTheme} from "@mui/material";
import {StoreContext} from "../../../context/context";
import styles from "./newProject.module.css";
import {ThemeContext} from "../../../App";
import {Images} from "../../../utils/images";
import SimpleInputComponent from "../../inputComponent/simpleInputComponent";
import {localStorageKeys, Themes} from "../../../utils/constants";
import useMediaQuery from "@mui/material/useMediaQuery";


function NewProjectButton(props) {
    const {isProject} = props;
    let navigate = useNavigate();
    const [open, setOpen] = useState(false);
    const [isInputError, setIsInputError] = useState(true);
    const themes = useTheme();
    const isMobile = useMediaQuery(themes.breakpoints.down('md'));
    const {
        projectName,
        setProjectName,
        setCurrentProjectXml,
        setCurrentProjectId,
        setFileId,
    } = useContext(StoreContext)

    const handleOpen = () => {
        localStorage.setItem(localStorageKeys.currentProject, "");
        setCurrentProjectXml("")
        setProjectName();
        setCurrentProjectId();
        setOpen(true);
        setFileId("")
    }
    const handleClose = () => setOpen(false);

    const OpenNewProjectHandle = () => {
        if (!projectName || projectName <= 0) {
            setIsInputError(true)
        } else {
            let updatedProjectName = projectName;
            const getAllProjects = localStorage.getItem(localStorageKeys.allProjects);
            let projectsArray = JSON.parse(getAllProjects);
            if (projectsArray) {
                updatedProjectName = handleUniqueName(projectsArray, updatedProjectName, projectName);
                setProjectName(updatedProjectName);
            }
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
                 style={{marginRight: (isProject > 0 && !isMobile) && 42}}
                 onClick={handleOpen}>
                <div className={styles.Card + " " + (theme === "dark" ? styles.MainDark : styles.MainLight)}>
                    <div className={styles.Button + " " + (theme === "dark" ? styles.ButtonDark : styles.ButtonLight)}>
                        <div className={styles.AddIconImage}>
                            <div className={styles.plus}>+</div>
                        </div>
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
                            <img alt="" src={Images.lightCrossIcon} className={styles.CrossIcon} onClick={handleClose}/> :
                            <img alt="" src={Images.darkCrossIcon} className={styles.CrossIcon}
                                 onClick={handleClose}/>)}
                    </div>
                    <div
                        className={styles.line + " " + (theme === "dark" ? styles.ModelHeadingDark : styles.ModelHeadingLight)}/>
                    <div className={styles.Input}>
                        <SimpleInputComponent inputType={"text"} extraStyle={styles.inputExtraStyle}
                                              inputTitle={"Give your project a name"}
                                              placeHolder={"Project Name"}
                                              extraInputStyle={styles.extraInputStyle}
                                              value={projectName} extraMargin={styles.inputBoxMargin}
                                              onDataChange={handleProjectNameChange}
                                              OpenNewProjectHandle={OpenNewProjectHandle}
                        />
                    </div>
                    {isMobile ?
                        <div className={styles.creatButton}>
                            <CreateButton OpenNewProjectHandle={OpenNewProjectHandle}/>
                        </div> :
                        <CreateButton OpenNewProjectHandle={OpenNewProjectHandle}/>
                    }
                </div>
            </Modal>
        </>

    );
}

export default NewProjectButton;


/**
 * handle project name :: return unique name
 * @param projectsArray
 * @param updatedProjectName
 * @param projectName
 * @returns {string|*}
 */
export function handleUniqueName(projectsArray, updatedProjectName, projectName) {
    const findProject = projectsArray.find(project => project.projectName === updatedProjectName)
    if (findProject) {
        const projectTitle = findProject.projectName
        const lastCharacter = projectTitle.charAt(projectTitle.length - 1);
        if (!isNaN(parseInt(lastCharacter))) {
            updatedProjectName = projectTitle.replace(/.$/, parseInt(lastCharacter) + 1);
        } else {
            updatedProjectName = projectTitle + "1";
        }
        let checkProject = projectsArray.find(project => project.projectName === updatedProjectName);
        if (checkProject) {
            return handleUniqueName(projectsArray, updatedProjectName, projectName)
        } else {
            return updatedProjectName;
        }

    } else {
        return projectName;
    }
}

function CreateButton(props) {
    const {OpenNewProjectHandle} = props
    return (
        <div className={styles.SaveBtn} onClick={() => {
            OpenNewProjectHandle();
        }}>Create
        </div>
    )
}