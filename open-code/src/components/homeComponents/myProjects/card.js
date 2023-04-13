import React, {useContext, useEffect, useRef, useState} from 'react';
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
    getDriveProjects,
    renameProject
} from "../../../services/workspace";
import {localStorageKeys, PathName, Themes} from "../../../utils/constants";
import {EditProjectPopUp} from "../header/headerComponents";
import {PopUpModal} from "../header/logOutAndDeleteModal";
import {handleUniqueName} from "./newProjectButton";
import useMediaQuery from "@mui/material/useMediaQuery";
import {useTheme} from "@mui/material";

function Card(props) {
    const {theme} = useContext(ThemeContext);
    const {
        setCurrentProjectXml,
        setProjectName,
        setCurrentProjectId,
        setFileId,
        setDrawer,
    } = useContext(StoreContext);
    const [openPopUp, setOpenPopUp] = useState(false);
    const [rename, setRename] = useState(false);
    const [anchorEl, setAnchorEl] = useState(null);
    const [reNameProject, setReNameProject] = useState("")
    const [deleteProject, setDeleteProject] = useState(false);
    const inputRef = useRef(null);
    const themes = useTheme();
    const isMobile = useMediaQuery(themes.breakpoints.down('md'));

    useEffect(() => {
        setReNameProject(props.projectData.projectName)
    }, [])

    let navigate = useNavigate();
    const openExistingProject = () => {
        navigate(`playground`);
    }
    const handleClick = (event) => {
        console.log("event::;", event)
        setAnchorEl(event.currentTarget);
        setOpenPopUp(!openPopUp);
    };

    /**
     * fetching blocks xml of selected project on home page
     * @param projectId
     * @returns {Promise<void>}
     */
    const handleOpenProject = async (projectData) => {
        localStorage.setItem(localStorageKeys.currentProject, "");
        setCurrentProjectId(projectData.id);
        setDrawer(false);
        try {
            if (projectData.storage === "drive") {
                //selected project from Google Drive
                const allProject = []
                await getDriveProjects(allProject);
                const findCurrentProject = allProject.find(currentProject => currentProject.id === projectData.id);
                setCurrentProjectXml(findCurrentProject.xmlValue);
                setProjectName(findCurrentProject.projectName);
                openExistingProject();
            } else {
                const findCurrentProject = getAllLocalProjects().find(currentProject => currentProject.id === projectData.id)
                setCurrentProjectXml(findCurrentProject.xmlValue);
                setProjectName(findCurrentProject.projectName);
                setFileId(findCurrentProject.fileId);
                openExistingProject();
            }

        } catch (error) {
            console.error(error);
        }
    }
    const handleDelete = (event) => {
        event.stopPropagation();
        setDeleteProject(true)
    }
    const handleClickOutside = () => {
        setOpenPopUp(false);
    }
    const handleClickBlur = async () => {
        if (!reNameProject || reNameProject <= 0) {
            setReNameProject(props.projectData.projectName)
        }
        setRename(false)
        if (reNameProject !== props.projectData.projectName) {
            await handleRename(reNameProject, props.projectData.projectName, setReNameProject).then(async (updatedProjectName) => {
                await renameProject(updatedProjectName, props.projectData.projectName, PathName.home).then()
            });
        }
    }


    const handleDeleteProject = () => {
        deleteProjectFromStorage(props.projectData.projectName).then(() => {
            let path = `/`;
            navigate(path);
        });
    }

    return (
        <div className={styles.cardContent}>
            {deleteProject && <PopUpModal setVariable={setDeleteProject}
                                          headerText={"Delete this file?"}
                                          containText={"You cannot restore this file later."}
                                          buttonText={"Delete"}
                                          handleButtonClick={handleDeleteProject}/>
            }
            <div onClick={() => {
                handleOpenProject(props.projectData).catch((err) => {
                    console.log(props.projectData)
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
                                             console.log("reName::", reNameProject)
                                             await handleClickBlur()
                                         }}
                                         onChange={(e) => setReNameProject(e.target.value)}
                                         onKeyDown={async (e) => {
                                             if (e.keyCode === 13) {
                                                 await handleClickBlur()
                                             }
                                         }}
                                         style={{width: `${props.projectData.projectName?.length * 1.5}ch`}}
                                         value={reNameProject}
                        /> : theme === "dark" ?
                            <WhiteText extraStyle={styles.CardHeading}
                                       text={reNameProject.slice(0, isMobile ? 8 : 10) + " " + ((reNameProject.length > (isMobile ? 8 : 10)) ? "..." : "")}/> :
                            <BlackText extraStyle={styles.CardHeading}
                                       text={reNameProject.slice(0, isMobile ? 8 : 10) + " " + ((reNameProject.length > (isMobile ? 8 : 10)) ? "..." : "")}/>
                        }
                    </div>
                    <img alt="pencil-icon" src={theme === Themes.dark ? Images.darkDots : Images.dots}
                         className={styles.PencilIcon} onClick={(event) => {
                        event.stopPropagation(); // prevent click event from propagating to parent element
                        handleClick(event);
                    }}/>
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
                           text={props.projectData.updatedDate}/>
                <BlackText extraStyle={styles.Date} text={props.projectData.time}/>
            </div>
        </div>
    );
}

export default Card;

export const handleRename = async (reNameProject, projectName, setReNameProject) => {
    let updatedProjectName = reNameProject;
    if (reNameProject !== projectName) {
        console.log("rename:::", reNameProject)
        const projectWithSameName = getAllLocalProjects()?.find((project) => project.projectName === reNameProject)
        console.log("pr:::", projectWithSameName)
        if (reNameProject === projectWithSameName?.projectName) {
            let projectsArray = getAllLocalProjects();
            if (projectsArray) {
                updatedProjectName = handleUniqueName(projectsArray, updatedProjectName, reNameProject);
            }
        }
        setReNameProject(updatedProjectName)
    }
    return updatedProjectName
}