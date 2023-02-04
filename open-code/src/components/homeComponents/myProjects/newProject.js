import React, {useContext} from 'react';
import Triangle from "../../../assets/images/icon/triangle.png";
import DarkTriangle from "../../../assets/images/icon/dark-triangle.png";
import NewProjectButton from "./newProjectButton";
import {ThemeContext} from "../../../App";
import styles from "./newProject.module.css";

export const NewProject = () => {
    const {theme} = useContext(ThemeContext)
    return (
        <div className={styles.Main + " " + (theme === "dark" ? styles.MainDark : styles.MainLight)}>
            <div className={styles.Heading + " " + (theme === "dark" ? styles.MainDark : styles.MainLight)}>My
                Projects
            </div>
            <div className={styles.ButtonsMessage}>
                <NewProjectButton/>
                {<div className={styles.MessageIcon}>
                    <img alt="Triangle" className={styles.TriangleIcon}
                         src={(theme === "dark" ? DarkTriangle : Triangle)}/>
                    <div
                        className={styles.Message + " " + (theme === "dark" ? styles.MessageDark : styles.MessageLight)}>Click
                        to create a new project.
                    </div>
                </div>}
            </div>
        </div>
    );
}

