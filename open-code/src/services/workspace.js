import {auth, db} from "./firebase";
import Blockly from "blockly/core";
import {collection, doc, setDoc} from "firebase/firestore";

async function savingWorkspace(projectName) {
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


export {savingWorkspace};