import {auth, db} from "./firebase";
import Blockly from "blockly/core";
import {collection, deleteDoc, doc, setDoc, updateDoc} from "firebase/firestore";
import {nanoid} from 'nanoid';
import {localStorageKeys} from "../utils/constants";

export async function createWorkspace(data, uniqueId) {

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

export async function updatingWorkspace(projectName, currentProjectId) {
    const date = new Date();
    const options = {day: 'numeric', month: 'long', year: 'numeric'};
    const currentDate = date.toLocaleDateString('en-US', options);
    const xmlValue = Blockly.Xml.workspaceToDom(Blockly.getMainWorkspace());
    const xmlText = Blockly.Xml.domToText(xmlValue);
    const workspaceRef = doc(collection(db, auth.currentUser.uid), currentProjectId);
    try {
        await updateDoc(workspaceRef, {
            date: currentDate,
            xmlText: xmlText
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

export function saveCurrentProject(uniqueId, projectName, code) {
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
        saveXmlInLocal(localStorage.getItem(localStorageKeys.currentProject))
    }
}

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
export function saveXmlInLocal(currentProject) {
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

