import React, {useContext, useEffect, useState} from 'react';
import Triangle from "../../../assets/images/icon/triangle.png";
import DarkTriangle from "../../../assets/images/icon/dark-triangle.png";
import NewProjectButton from "./newProjectButton";
import {ThemeContext} from "../../../App";
import styles from "./newProject.module.css";
import {collection, getDocs} from "firebase/firestore";
import {auth, db} from "../../../services/firebase";
import Card from "./card";
import {getAllLocalProjects} from "../../../services/workspace";

export const NewProject = () => {
    const [driveProjects, setDriveProjects] = useState([]);
    const localStorageProjects = getAllLocalProjects()

    async function loadingAllWorkspaces() {
        try {
            const allProjects = [];
            const projects = await getDocs(collection(db, auth.currentUser?.uid));
            projects.forEach((doc) => {
                allProjects.push({id: doc.id, ...doc.data()});
            })
            setDriveProjects(allProjects);
        } catch (error) {
            console.error(error);
        }
    }

    useEffect(() => {
        auth.onAuthStateChanged(async function () {
            await loadingAllWorkspaces()
        })
    }, [])
    const {theme} = useContext(ThemeContext)
    const localStorageProjectsLength = localStorageProjects?.length ? localStorageProjects?.length : 0
    return (
        <div className={styles.Main + " " + (theme === "dark" ? styles.MainDark : styles.MainLight)}>
            <div className={styles.Heading + " " + (theme === "dark" ? styles.MainDark : styles.MainLight)}>My
                Projects
            </div>
            <div className={styles.ButtonsMessage}>
                <NewProjectButton isProject={driveProjects.length + localStorageProjectsLength}/>
                {
                    localStorageProjects?.map((localProjects, key) => (
                        <Card key={key}
                              projectName={localProjects.projectName}
                              projectDate={localProjects.date}
                              projectId={localProjects.id}
                        />
                    ))
                }
                {driveProjects.length > 0 || localStorageProjectsLength > 0 ? driveProjects?.map((driveProject, key) => (
                        <Card key={key}
                              projectName={driveProject.projectName}
                              projectDate={driveProject.date}
                              projectId={driveProject.id}
                        />
                    ))
                    :
                    <div className={styles.MessageIcon}>
                        <img alt="Triangle" className={styles.TriangleIcon}
                             src={(theme === "dark" ? DarkTriangle : Triangle)}/>
                        <div
                            className={styles.Message + " " + (theme === "dark" ? styles.MessageDark : styles.MessageLight)}>
                            Click to create a new project.
                        </div>
                    </div>
                }
            </div>
        </div>
    );
}

