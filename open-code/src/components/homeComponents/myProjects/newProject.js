import React, {useContext, useEffect, useState} from 'react';
import Triangle from '../../../assets/images/icon/triangle.png';
import DarkTriangle from '../../../assets/images/icon/dark-triangle.png';
import {ThemeContext} from '../../../App';
import {
    getFilterProjects,
} from '../../../services/workspace';
import styles from './newProject.module.css';
import Card from './card';
import NewProjectButton from './newProjectButton';
import LoaderComponent from "../../loader/loaderComponent";
import {StoreContext} from "../../../context/context";


/**
 * Displays all projects with the option to create a new project
 * @returns {JSX.Element}
 * @constructor
 */
export const NewProject = () => {
        const [projects, setProjects] = useState(undefined);
        const {theme} = useContext(ThemeContext);
        const {setCode, isSignIn, setDrawer} = useContext(StoreContext);
        const [deleteLoader, setDeleteLoader] = useState(false);
        const [loader, setLoader] = useState(false);

        useEffect(() => {
            setProjects(undefined);
            setLoader(true);
            setCode(undefined);
            if (!deleteLoader) {
                setTimeout(() => {
                    getSortedData().then((res) => {
                        setProjects(res) //updating state for projects
                    })
                }, 0.00001)
                setLoader(false)
            }
        }, [deleteLoader, isSignIn, setCode]);

        //sorting the projects according to updated time
        const getSortedData = async () => {
            // Fetching projects from the API
            return getFilterProjects().then(async (filterProject) => {
                return await filterProject.sort((a, b) => {
                    return new Date(b.updatedTime) - new Date(a.updatedTime);
                })
            });
        }
        return (
            <div className={`${styles.Main} ${theme === 'dark' ? styles.MainDark : styles.MainLight}`}>
                <div className={`${styles.Heading} ${theme === 'dark' ? styles.MainDark : styles.MainLight}`}>
                    My Projects
                </div>
                {(loader || projects) && !deleteLoader ?
                    <div className={styles.ButtonsMessage}>

                        {/* Render the new project button if there are existing projects */}
                        <NewProjectButton isProject={projects?.length} setDrawer={setDrawer}/>

                        {/* Sort the projects based on the last updated date and time */}
                        {projects && projects?.length > 0 ?
                            projects?.map((project, key) => <Card key={key} projectData={project}
                                                                  setDeleteLoader={setDeleteLoader}/>)
                            :
                            // If there are no projects, render create new project component
                            <CreateNewProject/>
                        }

                    </div>
                    :
                    <div className={styles.loaderStyle}>
                        <LoaderComponent/>
                    </div>
                }
            </div>
        );
    }
;


/**
 * Renders an image with a message to create a new project
 * @returns {JSX.Element}
 * @constructor
 */
function CreateNewProject() {
    const {theme} = useContext(ThemeContext)
    return (
        <div className={styles.MessageIcon}>
            <img alt="Triangle" className={styles.TriangleIcon}
                 src={theme === 'dark' ? DarkTriangle : Triangle}/>
            <div className={`${styles.Message} ${theme === 'dark' ? styles.MessageDark : styles.MessageLight}`}>
                Click to create a new project.
            </div>
        </div>
    );
}
