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
    // let driveProjects = []
    if (localStorage.getItem("isSigIn") === "true") {
        try {
            const projects = await getDocs(collection(db, auth.currentUser?.uid));
            projects.forEach((doc) => {
                driveProjects?.push({id: doc.id, ...doc.data()});
            })
        } catch (error) {
            console.error(error);
        }
    }
}

export async function updatingWorkspace(projectName, currentProjectId) {
    const date = new Date();
    const options = {day: 'numeric', month: 'long', year: 'numeric'};
    const currentDate = date.toLocaleDateString('en-US', options);
    const xmlData = Blockly.Xml.domToText(Blockly.Xml.workspaceToDom(Blockly.getMainWorkspace()));
    const workspaceRef = doc(collection(db, auth.currentUser.uid), currentProjectId);
    try {
        await updateDoc(workspaceRef, {
            date: currentDate,
            xmlValue: xmlData
        })
    } catch (err) {
        console.log(err);
    }
}

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
export async function filterProjects() {
    let driveProjects = [];
    let allLocalProjects = getAllLocalProjects()
    await getDriveProjects(driveProjects)
    let allProjects = allLocalProjects?.concat(driveProjects)
    console.log(allProjects)
}