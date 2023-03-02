import {auth, db} from "./firebase";
import Blockly from "blockly/core";
import {collection, doc, setDoc, updateDoc} from "firebase/firestore";

export async function savingWorkspace(projectName, currentProjectId) {
    const date = new Date();
    const options = {day: 'numeric', month: 'long', year: 'numeric'};
    const currentDate = date.toLocaleDateString('en-US', options)
    const xmlValue = Blockly.Xml.workspaceToDom(Blockly.getMainWorkspace());
    const xmlText = Blockly.Xml.domToText(xmlValue);
    const workspaceRef = doc(collection(db, auth.currentUser.uid), currentProjectId);
    try {
        await updateDoc(workspaceRef,{
            date:currentDate,
            xmlText:xmlText
        })
    } catch (err) {
        console.log(err);
    }
}

export async function createWorkspace(projectName, currentProjectId, setCurrentProjectId) {
    const date = new Date();
    const options = {day: 'numeric', month: 'long', year: 'numeric'};
    const currentDate = date.toLocaleDateString('en-US', options)
    const data = {
        date: currentDate,
        projectTitle: projectName,
        xmlText: "",
    }
    const uniqueId = generatePath(projectName);
    setCurrentProjectId(uniqueId);
    if (localStorage.getItem("isSigIn") === "true") {
        try {
            const workspaceRef = doc(collection(db, auth.currentUser.uid), uniqueId);
            await setDoc(workspaceRef, data);
            console.log("workspace created = ", workspaceRef)
        } catch (err) {
            console.log(err);
        }
    } else {
        //create in local storage
    }
}

export function generatePath(projectName) {
    const date = new Date();
    const options = {day: 'numeric', month: 'numeric', year: 'numeric'};
    const customDate = date.toLocaleDateString('en-US', options).replace(/\//g, '-');
    const finalDate = customDate.replace(/\s+/g, '-');
    let projectNameWithoutSpace = projectName.replace(/\s+/g, '-');
    const hours = date.getHours();
    const minutes = date.getMinutes();
    const seconds = date.getSeconds();
    const milliseconds = date.getMilliseconds();
    const timestamp = hours + ":" + minutes + ":" + seconds;
    return projectNameWithoutSpace + "_" + finalDate + "_" + timestamp + ":" + milliseconds;
}

export function saveCurrentProject(projectName, code) {
    const date = new Date();
    const options = {day: 'numeric', month: 'long', year: 'numeric'};
    const currentDate = date.toLocaleDateString('en-US', options)
    let uniqueId
    try {
        uniqueId = JSON.parse(localStorage.getItem("Projects")).length
    } catch (error) {
        uniqueId = 0
    }
    const project = {
        id: uniqueId,
        [projectName]: code,
        date: currentDate,
    }
    localStorage.setItem("CurrentProject", JSON.stringify(project))
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

