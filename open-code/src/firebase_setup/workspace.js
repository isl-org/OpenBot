import {auth, db} from "./firebase";
import Blockly from "blockly/core";
import {FirebaseStorage} from "./firebase";
import { getFirestore,  collection, addDoc, getDocs} from "firebase/firestore";

async function savingWorkspace(projectName) {
    try {

        const xml = Blockly.Xml.workspaceToDom(Blockly.getMainWorkspace());
        const xmlText = Blockly.Xml.domToText(xml);
        const data={
            xmlText:xmlText,
            projectName:projectName
        }
        const workspaceRef = addDoc(collection(db, auth.currentUser.uid), data);

        await workspaceRef
            .then((res) => {
                    console.log('Uploaded a data_url string!',res);
                },
            )
            .catch(error => console.log(error))

    } catch (err) {
        console.log(err);
    }

}

// async function loadingWorkspace(projectName) {
//     try {
//         // return workspaceRef.get()
//         //     .then(doc => {
//         //         if (doc.exists) {
//         //             const workspaceXml = doc.data().workspaceXml;
//         //             const xml = Blockly.Xml.textToDom(workspaceXml);
//         //             Blockly.Xml.clearWorkspaceAndLoadFromXml(xml, Blockly.getMainWorkspace());
//         //         }
//         //     });
//
//         const workspaceRef = db.collection('workspaces').doc(auth.currentUser.uid);
//         // const workspaceRef = getDocs(collection(db, "workspace"));
//         const doc = await workspaceRef.get();
//         const xmlText = doc.data().workspaceXml;
//         const xml = Blockly.Xml.textToDom(xmlText);
//
//         Blockly.Xml.domToWorkspace(xml, Blockly.getMainWorkspace());
//     } catch (error) {
//         console.error(error);
//     }
// }

export {savingWorkspace};