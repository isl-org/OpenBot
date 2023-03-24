import React, {useContext} from 'react';
import styles from "./newProject.module.css"
import {Images} from "../../../utils/images";
import {ThemeContext} from "../../../App";
import BlackText from "../../fonts/blackText";
import WhiteText from "../../fonts/whiteText";
import {StoreContext} from "../../../context/context";
import {useNavigate} from "react-router-dom";
import {getAllLocalProjects, getDriveProjects} from "../../../services/workspace";
import {localStorageKeys} from "../../../utils/constants";

function Card(props) {
    const {theme} = useContext(ThemeContext);
    const {setCurrentProjectXml, setProjectName, setCurrentProjectId, setFileId, setDrawer} = useContext(StoreContext);
    let navigate = useNavigate();
    const openExistingProject = () => {
        navigate(`playground`);
    }

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

    return (
        <div className={styles.cardContent}>
            <div onClick={() => {
                handleOpenProject(props.projectData).catch((err) => {
                    console.log(props.projectData)
                    console.log("error on fetching blocks: ", err);
                })
            }} className={` ${styles.Card} ${theme === "dark" ? styles.darkBoxShadow : styles.lightBoxShadow}`}>
                <div className={styles.CardHeadingIcon}>
                    {theme === "dark" ?
                        <WhiteText extraStyle={styles.CardHeading} text={props.projectData.projectName}/> :
                        <BlackText extraStyle={styles.CardHeading} text={props.projectData.projectName}/>}
                    <img alt="pencil-icon" src={theme === "dark" ? Images.darkPencilIcon : Images.pencilIcon}
                         className={styles.PencilIcon}/>
                </div>
                <BlackText divStyle={{marginTop: 5, marginBottom: 5}} extraStyle={styles.Date}
                           text={props.projectData.updatedDate}/>
                <BlackText extraStyle={styles.Date} text={props.projectData.time}/>
            </div>
        </div>
    );
}

export default Card;
