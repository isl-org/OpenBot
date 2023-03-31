import React, {useContext, useEffect, useRef, useState} from 'react';
import styles from "./newProject.module.css"
import style from "../../../components/navBar/navbar.module.css"
import {Images} from "../../../utils/images";
import {ThemeContext} from "../../../App";
import BlackText from "../../fonts/blackText";
import WhiteText from "../../fonts/whiteText";
import {StoreContext} from "../../../context/context";
import {useNavigate} from "react-router-dom";
import {getAllLocalProjects, getDriveProjects, renameProject} from "../../../services/workspace";
import {localStorageKeys, Themes} from "../../../utils/constants";
import {EditProjectPopUp} from "../header/headerComponents";
import {DeleteModel} from "../header/logOutAndDeleteModal";

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
    const open = Boolean(openPopUp);

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

    return (
        <div className={styles.cardContent}>
            {deleteProject && <DeleteModel setDeleteProject={setDeleteProject}/>}
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
                                         onClick={(e) => e.stopPropagation()}
                                         onFocus={(e) => e.target.select()}
                                         onBlur={async () => {
                                             setRename(false)
                                             await renameProject(reNameProject,props.projectData.projectName).then()
                                         }
                                            }
                                         onChange={(e) => setReNameProject(e.target.value)}
                                         onKeyDown={async (e) => {
                                             if (e.keyCode === 13) {
                                                 setRename(false)
                                                 await renameProject(reNameProject,props.projectData.projectName).then()
                                             }
                                         }}
                                         style={{width: `${props.projectData.projectName?.length}ch`}}
                                         value={reNameProject}
                        /> : theme === "dark" ?
                            <WhiteText extraStyle={styles.CardHeading} text={reNameProject}/> :
                            <BlackText extraStyle={styles.CardHeading} text={reNameProject}/>
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
                                      projectName={reNameProject}
                                      handleDelete={(e) => handleDelete(e)}
                                      theme={theme}
                                      extraStyle={styles.optionExtraStyle}
                    />
                </div>
                <BlackText divStyle={{marginTop: 5, marginBottom: 5}} extraStyle={styles.Date}
                           text={props.projectData.updatedDate}/>
                <BlackText extraStyle={styles.Date} text={props.projectData.time}/>
            </div>
        </div>
    );
}

export default Card;
