import {auth, db} from "./firebase";
import Blockly from "blockly/core";
import {collection, deleteDoc, doc, setDoc, updateDoc} from "firebase/firestore";
import {nanoid} from 'nanoid';

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

export async function savingWorkspace(projectName, currentProjectId) {
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

export async function deletingWorkspace(currentProjectId) {
    try {
        await deleteDoc(doc(db, auth.currentUser.uid, currentProjectId)).then(() => {
            console.log("project deleted successfully");
        })
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
        [projectName]: code,
        date: currentDate,
    }
    localStorage.setItem("CurrentProject", JSON.stringify(project))
    const found = JSON.parse(localStorage?.getItem("Projects"))?.find((project) => {
        return project.id === getCurrentProject().id
    })
    if (!found) {
        saveXmlInLocal(localStorage.getItem("CurrentProject"))
    }
}

export function getCurrentProject() {
    try {
        const getProject = localStorage.getItem("CurrentProject")
        return JSON.parse(getProject)
    } catch (e) {
    }
}

export function saveXmlInLocal(currentProject) {
    const objectCurrentProject = JSON.parse(currentProject)
    const getAllProjects = localStorage.getItem("Projects")
    let ProjectsArray = JSON.parse(getAllProjects)
    if (ProjectsArray) {
        ProjectsArray.push(objectCurrentProject)
    } else {
        ProjectsArray = [objectCurrentProject]
    }
    localStorage.setItem("Projects", JSON.stringify(ProjectsArray))
}

/**
 * get all saved projects from local storage.
 */
export function getAllLocalProjects() {
    try {
        const projects = localStorage.getItem("Projects")
        return JSON.parse(projects)
    } catch (error) {

    }
}

