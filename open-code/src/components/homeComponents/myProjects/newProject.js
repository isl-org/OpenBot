import React, {useContext, useEffect, useState} from 'react';
import Triangle from "../../../assets/images/icon/triangle.png";
import DarkTriangle from "../../../assets/images/icon/dark-triangle.png";
import NewProjectButton from "./newProjectButton";
import {ThemeContext} from "../../../App";
import styles from "./newProject.module.css";
import {collection, getDocs} from "firebase/firestore";
import {auth, db} from "../../../services/firebase";
import Card from "./card";

export const NewProject = () => {
    const [allProject, setAllProject] = useState([]);

    async function loadingAllWorkspaces() {
        try {
            const allProjects = [];
            const projects = await getDocs(collection(db, auth.currentUser?.uid));
            projects.forEach((doc) => {
                allProjects.push({id: doc.id, ...doc.data()});
            })
            setAllProject(allProjects);
        } catch (error) {
            console.error(error);
        }
    }

    useEffect(() => {
        auth.onAuthStateChanged(function () {
            loadingAllWorkspaces().catch(err => {
            })
        })
    }, [])
    const {theme} = useContext(ThemeContext)
    return (
        <div className={styles.Main + " " + (theme === "dark" ? styles.MainDark : styles.MainLight)}>
            <div className={styles.Heading + " " + (theme === "dark" ? styles.MainDark : styles.MainLight)}>My
                Projects
            </div>
            <div className={styles.ButtonsMessage}>
                <NewProjectButton isProject={allProject.length}/>
                {allProject.length > 0 ? allProject?.map((project, value) => (
                        <Card key={value}
                              projectTitle={project.id}
                              projectDate={project.Date}
                        />
                    )) :
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

