import {
    addDoc,
    collection,
} from "firebase/firestore";
import firebase from "firebase/compat/app";
import {auth, db} from "../services/firebase";
import {tables} from "../utils/constants";
import {nanoid} from "nanoid";

/**
 * function to upload model details to firebase firestore
 * @param modelName
 * @returns {Promise<void>}
 */
export async function uploadModelDetails(modelName) {
    const time = firebase.firestore.Timestamp.fromDate(new Date()).toDate();
    const details = {
        name: modelName,
        uid: auth?.currentUser.uid,
        create: time,
        id: nanoid()
    }
    try {
        await addDoc(collection(db, tables.models),
            details
        ).then();
    } catch (e) {
        console.log("error in uploading projects::", e);
    }
}
