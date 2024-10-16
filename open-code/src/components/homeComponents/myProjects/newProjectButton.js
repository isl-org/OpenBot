import React, {useContext, useEffect, useState} from 'react';
import {useNavigate} from "react-router-dom";
import {Modal, useTheme} from "@mui/material";
import {StoreContext} from "../../../context/context";
import styles from "./newProject.module.css";
import {ThemeContext} from "../../../App";
import {Images} from "../../../utils/images";
import SimpleInputComponent from "../../inputComponent/simpleInputComponent";
import {localStorageKeys, PathName, Themes} from "../../../utils/constants";
import useMediaQuery from "@mui/material/useMediaQuery";


/**
 * This component represents a button that allows the user to create a new project.
 * @param {Object} props - The props object that contains the properties for this component.
 * @param {boolean} props.isProject - A boolean indicating whether or not there are any projects.
 * @returns {JSX.Element}
 * @constructor
 */
function NewProjectButton(props) {
    const {isProject, setDrawer} = props;
    let navigate = useNavigate();
    const [open, setOpen] = useState(false);
    const [isInputError, setIsInputError] = useState(true);
    const themes = useTheme();
    const isMobile = useMediaQuery(themes.breakpoints.down('sm'));
    const [isTabletQuery, setIsTabletQuery] = useState(window.matchMedia("(min-width: 768px) and (max-width: 1024px)").matches);
    const {theme} = useContext(ThemeContext)
    const {
        projectName,
        setProjectName,
        setCurrentProjectXml,
        setFileId,
    } = useContext(StoreContext)

    // Function to open the modal
    const handleOpen = () => {
        // Clear current project data from local storage and context
        localStorage.setItem(localStorageKeys.currentProject, "");
        setCurrentProjectXml("")
        setProjectName();
        setOpen(true);// open modal
        setFileId("")
    }

    // Function to close the modal
    const handleClose = () => setOpen(false);

    // Function to handle new project creation
    const OpenNewProjectHandle = () => {
        if (!projectName || projectName <= 0) {
            // If project name is not entered or is empty, set input error flag
            setIsInputError(true)
        } else {
            let updatedProjectName = projectName;
            const getAllProjects = localStorage.getItem(localStorageKeys.allProjects);
            let projectsArray = JSON.parse(getAllProjects);
            if (projectsArray) {
                // If there are already projects, check for unique name
                updatedProjectName = handleUniqueName(projectsArray, updatedProjectName, projectName);
                setProjectName(updatedProjectName);
            }
            setIsInputError(false);  // Clear input error flag
            // Navigate to playground page and close modal
            navigate(PathName.playGround);
            setDrawer(false);
            handleClose();
        }
    }

    //function to handle project name change
    function handleProjectNameChange(name) {
        setProjectName(name); // Function to handle project name input change
    }

    useEffect(() => {
        const handleOrientationChange = () => {
            setIsTabletQuery(window.matchMedia("(min-width: 768px) and (max-width: 1024px)").matches);
        };
        window.addEventListener("resize", handleOrientationChange);
    }, []);

    return (
        <>
            <div className={styles.Content + " " + (theme === "dark" ? styles.MainDark : styles.MainLight)}
                 style={{marginRight: isProject > 0 ? (!isMobile) && 42 : isTabletQuery ? "-50px" : 0}}
                 onClick={handleOpen}>
                {/*add new project icon*/}
                <div className={styles.Card + " " + (theme === "dark" ? styles.MainDark : styles.MainLight)}>
                    <div className={styles.Button + " " + (theme === "dark" ? styles.ButtonDark : styles.ButtonLight)}>
                        <div className={styles.AddIconImage}>
                            <div className={styles.plus}>+</div>
                        </div>
                    </div>
                </div>

            </div>
            {/*create new project modal*/}
            <CreateNewProjectModal
                open={open} handleProjectNameChange={handleProjectNameChange}
                OpenNewProjectHandle={OpenNewProjectHandle} isMobile={isMobile}
                handleClose={handleClose} projectName={projectName}/>
        </>

    );
}

