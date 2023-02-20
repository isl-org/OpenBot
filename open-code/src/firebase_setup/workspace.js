import {auth} from "./firebase";
import Blockly from "blockly/core";
import {ref, uploadString, uploadBytesResumable, getDownloadURL} from "firebase/storage";
import {FirebaseStorage} from "./firebase";

function getUserId() {
    return new Promise((resolve, reject) => {
        const unsubscribe = auth.onAuthStateChanged(user => {
            if (user) {
                unsubscribe();
                resolve(user.uid);
            } else {
                reject(new Error('User not authenticated'));
            }
        });
    });
}

async function savingWorkspace() {
    try {
        const userId = await getUserId();
        console.log("userID", userId);
        const xml = Blockly.Xml.workspaceToDom(Blockly.getMainWorkspace());
        const xmlText = Blockly.Xml.domToText(xml);
        const storageRef = ref(FirebaseStorage, userId + "hardik");
        const uploadTask = uploadBytesResumable(storageRef, xmlText);

        uploadTask.on('state_changed',
            (snapshot) => {
                console.log(snapshot.bytesTransferred , snapshot.totalBytes)
                // const progress = (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
                // console.log('Upload is ', progress.toString() ,'% done');
                console.log(xmlText)
            },
            error => console.log(error),
            () => {
                getDownloadURL(uploadTask.snapshot.ref)
                    .then((downloadURL) => {
                            console.log('File available at', downloadURL);

                        }
                    )
            }
        )
    }
    catch (err){
        console.log(err);
    }
}

// async function loadingWorkspace() {
//     try {
//         const userId = await getUserId();
//         console.log("userId = ", userId)
//         const workspaceRef = db.collection("Workspace").doc(userId);
//
//         return workspaceRef.get()
//             .then(doc => {
//                 if (doc.exists) {
//                     const workspaceXml = doc.data().workspaceXml;
//
//                     const xml = Blockly.Xml.textToDom(workspaceXml);
//                     Blockly.Xml.clearWorkspaceAndLoadFromXml(xml, Blockly.getMainWorkspace());
//                 }
//             });
//     } catch (error) {
//         console.error(error);
//     }
// }

    export {savingWorkspace};