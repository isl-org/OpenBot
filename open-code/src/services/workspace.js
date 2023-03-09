import {auth, db} from "./firebase";
import Blockly from "blockly/core";
import {collection, deleteDoc, doc, getDocs, setDoc, updateDoc} from "firebase/firestore";
import {localStorageKeys} from "../utils/constants";

/**
 * project upload on drive when user signedIn.
 * @param data
 * @param uniqueId
 * @returns {Promise<void>}
 */
export async function uploadOnDrive(data, uniqueId) {
    if (localStorage.getItem("isSigIn") === "true") {
        try {
            const workspaceRef = doc(collection(db, auth.currentUser.uid), uniqueId);
            await setDoc(workspaceRef, data);
        } catch (err) {
            console.log(err);
        }
    } else {
        //alert for login first
    }
}

/**
 * get project from drive when user signedIn
 * @param driveProjects
 * @returns {Promise<void>}
 */
export async function getDriveProjects(driveProjects) {
    if (localStorage.getItem("isSigIn") === "true") {
        try {
            const projects = await getDocs(collection(db, auth.currentUser?.uid));
            projects.forEach((doc) => {
                driveProjects?.push({storage: "drive", id: doc.id, ...doc.data()});
            })
        } catch (error) {
            console.error(error);
        }
    }
}

export async function updateProjectOnDrive() {
    // const date = new Date();
    // const options = {day: 'numeric', month: 'long', year: 'numeric'};
    // const currentDate = date.toLocaleDateString('en-US', options);
    const workspaceRef = doc(collection(db, auth.currentUser.uid), getCurrentProject().id);
    try {
        await updateDoc(workspaceRef, {
            // date: currentDate,
            xmlValue: getCurrentProject().xmlValue
        })
    } catch (err) {
        console.log(err);
    }
}

/**
 * delete projects from local and firestore
 * @param currentProjectId
 * @returns {Promise<void>}
 */
export async function deletingCurrentProject(currentProjectId) {
    try {

        if (localStorage.getItem("isSigIn") === "true") {
            await deleteDoc(doc(db, auth.currentUser.uid, currentProjectId))

            JSON.parse(localStorage?.getItem(localStorageKeys.allProjects))?.find((project) => {
                if (project.id === currentProjectId) {
                    const restObject = getAllLocalProjects().filter((res) => (res.id !== project.id));
                    localStorage.setItem(localStorageKeys.allProjects, JSON.stringify(restObject))
                }
            })
        } else {
            JSON.parse(localStorage?.getItem(localStorageKeys.allProjects))?.find((project) => {
                if (project.id === currentProjectId) {
                    const restObject = getAllLocalProjects().filter((res) => (res.id !== project.id));
                    localStorage.setItem(localStorageKeys.allProjects, JSON.stringify(restObject))
                }
            })
        }
    } catch (err) {
        console.log(err);
    }
}

/**
 * create new project in current local storage and changes in current project save in allProjects local storage
 * @param uniqueId
 * @param projectName
 * @param code
 */
export function updateCurrentProject(uniqueId, projectName, code) {
    const date = new Date();
    const options = {day: 'numeric', month: 'long', year: 'numeric'};
    const currentDate = date.toLocaleDateString('en-US', options)
    const project = {
        storage: "local",
        id: uniqueId,
        projectName: projectName,
        xmlValue: code,
        date: currentDate,
    }
    localStorage.setItem(localStorageKeys.currentProject, JSON.stringify(project))
    const found = JSON.parse(localStorage?.getItem(localStorageKeys.allProjects))?.find((project) => {
        return project.id === getCurrentProject().id
    })
    if (!found) {
        saveProjectInLocal(localStorage.getItem(localStorageKeys.currentProject))
        if (localStorage.getItem("isSigIn") === "true") {
            const data = {
                projectName: getCurrentProject().projectName,
                xmlValue: getCurrentProject().xmlValue,
                date: getCurrentProject().date,
            }
            uploadOnDrive(data, getCurrentProject().id).then()
        }
    }
}

/**
 * get that project which is running currently in work space.
 * @returns {return project data in object}
 */
export function getCurrentProject() {
    try {
        const getProject = localStorage.getItem(localStorageKeys.currentProject)
        return JSON.parse(getProject)
    } catch (e) {
    }
}

/**
 * save new project in local storage
 * @param currentProject
 */
export function saveProjectInLocal(currentProject) {
    const objectCurrentProject = JSON.parse(currentProject)
    const getAllProjects = localStorage.getItem(localStorageKeys.allProjects)
    let ProjectsArray = JSON.parse(getAllProjects)
    if (ProjectsArray) {
        ProjectsArray.push(objectCurrentProject)
    } else {
        ProjectsArray = [objectCurrentProject]
    }
    localStorage.setItem(localStorageKeys.allProjects, JSON.stringify(ProjectsArray))
}

/**
 * get all saved projects from local storage.
 */
export function getAllLocalProjects() {
    try {
        const projects = localStorage.getItem(localStorageKeys.allProjects)
        return JSON.parse(projects)
    } catch (error) {

    }
}

/**
 * current project changes save in local storage.
 */
export function updateLocalProjects() {
    if (getCurrentProject()) {
        let allProjects = getAllLocalProjects()
        let index = allProjects?.findIndex(obj => obj.id === getCurrentProject().id)
        allProjects?.find((project) => {
            if (project.id === getCurrentProject().id) {
                allProjects.splice(index, 1, getCurrentProject())
            }
        })
        localStorage.setItem(localStorageKeys.allProjects, JSON.stringify(allProjects));
        localStorage.setItem(localStorageKeys.currentProject, "")
    }
}

/**
 * remove duplicate project get from drive and also save in local.
 */
export async function getFilterProjects() {
    let allProjects
    let filterProjects
    let allDriveProjects = [];
    let allLocalProjects = getAllLocalProjects()
    await getDriveProjects(allDriveProjects).then(() => {
        allProjects = allLocalProjects?.concat(allDriveProjects) || allDriveProjects
        const uniqueIds = {}; // object to keep track of unique id values
        filterProjects = allProjects.filter(project => {
            // check if id value has already been seen
            if (uniqueIds[project.id]) {
                return false;
            }

            // mark id value as seen
            uniqueIds[project.id] = true;

            // check if storage value is 'drive'
            if (project.storage === 'drive') {
                // check if id value is unique
                return allProjects.filter(o => o.id === project.id).length === 1;
            }
            return true;
        });
    })
    return filterProjects;
}