import {doc, getDoc, setDoc, updateDoc} from '@firebase/firestore'
import {auth, db} from './authentication'

/**
 * function to store user usage on monthly basis
 * @param time
 * @returns {Promise<void>}
 */
export const uploadUserData = async (time) => {
    try {
        const Month = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December']
        const docRef = doc(db, 'users', auth.currentUser?.uid)
        const docSnapshot = await getDoc(docRef)
        const dataArray = []
        const date = new Date()
        const monthName = Month[date.getMonth()] + ' ' + date.getFullYear()
        const userObject = {
            [monthName]: {
                projects: 0,
                models: 0,
                serverDuration: time
            }
        }
        dataArray.push(userObject)
        if (docSnapshot.exists()) {
            if (docSnapshot.data().userData !== undefined) {
                const docSnapData = docSnapshot.data().userData || [] // user data
                // eslint-disable-next-line no-prototype-builtins
                const index = docSnapData.findIndex(entry => entry.hasOwnProperty(monthName))
                if (index !== -1) {
                    docSnapData[index][monthName].serverDuration += time
                } else {
                    docSnapData.push(userObject)
                }
                await updateDoc(docRef, {
                    userData: docSnapData
                }).catch((e) => {
                    console.log('error in updating:', e)
                })
            } else {
                await setDoc(docRef, {
                    userData: dataArray
                }, {merge: true}).catch((e) => {
                    console.log('error in setting userData:', e)
                })
            }
        }
    } catch
        (e) {
        console.log('error in setting projects:', e)
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