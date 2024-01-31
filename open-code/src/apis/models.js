import {
    addDoc, and,
    collection, getAggregateFromServer, getCountFromServer, query, sum, where,
} from "firebase/firestore";
import {auth, db} from "../services/firebase";
import {Month, tables} from "../utils/constants";
import {nanoid} from "nanoid";

/**
 * function to upload model details to firebase firestore
 * @param modelName
 * @returns {Promise<void>}
 */
export async function uploadModelDetails(modelName) {
    const date = new Date();
    const year = date.getFullYear();
    const getMonth = date.getMonth();
    const details = {
        name: modelName,
        uid: auth?.currentUser.uid,
        status: {
            year: year,
            month: Month[getMonth],
        },
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

export async function getModelsCount() {
    try {
        const ordersQuery = query(collection(db, tables.models), and(where("uid", '==', auth?.currentUser.uid)));
        const snapshot = await getCountFromServer(ordersQuery);
        return snapshot.data().count;
    } catch (e) {
        console.log(e);
    }
}