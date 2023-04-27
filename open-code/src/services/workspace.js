import {localStorageKeys, PathName} from "../utils/constants";
import {
    checkFileExistsInFolder, deleteFileFromGoogleDrive, getAllFilesFromGoogleDrive, getFolderId, fileRename,
} from "./googleDrive";
import moment from "moment/moment";


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
            allFilesFromGoogleDrive.length > 0 && allFilesFromGoogleDrive.forEach((doc) => {
                //check project is not deleted
                if (!doc?.trashed) {
                    driveProjects?.push({
                        storage: "drive",
                        xmlValue: doc.xmlValue,
                        id: doc.appProperties.id,
                        projectName: doc.name.replace(/\.[^/.]+$/, ""),
                        updatedDate: doc.appProperties.date,
                        updatedTime: doc.appProperties.updatedTime,
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
export async function deleteProjectFromStorage(projectName) {

    try {

        //delete file from localProject so find project from local first then update the all projects by uploading project list.
        JSON.parse(localStorage?.getItem(localStorageKeys.allProjects))?.find((project) => {

            if (project.projectName === projectName) {
                //restProject contains all the projects except deleted project
                const restProjects = getAllLocalProjects().filter((res) => (res.projectName !== project.projectName));

                //set restProjects as all project in local
                localStorage.setItem(localStorageKeys.allProjects, JSON.stringify(restProjects))
            }

            return null;
        })
        //delete file from Google Drive
        if (localStorage.getItem("isSigIn") === "true") {

            const allProject = []
            //deleting file from Google Drive
            await getDriveProjects(allProject);
            const findCurrentProject = allProject.find(currentProject => currentProject?.projectName === projectName);
            //check if file present in drive or not js and xml
            findCurrentProject && await checkFileExistsInFolder(await getFolderId(), findCurrentProject?.projectName, "js").then(async (response) => {
                if (response.exists)
                    await deleteFileFromGoogleDrive(response?.fileId)
            })
            findCurrentProject && await checkFileExistsInFolder(await getFolderId(), findCurrentProject?.projectName, "xml").then(async (response) => {
                if (response.exists)
                    await deleteFileFromGoogleDrive(response?.fileId)
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
export function updateCurrentProject(projectName, code) {

    const project = {
        storage: "local",
        projectName: projectName,
        xmlValue: code,
        updatedDate: FormatDate().currentDate,
        time: FormatDate().currentTime,
        updatedTime: new Date(),
    }
    //current project will first get store in current
    localStorage.setItem(localStorageKeys.currentProject, JSON.stringify(project))
    //now will check current project is in all project or not.
    const found = JSON.parse(localStorage?.getItem(localStorageKeys.allProjects))?.find((project) => {
        return project.projectName === getCurrentProject().projectName
    })
    //current project is not in local so will save it in local
    if (!found) {
        //add current project in local
        createProjectInLocal(localStorage.getItem(localStorageKeys.currentProject))
        // auto sync
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
        let index = allProjects?.findIndex(obj => obj.projectName === getCurrentProject().projectName)
        allProjects?.find((project) => {
                if (project.projectName === getCurrentProject().projectName) {
                    allProjects.splice(index, 1, getCurrentProject())
                }
                return "";
            }
        )
        localStorage.setItem(localStorageKeys.allProjects, JSON.stringify(allProjects));
    }
}


/**
 * remove duplicate project get from drive and also save in local. and give high priority to local project
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
            if (uniqueIds[project.projectName]) {
                return false;
            }
            // mark id value as seen
            uniqueIds[project.projectName] = true;

            // check if storage value is 'drive'
            if (project.storage === 'drive') {
                console.log("project:::::", project.projectName, project)
                // check if id value is unique
                return allProjects.filter(o => o.projectName === project.projectName).length === 1;
            }
            return true;
        });
    })

    filterProjects.sort((a, b) => {
        return new Date(b.updatedTime) - new Date(a.updatedTime);
    });

    return filterProjects;
}


/**
 * Format Date
 * @returns {{currentTime: string, currentDate: string}}
 * @constructor
 */
export function FormatDate() {
    const date = new Date();
    const dateOptions = {day: 'numeric', month: 'long', year: 'numeric'};
    const currentDate = date.toLocaleDateString('en-US', dateOptions)
    const timeOptions = {hour: 'numeric', minute: 'numeric', hour12: false};
    const currentTime = date.toLocaleTimeString('en-US', timeOptions);
    return {currentDate: currentDate, currentTime: currentTime}
}


/**
 * Renames a project by updating its name in local storage and potentially in Google Drive if the user is signed in.
 * @param {string} projectName - The new name for the project.
 * @param {string} oldName - The current name of the project.
 * @param {string} screen - The current screen the user is on. Used to determine whether to refresh the page after renaming the project.
 * @returns {Promise<void>}
 */
export async function renameProject(projectName, oldName, screen) {
    // Only proceed if the project names are different
    if (projectName !== oldName) {
        // Prepare data for updating local storage and Google Drive
        let data = {
            projectName: projectName,
            xmlValue: getCurrentProject()?.xmlValue,
        }
        // Update project name in local storage
        const allProjects = JSON.parse(localStorage?.getItem(localStorageKeys.allProjects));
        let projectsArray = allProjects || []
        const specificProject = projectsArray.findIndex((project) => project.projectName === oldName);

        //if project only in drive then specificProject in local will not be present, so it will return -1
        if (specificProject >= 0) {
            //if project is in localStorage then update their name.
            projectsArray[specificProject].projectName = projectName;
            localStorage.setItem(localStorageKeys.allProjects, JSON.stringify(projectsArray))
        }

        // If user is signed in, update project name in Google Drive and check for renamed files
        if (localStorage.getItem("isSigIn") === "true") {
            // Add project name to data object for updating project in Google Drive
            data = Object.assign(data, {projectName: projectName});
            // Update current project in Google Drive if it has the old project name
            if (oldName === getCurrentProject().projectName) {
                updateCurrentProject(projectName, data.xmlValue)
            }
            // Check if XML and JS files for the project exist and rename them if necessary
            const xmlFileExists = await checkFileExistsInFolder(await getFolderId(), oldName, "xml");
            const jsFileExists = await checkFileExistsInFolder(await getFolderId(), oldName, "js");
            if (xmlFileExists.exists)
                await fileRename(projectName, oldName, "xml")
            if (jsFileExists.exists)
                await fileRename(projectName, oldName, "js")

            // If user is not signed in, only update current project if it has the old project name
        } else {
            if (oldName === getCurrentProject()?.projectName) {
                data = Object.assign(data, {projectName: projectName});
                updateCurrentProject(projectName, data.xmlValue)
            }
        }

    }
}
