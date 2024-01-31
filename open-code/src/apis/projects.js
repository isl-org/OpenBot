import {
    addDoc,
    collection,
    doc,
    updateDoc,
    where,
    query,
    getDocs, and, writeBatch, getAggregateFromServer, sum
} from "firebase/firestore";
import {auth, db} from "../services/firebase";
import {Month, tables} from "../utils/constants";

/**
 * function to set user usage for projects in firebase firestore
 * @param projectName
 * @returns {Promise<void>}
 */
export async function setProjectDetails(projectName) {
    const date = new Date();
    const year = date.getFullYear();
    const getMonth = date.getMonth();
    const details = {
        name: projectName,
        uid: auth?.currentUser.uid,
        status: {
            year: year,
            month: Month[getMonth],
            update: 1
        },
    };
    try {
        let docDetails = await getDocDetails(projectName, tables.projects, "name");
        if (docDetails === null) {
            await addDoc(collection(db, tables.projects),
                details
            ).then();
        } else {
            const projectsRef = doc(db, tables.projects, docDetails?.id);
            let updatedData = docDetails?.data;
            updatedData.status.update += 1;
            await updateDoc(projectsRef,
                updatedData
            ).then(() => {
            });
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
        const ordersQuery = query(collection(db, tables.projects), and(where("name", '==', oldProjectName), where("uid", '==', auth?.currentUser.uid)));
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

/**
 * function to get document details from the firebase firestore
 * @param value
 * @param table
 * @param fieldName
 * @returns {Promise<null>}
 */
export const getDocDetails = async (value, table, fieldName) => {
    try {
        const year = new Date().getFullYear();
        const getMonth = new Date().getMonth();
        const ordersQuery = query(collection(db, table), and(where(fieldName, '==', value), where("uid", '==', auth?.currentUser.uid), where("status.year", '==', year), where("status.month", '==', Month[getMonth])));
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

/**
 * function to get projects compile time
 * @returns {Promise<number>}
 */
export const sumUploadCode = async () => {
    try {
        const ordersQuery = query(collection(db, tables.projects), and(where("uid", '==', auth?.currentUser.uid)));
        const snapshot = await getAggregateFromServer(ordersQuery, {
            totalCompileCode: sum('status.update')
        });
        return snapshot.data().totalCompileCode;
    } catch (e) {
        console.log(e);
    }
}