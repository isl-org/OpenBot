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
            monthlyUpdate: Object.fromEntries(Month.map((month, index) => [month, index === getMonth ? 1 : 0]))
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
            let updatedData = docDetails?.data
            updatedData.status.monthlyUpdate[Month[getMonth]] += 1;
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

export function queryBuilder(table, fieldName, value) {
    const year = new Date().getFullYear();
    return query(collection(db, table), and(where(fieldName, '==', value), where("uid", '==', auth?.currentUser.uid), where("status.year", '==', year)));
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
        const ordersQuery = queryBuilder(table, fieldName, value);
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