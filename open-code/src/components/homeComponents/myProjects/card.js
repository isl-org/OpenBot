import React, {useContext} from 'react';
import styles from "./newProject.module.css"
import {Images} from "../../../utils/images";
import {ThemeContext} from "../../../App";
import BlackText from "../../fonts/blackText";
import WhiteText from "../../fonts/whiteText";
import {doc, getDoc} from "firebase/firestore";
import {auth, db} from "../../../services/firebase";
import {StoreContext} from "../../../context/context";
import {useNavigate} from "react-router-dom";
import {getAllLocalProjects} from "../../../services/workspace";
import {localStorageKeys} from "../../../utils/constants";

function Card(props) {
    const {theme} = useContext(ThemeContext);
    const {setCurrentProjectXml, setProjectName, setCurrentProjectId,} = useContext(StoreContext);
    let navigate = useNavigate();
    const openExistingProject = () => {
        navigate(`playground`);
    }

    /**
     * fetching blocks xml of selected project on home page
     * @param projectId
     * @returns {Promise<void>}
     */
    const handleOpenProject = async (projectId) => {
        localStorage.setItem(localStorageKeys.currentProject, "")
        setCurrentProjectId(projectId)
        const findCurrentProject = getAllLocalProjects().find(currentProject => currentProject.id === projectId)
        setCurrentProjectXml(findCurrentProject.xmlValue);
        setProjectName(findCurrentProject.projectName);
        openExistingProject();
        try {
            const blockSnap = doc(db, auth.currentUser.uid, projectId);
            const workspaceRef = await getDoc(blockSnap);
            if (workspaceRef.exists()) {
                const projectId = workspaceRef.id;
                setCurrentProjectId(projectId);
                const projectXmlData = workspaceRef.data().xmlValue;
                const projectName = workspaceRef.data().projectName;
                setCurrentProjectXml(projectXmlData);
                setProjectName(projectName);
                openExistingProject();
            }
        } catch (error) {
            console.error(error);
        }
    }

    return (
        <div className={styles.cardContent}>
            <div onClick={() => {
                handleOpenProject(props.projectId).catch((err) => {
                    console.log("error on fetching blocks: ", err);
                })
            }} className={` ${styles.Card} ${theme === "dark" ? styles.darkBoxShadow : styles.lightBoxShadow}`}>
                <div className={styles.CardHeadingIcon}>
                    {theme === "dark" ? <WhiteText extraStyle={styles.CardHeading} text={props.projectName}/> :
                        <BlackText extraStyle={styles.CardHeading} text={props.projectName}/>}
                    <img alt="pencil-icon" src={theme === "dark" ? Images.darkPencilIcon : Images.pencilIcon}
                         className={styles.PencilIcon}/>
                </div>
                <BlackText extraStyle={styles.Date} text={props.projectDate}/>
            </div>
        </div>
    );
}

export default Card;
