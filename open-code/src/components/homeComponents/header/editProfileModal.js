import React, {forwardRef, useContext, useEffect, useRef, useState} from "react";
import {ThemeContext} from "../../../App";
import {auth, uploadProfilePic} from "../../../services/firebase";
import styles from "../../navBar/navbar.module.css";
import {Images} from "../../../utils/images";
import {Box, Modal} from "@mui/material";
import LoaderComponent from "../../loader/loaderComponent";
import SimpleInputComponent from "../../inputComponent/simpleInputComponent";
import BlueButton from "../../buttonComponent/blueButtonComponent";
import {Constants, Themes} from "../../../utils/constants";

export function EditProfileModal(props) {
    const {isEditProfileModal, setIsEditProfileModal, user} = props
    const inputRef = useRef();
    const {theme} = useContext(ThemeContext)
    const [file, setFile] = useState(user?.photoURL && user.photoURL);
    const [fullName, setFullName] = useState(user?.displayName);
    const [isAlertSuccess, setIsAlertSuccess] = useState(false)
    const [isAlertError, setIsAlertError] = useState(false)
    const [isLoader, setIsLoader] = useState(false)
    const [userDetails, setUserDetail] = useState({
        displayName: user?.displayName,
        email: user?.email,
        photoUrl: user?.photoURL
    })

    useEffect(() => {
        setUserDetail({
            displayName: auth?.currentUser.displayName,
            photoUrl: auth?.currentUser.photoURL,
        })
    }, [])

    const handleClose = () => {
        setIsEditProfileModal(false)
    }

    function handleChange(e) {
        console.log(e.target.files[0].name.replace(/HEIC/g, 'jpg'));
        setFile(e.target.files[0])
        setUserDetail({
            ...userDetails,
            photoUrl: URL.createObjectURL(e.target.files[0])
        })
    }

    function handleNameChange(name) {
        setFullName(name)
        setUserDetail({
            ...userDetails,
            displayName: name,
        })
    }

    const handleSubmit = async () => {
        //TODO date of birth condition needs to be handle
        if (user.photoURL !== file || user.displayName !== userDetails.displayName) {
            setIsLoader(true);
            await uploadProfilePic(file, file.name).then((photoURL) => {
                if (user.photoURL === file) {
                    photoURL = file;
                }
                auth.currentUser.updateProfile({
                    photoURL: photoURL,
                    displayName: userDetails.displayName,
                }).then(() => {
                    setIsAlertSuccess(true)
                    setTimeout(
                        function () {
                            handleClose()
                            setIsAlertSuccess(false)
                        }.bind(this), 3000);
                }).catch((error) => {
                    console.log(error)
                    setIsAlertError(true)
                })
                setIsLoader(false)
            })
        }
    }


    return (
        <Modal
            open={isEditProfileModal}
            style={{display: "flex", alignItems: "center", justifyContent: "center"}}>
            {isLoader ?
                <LoaderComponent color="blue"/>
                :
                <Box className={styles.editProfileModal + " " + (theme === Themes.dark && styles.darkEditProfileModal)}>

                    <div className={styles.crossIconDiv}>
                        <img onClick={handleClose} alt={"cross icon"} className={styles.crossIcon}
                             src={theme === Themes.dark ? Images.darkCrossIcon : Images.lightCrossIcon}/>
                    </div>

                    <div style={{backgroundImage: `url(${userDetails.photoUrl})`}} className={styles.profileImg}>
                        <input ref={inputRef} style={{display: "none",}} type="file" onChange={handleChange}/>
                        <img onClick={() => inputRef.current.click()} alt={"edit profile icon"}
                             className={styles.editProfileIcon} src={Images.editProfileIcon}/>
                    </div>

                    <div style={{display: "flex"}}>
                        <SimpleInputComponent inputType={"text"} extraStyle={styles.inputExtraStyle}
                                              inputTitle={"Full Name"}
                                              value={fullName} onDataChange={handleNameChange}/>
                        <SimpleInputComponent inputType={"date"} extraStyle={styles.inputExtraStyle}
                                              inputTitle={"Date Of Birth"}/>
                    </div>
                    <SimpleInputComponent inputType={"email"} extraStyle={styles.emailInputExtraStyle}
                                          inputTitle={"Email address"} value={user?.email}/>

                    <div style={{display: "flex"}}>
                        <BlueButton onClick={handleSubmit} buttonType={"contained"} buttonName={"Save"}/>
                        <BlueButton onClick={handleClose} buttonName={"Cancel"}/>
                    </div>
                    {isAlertSuccess && <Alert message={"Profile updated successfully!"}/>}
                    {isAlertError && <Alert message={"Oops! There was an error."}/>}
                </Box>
            }
        </Modal>
    )
}


/**
 * Alert UI
 * @type {React.ForwardRefExoticComponent<React.PropsWithoutRef<{}> & React.RefAttributes<unknown>>}
 */
export const Alert = forwardRef(function Alert(props, ref) {
    return (
        <div
            className={styles.successAlertBox + " " + (props.message !== Constants.ProfileSuccessMsg && styles.errorAlertBox)}>
            <img alt={"emoji icon"}
                 style={{height: 20, marginRight: 10}}
                 src={props.message === Constants.ProfileSuccessMsg ? Images.successfulEmojiIcon : Images.errorEmojiIcon}/>
            {props.message}
        </div>
    )
})

