import React, {forwardRef, useContext, useEffect, useRef, useState} from "react";
import {ThemeContext} from "../../../App";
import {auth, uploadProfilePic} from "../../../services/firebase";
import styles from "../../navBar/navbar.module.css";
import {Images} from "../../../utils/images";
import {Box, Modal} from "@mui/material";
import LoaderComponent from "../../loader/loaderComponent";
import SimpleInputComponent from "../../inputComponent/simpleInputComponent";
import BlueButton from "../../buttonComponent/blueButtonComponent";
import {Constants, errorToast, Themes} from "../../../utils/constants";
import Compressor from 'compressorjs';
import {StoreContext} from "../../../context/context";
import heic2any from "heic2any";

/**
 * Edit  Profile option modal contains profile image picker and edit name, birthdate nad email field.
 * @param props
 * @returns {JSX.Element}
 * @constructor
 */
export function EditProfileModal(props) {
    const {isEditProfileModal, setIsEditProfileModal, user} = props
    const inputRef = useRef();
    const {theme} = useContext(ThemeContext)
    const {isOnline} = useContext(StoreContext)
    const [file, setFile] = useState(user?.photoURL && user.photoURL);
    const [fullName, setFullName] = useState(user?.displayName);
    const [isAlertSuccess, setIsAlertSuccess] = useState(false)
    const [isAlertError, setIsAlertError] = useState(false)
    const [isLoader, setIsLoader] = useState(false);
    const [isNameEmpty, setIsNameEmpty] = useState(false);
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


    //compressing the profile image
    async function handleCompressFile(e) {
        const file = e.target.files[0];
        let convertedFile = file;
        if (file.type === 'image/heic' || file.type === 'image/heif') {
            try {
                const convertedImage = await heic2any({
                    blob: file,
                    toType: 'image/jpeg', // Convert HEIC to JPEG
                });

                convertedFile = new File([convertedImage], 'converted.jpg', {type: 'image/jpeg'});
            } catch (error) {
                // Handle conversion error
                console.error('Error converting HEIC image:', error);
                return;
            }
        }

        new Compressor(convertedFile, {
            quality: 0.2, // Set the compression quality (0 to 1)
            success(result) {
                setFile(result);
            },
            error(err) {
                setFile(convertedFile);
                console.log(err.message);
            },
        });
    }

    //handle image change
    function handleChange(e) {
        handleCompressFile(e).then(() =>
            setUserDetail({
                ...userDetails,
                photoUrl: URL.createObjectURL(e.target.files[0])
            })
        );

    }

    //handle name change
    function handleNameChange(name) {
        if (!(name.trim().length <= 0)) {
            setIsNameEmpty(false);
            setFullName(name);
            setUserDetail({
                ...userDetails,
                displayName: name,
            })
        } else {
            setIsNameEmpty(true);
            setFullName(user?.displayName)
        }
    }

    const handleSubmit = async () => {
        if (isOnline) {
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
                            }.bind(), 1000);
                    }).catch((error) => {
                        console.log("error::::", error);
                        setIsAlertError(true)
                    })
                    setIsLoader(false)
                })
            }
        } else {
            errorToast(Constants.InternetOffMsg);
        }
    }


    return (
        <Modal
            open={isEditProfileModal}
            style={{display: "flex", alignItems: "center", justifyContent: "center", overflow: "scroll"}}>

            <Box className={styles.editProfileModal + " " + (theme === Themes.dark && styles.darkEditProfileModal)}>
                <div className={styles.crossIconDiv}>
                    <img onClick={handleClose} alt={"cross icon"} className={styles.crossIcon}
                         src={theme === Themes.dark ? Images.darkCrossIcon : Images.lightCrossIcon}/>
                </div>
                {/* profile icon */}
                <div style={{backgroundImage: `url(${userDetails.photoUrl})`}}
                     className={styles.profileImg + " " + (isLoader && styles.loader)}>
                    {isLoader ?
                        <LoaderComponent color="blue" height="20" width="20"/> :
                        <>
                            <input ref={inputRef} style={{display: "none",}} type="file" accept="image/*,.heic,.heif"
                                   onChange={handleChange}/>
                            <img onClick={() => inputRef.current?.click()} alt={"edit profile icon"}
                                 className={styles.editProfileIcon} src={Images.editProfileIcon}/>
                        </>
                    }
                </div>
                <div style={{display: "flex"}}>
                    <SimpleInputComponent inputType={"text"} extraStyle={styles.inputExtraStyle}
                                          headStyle={styles.headStyle} inlineStyle={{border:isNameEmpty && "1px solid red"}}
                                          inputTitle={"Full Name"} extraInputStyle={styles.extraInputStyle}
                                          value={fullName} onDataChange={handleNameChange}/>
                    <SimpleInputComponent inputType={"date"} extraStyle={styles.inputExtraStyle}
                                          headStyle={styles.headStyle}
                                          inputTitle={"Date Of Birth"} extraInputStyle={styles.extraInputStyle}/>
                </div>
                <SimpleInputComponent inputType={"email"} extraStyle={styles.emailInputExtraStyle}
                                      headStyle={styles.headStyle}
                                      inputTitle={"Email address"} value={user?.email}
                                      extraInputStyle={styles.extraInputStyle}/>

                <div className={styles.buttonSection}>
                    <BlueButton onClick={handleSubmit} buttonType={"contained"} buttonName={"Save"}
                                buttonStyle={styles.buttonText}/>
                    <BlueButton onClick={handleClose} buttonName={"Cancel"} buttonStyle={styles.buttonText}/>
                </div>

                {isAlertSuccess && <Alert message={"Profile updated successfully!"}/>}
                {isAlertError && <Alert message={"Oops! There was an error."}/>}
            </Box>

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

