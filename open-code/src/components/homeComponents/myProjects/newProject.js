import React, {useContext, useEffect, useState} from 'react';
import moment from 'moment';
import Triangle from '../../../assets/images/icon/triangle.png';
import DarkTriangle from '../../../assets/images/icon/dark-triangle.png';
import {ThemeContext} from '../../../App';
import {getFilterProjects} from '../../../services/workspace';
import styles from './newProject.module.css';
import Card from './card';
import NewProjectButton from './newProjectButton';
import LoaderComponent from "../../loader/loaderComponent";

/**
 * Displays all projects with the option to create a new project
 * @returns {JSX.Element}
 * @constructor
 */
export const NewProject = () => {
    const [projects, setProjects] = useState(undefined);
    const {theme} = useContext(ThemeContext);

    useEffect(() => {
        // Fetch projects from the API and update state
        getFilterProjects().then((filterProject) => {
            setProjects(filterProject);
        });
    }, []);

    return (
        <div className={`${styles.Main} ${theme === 'dark' ? styles.MainDark : styles.MainLight}`}>
            <div className={`${styles.Heading} ${theme === 'dark' ? styles.MainDark : styles.MainLight}`}>
                My Projects
            </div>
            {projects ?
                <div className={styles.ButtonsMessage}>

                    {/* Render the new project button if there are existing projects */}
                    <NewProjectButton isProject={projects?.length}/>
                    {/* Sort the projects based on the last updated date and time */}
                    {projects?.length > 0 ? (
                        projects?.sort(
                            (z, a) =>
                                moment(`${a.updatedDate} ${a.time}`, 'MMMM D, YYYY h:mm').valueOf() -
                                moment(`${z.updatedDate} ${z.time}`, 'MMMM D, YYYY h:mm').valueOf()
                        )
                            .map((project, key) => <Card key={key} projectData={project}/>)
                    ) : (
                        // If there are no projects, render create new project component
                        <CreateNewProject/>
                    )}

                </div>
                :
                <div className={styles.loaderStyle}>
                    <LoaderComponent/>
                </div>

            }

        </div>
    );
};


/**
 * Renders an image with a message to create a new project
 * @returns {JSX.Element}
 * @constructor
 */
function CreateNewProject() {
    const {theme} = useContext(ThemeContext)
    return (
        <div className={styles.MessageIcon}>
            <img alt="Triangle" className={styles.TriangleIcon} src={theme === 'dark' ? DarkTriangle : Triangle}/>
            <div className={`${styles.Message} ${theme === 'dark' ? styles.MessageDark : styles.MessageLight}`}>
                Click to create a new project.
            </div>
        </div>
    );
}
