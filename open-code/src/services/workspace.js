import {auth, db} from "./firebase";
import Blockly from "blockly/core";
import {collection, doc, setDoc} from "firebase/firestore";

const date = new Date();
const options = {day: 'numeric', month: 'long', year: 'numeric'};
const currentDate = date.toLocaleDateString('en-US', options)

export async function savingWorkspace(projectName) {
    try {
        const xml = Blockly.Xml.workspaceToDom(Blockly.getMainWorkspace());
        const xmlText = Blockly.Xml.domToText(xml);
        const data = {
            xmlText: xmlText,
            date: currentDate,
        }
        doc(collection(db, auth.currentUser.uid));
        await setDoc(doc(db, auth.currentUser.uid, projectName), data);
    } catch (err) {
        console.log(err);
    }
}

export function saveCurrentProject(projectName, code) {
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
        console.log("all projects = > ", JSON.parse(localStorage.getItem("Projects")).length)
        return JSON.parse(projects)
    } catch (error) {

    }
}

