import React, {useContext, useEffect, useState} from 'react';
import Triangle from "../../../assets/images/icon/triangle.png";
import DarkTriangle from "../../../assets/images/icon/dark-triangle.png";
import NewProjectButton from "./newProjectButton";
import {ThemeContext} from "../../../App";
import styles from "./newProject.module.css";
import Card from "./card";
import {getFilterProjects} from "../../../services/workspace";
import moment from 'moment';

/**
 *  show all projects with create new project option
 * @returns {JSX.Element}
 * @constructor
 */
export const NewProject = () => {
    const [projects, setProjects] = useState([]);
    const {theme} = useContext(ThemeContext)

    //TODO get projects from drive and store in local storage when signIn
    useEffect(() => {
        getFilterProjects().then((filterProject) => {
            setProjects(filterProject)
        })
    }, [])

    return (
        <div className={styles.Main + " " + (theme === "dark" ? styles.MainDark : styles.MainLight)}>
            <div className={styles.Heading + " " + (theme === "dark" ? styles.MainDark : styles.MainLight)}>My Projects
            </div>
            <div className={styles.ButtonsMessage}>
                <NewProjectButton isProject={projects.length}/>
                {projects.length > 0 ?
                    projects?.sort((z, a) => moment(`${a.updatedDate} ${a.time}`, 'MMMM D, YYYY h:mm')
                        .valueOf() - moment(`${z.updatedDate} ${z.time}`, 'MMMM D, YYYY h:mm')
                        .valueOf()).map((project, key) => (
                        <Card key={key}
                              projectData={project}
                        />
                    ))
                    :
                    <CreateNewProject/>
                }
            </div>
        </div>
    );
}


/**
 * Create New Project
 * @returns {JSX.Element}
 * @constructor
 */
function CreateNewProject() {
    const {theme} = useContext(ThemeContext)
    return (
        <div className={styles.MessageIcon}>
            <img alt="Triangle" className={styles.TriangleIcon}
                 src={(theme === "dark" ? DarkTriangle : Triangle)}/>
            <div
                className={styles.Message + " " + (theme === "dark" ? styles.MessageDark : styles.MessageLight)}>
                Click to create a new project.
            </div>
        </div>
    )
}

