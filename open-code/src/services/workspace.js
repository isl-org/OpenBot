import {localStorageKeys} from "../utils/constants";
import {
    checkFileExistsInFolder,
    deleteFileFromGoogleDrive,
    getAllFilesFromGoogleDrive,
    getFolderId,
    fileRename,
} from "./googleDrive";


/**
 * get project from drive when user signedIn
 * @param driveProjects
 * @returns {Promise<void>}
 */
export async function getDriveProjects(driveProjects) {

    if (localStorage.getItem("isSigIn") === "true") {
        try {
            //getting allDocs from Google Drive
            let allFilesFromGoogleDrive = await getAllFilesFromGoogleDrive();
            //check if there is any project or not if then in driveProject push only required data.
            console.log("allFilesFromGoogleDrive", allFilesFromGoogleDrive)
            allFilesFromGoogleDrive.length > 0 && allFilesFromGoogleDrive.forEach((doc) => {
                //check project is not deleted
                if (!doc?.trashed) {
                    driveProjects?.push({
                        storage: "drive",
                        xmlValue: doc.xmlValue,
                        id: doc.appProperties.id,
                        projectName: doc.name.replace(/\.[^/.]+$/, ""),
                        updatedDate: doc.appProperties.date,
                        time: doc.appProperties.time,
                    });
                }
            })
            return driveProjects
        } catch (error) {
            console.error(error);
        }
    }
}


/**
 * delete project from local and drive also if you are signedIn.
 * @returns {Promise<void>}
 * @param projectName
 */
export async function deleteProject(projectName) {

    try {
        if (localStorage.getItem("isSigIn") === "true") {
            //deleting file from Google Drive
            const allProject = []
            JSON.parse(localStorage?.getItem(localStorageKeys.allProjects))?.find((project) => {
                if (project.projectName === projectName) {
                    const restObject = getAllLocalProjects().filter((res) => (res.projectName !== project.projectName));
                    localStorage.setItem(localStorageKeys.allProjects, JSON.stringify(restObject))
                }
            })
            await getDriveProjects(allProject);
            const findCurrentProject = allProject.find(currentProject => currentProject?.projectName === projectName);
            console.log("findCurrentProject", findCurrentProject)
            findCurrentProject && await checkFileExistsInFolder(await getFolderId(), findCurrentProject?.projectName, "js").then(async (response) => {
                await deleteFileFromGoogleDrive(response?.fileId)
            })
            findCurrentProject && await checkFileExistsInFolder(await getFolderId(), findCurrentProject?.projectName, "xml").then(async (response) => {
                await deleteFileFromGoogleDrive(response?.fileId)
            })


        } else {
            //delete file from localProject
            JSON.parse(localStorage?.getItem(localStorageKeys.allProjects))?.find((project) => {
                if (project.projectName === projectName) {
                    const restObject = getAllLocalProjects().filter((res) => (res.projectName !== project.projectName));
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

    const project = {
        storage: "local",
        id: uniqueId,
        projectName: projectName,
        xmlValue: code,
        updatedDate: FormatDate().currentDate,
        time: FormatDate().currentTime,
    }
    //current project will first get store in current
    localStorage.setItem(localStorageKeys.currentProject, JSON.stringify(project))
    //now will check current project is in all project or not.
    const found = JSON.parse(localStorage?.getItem(localStorageKeys.allProjects))?.find((project) => {
        return project.id === getCurrentProject().id
    })
    //current project is not in local so will save it in local
    if (!found) {
        //add current project in local
        createProjectInLocal(localStorage.getItem(localStorageKeys.currentProject))

        // if (localStorage.getItem("isSigIn") === "true") {
        //     let data = {
        //         id: getCurrentProject().id,
        //         projectName: getCurrentProject().projectName,
        //         xmlValue: getCurrentProject().xmlValue,
        //         createdDate: FormatDate().currentDate,
        //         updatedDate: FormatDate().currentDate,
        //         time: FormatDate().currentTime,
        //     }
        //     //after that add to google Drive
        //     uploadToGoogleDrive(data).then();
        //     // uploadOnDrive(data, getCurrentProject().id).then() // firebase upload
        // }
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
    const objectCurrentProject = JSON.parse(currentProject)
    const getAllProjects = localStorage.getItem(localStorageKeys.allProjects)
    let projectsArray = JSON.parse(getAllProjects) || []
    projectsArray?.push({createdDate: FormatDate().currentDate, ...objectCurrentProject})
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
        console.log("allDriveProjects", allDriveProjects)
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

export function FormatDate() {
    const date = new Date();
    const dateOptions = {day: 'numeric', month: 'long', year: 'numeric'};
    const currentDate = date.toLocaleDateString('en-US', dateOptions)
    const timeOptions = {hour: 'numeric', minute: 'numeric', hour12: false};
    const currentTime = date.toLocaleTimeString('en-US', timeOptions);
    return {currentDate: currentDate, currentTime: currentTime}
}


export async function renameProject(projectName) {
    if (localStorage.getItem("isSigIn") === "true") {
        if (projectName !== getCurrentProject().projectName) {
            const data = {
                projectName: projectName,
                xmlValue: getCurrentProject().xmlValue,
                time: getCurrentProject().time,
                id: getCurrentProject().id,
                fileId: getCurrentProject().fileId,// require to check if already exist in folder or not
                createdDate: FormatDate().currentDate // Todo on create button add newly created date and time
            }
            console.log("current project", getCurrentProject().projectName)
            await fileRename(projectName, getCurrentProject().projectName, "xml").then(() => {
                    fileRename(projectName, getCurrentProject().projectName, "js").then();
                    updateCurrentProject(data.id, projectName, data.xmlValue)
                }
            )

        } else {
            //TODO file already exists checker
        }
    } else {
        const data = {
            projectName: projectName,
            xmlValue: getCurrentProject().xmlValue,
            time: getCurrentProject().time,
            id: getCurrentProject().id,
            fileId: getCurrentProject().fileId,// require to check if already exist in folder or not
            createdDate: FormatDate().currentDate // Todo on create button add newly created date and time
        }
        updateCurrentProject(data.id, projectName, data.xmlValue)
        console.log("project with same name in local::::", data)
    }
}