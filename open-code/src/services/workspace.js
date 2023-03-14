import {auth, db} from "./firebase";
import {collection, deleteDoc, doc, getDocs, setDoc, updateDoc} from "firebase/firestore";
import {localStorageKeys} from "../utils/constants";
import {getFolderId} from "./googleDrive";

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
    if (auth.currentUser?.uid) {
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

/**
 * update project on drive when change on blockly workspace
 * @returns {Promise<void>}
 */
export async function updateProjectOnDrive() {
    const date = new Date();
    const dateOptions = {day: 'numeric', month: 'long', year: 'numeric'};
    const currentDate = date.toLocaleDateString('en-US', dateOptions);
    const timeOptions = {hour: 'numeric', minute: 'numeric', hour12: false};
    const currentTime = date.toLocaleTimeString('en-US', timeOptions);
    const workspaceRef = doc(collection(db, auth.currentUser.uid), getCurrentProject().id);
    try {
        await updateDoc(workspaceRef, {
            updatedDate: currentDate,
            xmlValue: getCurrentProject().xmlValue,
            time: currentTime,
        })
    } catch (err) {
        console.log(err);
    }
}

/**
 * delete project from local and drive also if you are signedIn.
 * @param currentProjectId
 * @returns {Promise<void>}
 */
export async function deleteProject(currentProjectId) {
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
export function updateCurrentProject(uniqueId, projectName, code, fileId) {

    const date = new Date();
    const dateOptions = {day: 'numeric', month: 'long', year: 'numeric'};
    const currentDate = date.toLocaleDateString('en-US', dateOptions)
    const timeOptions = {hour: 'numeric', minute: 'numeric', hour12: false};
    const currentTime = date.toLocaleTimeString('en-US', timeOptions);


    if (getCurrentProject()?.fileId || fileId) {
        const project = {
            storage: "local",
            id: uniqueId,
            projectName: projectName,
            xmlValue: code,
            updatedDate: currentDate,
            time: currentTime,
            fileId: getCurrentProject()?.fileId ?? fileId,
        }
        // if(getCurrentProject()?.fileId)
        localStorage.setItem(localStorageKeys.currentProject, JSON.stringify(project))
    } else {
        const project = {
            storage: "local",
            id: uniqueId,
            projectName: projectName,
            xmlValue: code,
            updatedDate: currentDate,
            time: currentTime,
        }
        localStorage.setItem(localStorageKeys.currentProject, JSON.stringify(project))
    }

    const found = JSON.parse(localStorage?.getItem(localStorageKeys.allProjects))?.find((project) => {
        return project.id === getCurrentProject().id
    })

    if (!found) {
        createProjectInLocal(localStorage.getItem(localStorageKeys.currentProject))
        if (localStorage.getItem("isSigIn") === "true") {
            const data = {
                projectName: getCurrentProject().projectName,
                xmlValue: getCurrentProject().xmlValue,
                createdDate: currentDate,
                updatedDate: currentDate,
                time: currentTime,
                folderId: getFolderId(),
                fileId: getCurrentProject().fileId,
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
 * create new project in local storage
 * @param currentProject
 */
export function createProjectInLocal(currentProject) {
    const date = new Date();
    const dateOptions = {day: 'numeric', month: 'long', year: 'numeric'};
    const currentDate = date.toLocaleDateString('en-US', dateOptions)
    const objectCurrentProject = JSON.parse(currentProject)
    const getAllProjects = localStorage.getItem(localStorageKeys.allProjects)
    let projectsArray = JSON.parse(getAllProjects)
    if (projectsArray) {
        projectsArray.push({createdDate: currentDate, ...objectCurrentProject})
    } else {
        projectsArray = [{createdDate: currentDate, ...objectCurrentProject}]
    }
    localStorage.setItem(localStorageKeys.allProjects, JSON.stringify(projectsArray))
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
        // localStorage.setItem(localStorageKeys.currentProject, "")
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

