import {auth, db} from "./firebase";
import Blockly from "blockly/core";
import {doc, setDoc, getDoc, collection, getDocs, Timestamp} from "firebase/firestore";
import {useRef} from "react";
import BlocklyComponent from "../components/blockly/BlocklyComponent";
async function savingWorkspace(projectName) {
    try {
        const date = new Date();
        const options = { day: 'numeric', month: 'long', year: 'numeric' };
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

async function loadingWorkspace(projectName) {
    try {
        const blockSnap = doc(db, auth.currentUser.uid,projectName);
        const workspaceRef=await getDoc(blockSnap);
                if (workspaceRef.exists()) {
                    const xmlText = workspaceRef.data().xmlText;
                    const xml = Blockly.Xml.textToDom(xmlText);
                    Blockly.Xml.clearWorkspaceAndLoadFromXml(xml, Blockly.getMainWorkspace());
                    // Blockly.Xml.domToWorkspace(xml, Blockly.getMainWorkspace());
                    console.log("workspace loaded")
                }
    } catch (error) {
        console.error(error);
    }

}



export {savingWorkspace,loadingWorkspace};