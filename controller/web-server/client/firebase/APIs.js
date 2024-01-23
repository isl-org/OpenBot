import {addDoc, and, collection, doc, getDoc, getDocs, query, updateDoc, where} from '@firebase/firestore'
import {auth, db} from './authentication'
import {Month, tables} from '../utils/constants'

/**
 * function to upload user usage on monthly basis on firebase firestore
 * @returns {Promise<void>}
 * @param duration
 */
export async function uploadUserData (duration) {
    try {
        const date = new Date()
        const monthName = Month[date.getMonth()] + '-' + date.getFullYear()
        const workspaceRef = doc(collection(db, tables.users), auth.currentUser?.uid)
        const userMonthlyUsage = {
            month: monthName,
            projects: 0,
            models: 0,
            serverDuration: duration,
            id: workspaceRef.id
        }
        const docDetails = await getDocDetails(monthName, tables.userUsage, 'month', workspaceRef.id)
        if (docDetails === null) {
            await addDoc(collection(db, tables.userUsage),
                userMonthlyUsage
            ).then()
        } else {
            const updatedData = docDetails.data
            const userUsageRef = doc(db, tables.userUsage, docDetails.id)
            updatedData.serverDuration += duration
            await updateDoc(userUsageRef,
                updatedData
            ).then()
        }
    } catch
        (e) {
        console.log('error in setting projects:', e)
    }
}

/**
 * function to get document details from the firebase firestore
 * @param value
 * @param table
 * @param fieldName
 * @param id
 * @returns {Promise<null>}
 */
const getDocDetails = async (value, table, fieldName, id) => {
    try {
        const ordersQuery = query(collection(db, table), and(where(fieldName, '==', value), where('id', '==', id)));
        const querySnapshot = await getDocs(ordersQuery)
        let response = null
        querySnapshot.forEach((doc) => {
            return response = {
                data: doc.data(),
                id: doc.id
            }
        })
        return response
    } catch (error) {
        console.log('error :', error)
    }
}

/**
 * function to get user current plan
 * @returns {Promise<undefined|*>}
 */
export async function getUserPlan () {
    const usersRef = doc(db, 'users', auth?.currentUser?.uid)
    try {
        const docSnapshot = await getDoc(usersRef)
        if (!docSnapshot.exists()) {
            return undefined
        }
        const subscriptionData = docSnapshot.data()?.subscription
        if (subscriptionData?.paid) {
            if (new Date() <= subscriptionData.planEndDate) {
                return subscriptionData.planEndDate
            }
        }
    } catch (error) {
        console.error('Error retrieving user plan:', error)
    }
    return undefined
}