export default NewProjectButton;


/**
 * Create new Project PopUp
 * @param params
 * @returns {JSX.Element}
 * @constructor
 */
function CreateNewProjectModal(params) {
    const {open, handleClose, projectName, handleProjectNameChange, OpenNewProjectHandle, isMobile} = params
    const {theme} = useContext(ThemeContext)
    const isAndroid = navigator.userAgent.toLowerCase().indexOf("android") > -1;
    const isIOS = /iPad|iPhone|iPod/.test(navigator.userAgent) && !window.MSStream;
    const isMobileLandscape = window.matchMedia("(max-height:440px) and (max-width: 1000px) and (orientation: landscape)").matches

    return (
        <Modal
            open={open}
            onClose={handleClose}
            className={"model"}
            style={{overflow: "scroll"}}
        >
            <div className={styles.model + " " + (theme === "dark" ? styles.modelDark : styles.modelLight)}>
                {/* The header section of the Modal*/}
                <div
                    className={styles.ModelHeading + " " + (theme === "dark" ? styles.ModelHeadingDark : styles.ModelHeadingLight)}>

                    {/* Title of the Modal*/}
                    <span>Create a New Project</span>
                    {/* Close button icon displayed depending on the selected theme*/}
                    {(theme === Themes.light ?
                        <img alt="cross" src={Images.lightCrossIcon} className={styles.CrossIcon}
                             onClick={handleClose}/> :
                        <img alt="cross" src={Images.darkCrossIcon} className={styles.CrossIcon}
                             onClick={handleClose}/>)}
                </div>
                {/* A line separating the header and content sections of the Modal*/}
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
                {/*The button to create a new project*/}
                {isMobile ?
                    <div
                        className={isMobileLandscape && isIOS ? styles.iosButton : isMobileLandscape && isAndroid ? styles.androidButton : styles.creatButton}>
                        <CreateButton OpenNewProjectHandle={OpenNewProjectHandle}/>
                    </div> :
                    <CreateButton OpenNewProjectHandle={OpenNewProjectHandle}/>
                }
            </div>
        </Modal>
    )
}


/**
 * handleUniqueName - Given an array of projects and a project name,
 * checks if the project name already exists in the array, and returns a unique
 * project name by appending a number to the end of the project name if needed.
 * @param {Array} projectsArray - An array of existing project objects
 * @param {string} updatedProjectName - The project name to check for uniqueness
 * @param {string} projectName - The original project name
 * @returns {string} - A unique project name
 */
export function handleUniqueName(projectsArray, updatedProjectName, projectName) {
    const findProject = projectsArray.find(project => project.projectName === updatedProjectName)

    // If the project name already exists in the array, append a number to make it unique
    if (findProject) {
        const projectTitle = findProject.projectName
        const lastCharacter = projectTitle.charAt(projectTitle.length - 1);
        if (!isNaN(parseInt(lastCharacter))) {
            updatedProjectName = projectTitle.replace(/.$/, parseInt(lastCharacter) + 1);
        } else {
            updatedProjectName = projectTitle + "1";
        }
        // Recursively check if the updated project name is already in use
        let checkProject = projectsArray.find(project => project.projectName === updatedProjectName);
        if (checkProject) {
            return handleUniqueName(projectsArray, updatedProjectName, projectName)
        } else {
            return updatedProjectName;
        }
    } else {
        // If the project name is unique, return the original project name
        return projectName;
    }
}


/**
 * Create icon
 * @param props
 * @returns {JSX.Element}
 * @constructor
 */
function CreateButton(props) {
    const {OpenNewProjectHandle} = props
    return (
        <div className={styles.SaveBtn} onClick={() => {
            OpenNewProjectHandle();
        }}>Create
        </div>
    )
}