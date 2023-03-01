import {auth, db} from "./firebase";
import Blockly from "blockly/core";
import {collection, doc, setDoc} from "firebase/firestore";

export async function savingWorkspace(projectName) {
    try {
        const date = new Date();
        const options = {day: 'numeric', month: 'long', year: 'numeric'};
        const xml = Blockly.Xml.workspaceToDom(Blockly.getMainWorkspace());
        const xmlText = Blockly.Xml.domToText(xml);
        const data = {
            xmlText: xmlText,
            Date: date.toLocaleDateString('en-US', options)
        }
        doc(collection(db, auth.currentUser.uid));
        await setDoc(doc(db, auth.currentUser.uid, projectName), data);
    } catch (err) {
        console.log(err);
    }
}

export function saveCurrentProject(projectName, code) {
    const project = {
        [projectName]: code,
    }
    localStorage.setItem("CurrentProject", JSON.stringify(project))
}

export function saveXmlInLocal(currentProject) {
    console.log(currentProject)
    const objectCurrentProject = JSON.parse(currentProject)
    const getAllProjects = localStorage.getItem("Projects")
    const objectGetAllProjects = JSON.parse(getAllProjects)

    let ProjectsArray = objectGetAllProjects
    if (ProjectsArray) {
        ProjectsArray.push(objectCurrentProject)
    } else {
        ProjectsArray = [objectCurrentProject]
    }
    localStorage.setItem("Projects", JSON.stringify(ProjectsArray))
}

export function getCurrentProject() {
    try {
        const getProject = localStorage.getItem("CurrentProject")
        return JSON.parse(getProject)
    } catch (e) {
    }
}

