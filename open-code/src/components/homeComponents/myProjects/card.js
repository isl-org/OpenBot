import React, {useContext, useRef, useState} from 'react';
import styles from "./newProject.module.css"
import style from "../../../components/navBar/navbar.module.css"
import {Images} from "../../../utils/images";
import {ThemeContext} from "../../../App";
import BlackText from "../../fonts/blackText";
import WhiteText from "../../fonts/whiteText";
import {StoreContext} from "../../../context/context";
import {useNavigate} from "react-router-dom";
import {
    deleteProjectFromStorage,
    getAllLocalProjects,
    getCurrentProject,
    renameProject
} from "../../../services/workspace";
import {localStorageKeys, PathName, Themes} from "../../../utils/constants";
import {EditProjectPopUp} from "../header/headerComponents";
import {PopUpModal} from "../header/logOutAndDeleteModal";
import {handleUniqueName} from "./newProjectButton";
import useMediaQuery from "@mui/material/useMediaQuery";
import {useTheme} from "@mui/material";


/**
 * Card
 * @param props
 * @returns {JSX.Element}
 * @constructor
 */
function Card(props) {
    const {theme} = useContext(ThemeContext);
    const {
        setCurrentProjectXml,
        setProjectName,
        setFileId,
        setDrawer,
        setIsError,
    } = useContext(StoreContext);
    const {projectData, setDeleteLoader,} = props
    const [openPopUp, setOpenPopUp] = useState(false);
    const [deleteProject, setDeleteProject] = useState(false);
    const [rename, setRename] = useState(false);
    const [anchorEl, setAnchorEl] = useState(null);
    const [reNameProject, setReNameProject] = useState(null)
    const inputRef = useRef(null);
    const themes = useTheme();
    const isMobile = useMediaQuery(themes.breakpoints.down('md'));
    let navigate = useNavigate();

    //function to open existing project
    const openExistingProject = () => {
        navigate(`playground`);
    }

    //function to handle click event
    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
        setOpenPopUp(!openPopUp);
    };

    /**
     * fetching blocks xml of selected project on home page
     * @param projectData - data of the project to be opened
     * @returns {Promise<void>}
     */
    const handleOpenProject = async (projectData) => {
        if (!(projectData.projectName === getCurrentProject()?.projectName)) {
            //current project will first get store in current
            localStorage.setItem(localStorageKeys.currentProject, "")
        }
        setDrawer(false);
        setIsError(false);
        try {
            setCurrentProjectXml(projectData.xmlValue);
            setProjectName(projectData.projectName);
            setFileId(projectData.fileId);
            openExistingProject();
        } catch (error) {
            console.error(error);
        }
    }

    // Handle click event for deleting a project
    const handleDelete = (event) => {
        event.stopPropagation();
        setDeleteProject(true)
    }

    // Handle click event for closing the pop-up menu
    const handleClickOutside = () => {
        setOpenPopUp(false);
    }

    // Handle click event for renaming a project
    const handleClickBlur = async () => {
        if (!reNameProject || reNameProject.trim().length <= 0 || reNameProject <= 0) {
            setReNameProject(projectData.projectName)
        }
        setRename(false)
        setDeleteLoader(true);
        if (reNameProject !== projectData?.projectName && !reNameProject?.trim().length <= 0) {
            await handleRename(reNameProject, projectData.projectName, setReNameProject).then(async (updatedProjectName) => {
                await renameProject(updatedProjectName, projectData.projectName, PathName.home).then(() => {
                        setDeleteLoader(false);
                    }
                )
            });
        }
        setDeleteLoader(false);
    }

    /**
     * function to handle deleting the project
     */
    const handleDeleteProject = () => {
        setDeleteLoader(true);
        deleteProjectFromStorage(projectData.projectName).then((res) => {
            setTimeout(() => {
                setDeleteLoader(false);
            }, 1000)
        });

    }

    return (
        <div className={styles.cardContent}>
            {deleteProject && <PopUpModal setVariable={setDeleteProject}
                                          inlineStyle={{backgroundColor: "#E03E1A"}}
                                          headerText={"Delete this file?"}
                                          containText={"You cannot restore this file later."}
                                          buttonText={"Delete"}
                                          handleButtonClick={handleDeleteProject}/>
            }
            <div onClick={() => {
                handleOpenProject(projectData).catch((err) => {
                    console.log("error on fetching blocks: ", err);
                })
            }} className={` ${styles.Card} ${theme === "dark" ? styles.darkBoxShadow : styles.lightBoxShadow}`}>
                <div className={styles.rowDiv}>
                    <div className={styles.CardHeadingIcon}>
                        {rename ? <input type="text" className={style.Edit}
                                         id="userEdit"
                                         ref={inputRef} // set the ref to the input element
                                         onClick={(e) => e.stopPropagation()}
                                         onFocus={(e) => e.target.select()}
                                         onBlur={async () => {
                                             await handleClickBlur()
                                         }}
                                         onChange={(e) => setReNameProject(e.target.value)}
                                         onKeyDown={async (e) => {
                                             if (e.keyCode === 13) {
                                                 await handleClickBlur()
                                             }
                                         }}
                                         style={{width: `${projectData.projectName?.length * 1.5}ch`}}
                                         value={reNameProject ?? projectData.projectName}
                        /> : theme === "dark" ?
                            <WhiteText extraStyle={styles.CardHeading}
                                       text={projectData?.projectName?.slice(0, isMobile ? 8 : 10) + " " + ((projectData?.projectName?.length > (isMobile ? 8 : 10)) ? "..." : "")}/> :
                            <BlackText extraStyle={styles.CardHeading}
                                       text={projectData?.projectName?.slice(0, isMobile ? 8 : 10) + " " + ((projectData?.projectName?.length > (isMobile ? 8 : 10)) ? "..." : "")}/>
                        }
                    </div>
                    <div>
                        <img alt="pencil-icon" src={theme === Themes.dark ? Images.darkDots : Images.dots}
                             className={styles.PencilIcon}
                             onClick={(event) => {
                                 event.stopPropagation(); // prevent click event from propagating to parent element
                                 handleClick(event);
                             }}
                        />
                    </div>
                    <EditProjectPopUp open={openPopUp} anchorEl={anchorEl}
                                      setOpen={setOpenPopUp}
                                      setRename={setRename}
                                      inputRef={inputRef}
                                      clickOutside={handleClickOutside}
                                      projectName={reNameProject}
                                      handleRename={() => {
                                          setOpenPopUp(false)
                                      }}
                                      handleDelete={(e) => handleDelete(e)}
                                      theme={theme}
                                      extraStyle={styles.optionExtraStyle}
                    />
                </div>
                <BlackText divStyle={{marginTop: (!isMobile && 4), marginBottom: (!isMobile && 5)}}
                           extraStyle={styles.Date}
                           text={projectData.updatedDate}/>
                <BlackText extraStyle={styles.Date} text={projectData.time}/>
            </div>
        </div>
    )
        ;
}
export default Card;


/**
 * Renames a project with the given new name, checking for duplicates and ensuring a unique name if necessary.
 * @param reNameProject
 * @param projectName
 * @param setReNameProject
 * @returns {Promise<string>}
 */
export const handleRename = async (reNameProject, projectName, setReNameProject) => {
    let updatedProjectName = reNameProject;
    if (reNameProject !== projectName) {

        // Check if there is already a project with the new name.
        const projectWithSameName = getAllLocalProjects()?.find((project) => project.projectName === reNameProject)

        if (reNameProject === projectWithSameName?.projectName) {
            let projectsArray = getAllLocalProjects();
            if (projectsArray) {
                // If there is a project with the same name, update the new name to ensure uniqueness.
                updatedProjectName = handleUniqueName(projectsArray, updatedProjectName, reNameProject);
            }
        }
        setReNameProject(updatedProjectName)
    }
    return updatedProjectName
}