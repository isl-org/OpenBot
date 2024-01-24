import {
    addDoc,
    collection,
    doc,
    updateDoc,
    where,
    query,
    getDocs, and, writeBatch
} from "firebase/firestore";
import {auth, db} from "../services/firebase";
import {tables} from "../utils/constants";
import firebase from "firebase/compat/app";

/**
 * function to set user usage for projects in firebase firestore
 * @param projectName
 * @returns {Promise<void>}
 */
export async function setProjectDetails(projectName) {
    const time = firebase.firestore.Timestamp.fromDate(new Date()).toDate();

    const details = {
        name: projectName,
        uid: auth?.currentUser.uid,
        create: time,
        update: null,
    }
    try {
        let docDetails = await getDocDetails(projectName, tables.projects, "name");

        if (docDetails === null) {
            await addDoc(collection(db, tables.projects),
                details
            ).then();
        } else if (docDetails?.data.update === null) {
            const projectsRef = doc(db, tables.projects, docDetails.id);
            let updatedData = docDetails?.data
            updatedData.update = time
            await updateDoc(projectsRef,
                updatedData
            ).then(() => {
            });
        } else {
            const newDetails = docDetails?.data
            newDetails.update = time
            await addDoc(collection(db, tables.projects),
                newDetails
            ).then();
        }
    } catch (e) {
        console.log("error in uploading projects::", e);
    }
}

/**
 * function to rename projects in firebase firestore
 * @param oldProjectName
 * @param newProjectName
 * @returns {Promise<void>}
 */
export async function renameAllProjects(oldProjectName, newProjectName) {
    try {
        const ordersQuery = queryBuilder(tables.projects, "name", oldProjectName)
        const querySnapshot = await getDocs(ordersQuery);
        const batch = writeBatch(db);
        querySnapshot.forEach((document) => {
            batch.update(document.ref, {name: newProjectName});
        });
        await batch.commit();
    } catch (e) {
        console.log("error in uploading projects::", e);
    }
}

export function queryBuilder(table, fieldName, value) {
    return query(collection(db, table), and(where(fieldName, '==', value), where("uid", '==', auth?.currentUser.uid)));
}


/**
 * function to get document details from the firebase firestore
 * @param value
 * @param table
 * @param fieldName
 * @param id
 * @returns {Promise<null>}
 */
export const getDocDetails = async (value, table, fieldName) => {
    try {
        const ordersQuery = queryBuilder(table, fieldName, value)
        const querySnapshot = await getDocs(ordersQuery);
        let response = null;
        querySnapshot.forEach((doc) => {
            return response = {
                data: doc.data(),
                id: doc.id
            }
        });
        return response
    } catch (error) {
        console.log("error :", error);
    }
}