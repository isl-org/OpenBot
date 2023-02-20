import React, {useContext, useState, useRef, useEffect} from 'react';
import moon from "../../assets/images/icon/whiteMode/white-mode-icon.png";
import icon from "../../assets/images/icon/open-bot-logo.png"
import downArrow from "../../assets/images/icon/down-arrow.png"
import {NavbarStyle} from "./styles";
import {useNavigate} from "react-router-dom";
import {ThemeContext} from "../../App"
import WhiteText from "../fonts/whiteText";
import styles from "./navbar.module.css"
import {Box, Modal, Popper} from "@mui/material";
import BlueText from "../fonts/blueText";
import {Images} from "../../utils/images";
import {StoreContext} from "../../context/context";
import {useLocation} from 'react-router-dom';
import UpArrow from "../../assets/images/icon/up-arrow.png";
import Edit from "../../assets/images/icon/edit.png";
import trash from "../../assets/images/icon/trash.png";
import DeleteModel from "../../pages/profile/deleteModel";
import SimpleInputComponent from "../inputComponent/simpleInputComponent";
import BlueButton from "../buttonComponent/blueButtonComponent";
import BlackText from "../fonts/blackText";
import {HelpCenterText} from "../../utils/constants";
import {auth, signInWithGoogle, uploadProfilePic} from "../../firebase_setup/firebase";
import renameIcon from "../../assets/images/icon/rename-icon.png";
import deleteIcon from "../../assets/images/icon/delete-icon.png";
import {colors} from "../../utils/color";
// import {loadingWorkspace} from "../../firebase_setup/workspace";

export function Header() {
    const {theme, toggleTheme} = useContext(ThemeContext)
    const {projectName, setProjectName} = useContext(StoreContext);
    const [anchorEl, setAnchorEl] = useState(null);
    const [deleteProject, setDeleteProject] = useState(false);
    const [isHelpCenterModal, setIsHelpCenterModal] = useState(false)
    const handleClick = (event) => {
        setAnchorEl(anchorEl ? null : event.currentTarget)
    };
    const open = Boolean(anchorEl);
    const id = open ? 'simple-popper' : undefined
    let navigate = useNavigate()
    const [isProfileModal, setIsProfileModal] = useState(false)
    const [isEditProfileModal, setIsEditProfileModal] = useState(false)
    const [isLogoutModal, setIsLogoutModal] = useState(false)
    const [user, setUser] = useState(null);


    const anchorRef = React.useRef();
    useEffect(() => {
        setTimeout(() => setAnchorEl(anchorRef?.current), 1)
    }, [anchorRef])

    useEffect(() => {
        auth.onAuthStateChanged(function (currentUser) {
            // const userName = currentUser.displayName.split(" ");
            // setUserName(userName[0]);
            // setProfileIcon(currentUser.photoURL);
            setUser({
                photoURL: currentUser.photoURL,
                displayName: currentUser.displayName,
                email: currentUser.email,
            });
            currentUser.getIdToken().then((currentToken) => {
                if (getCookie("userToken") === currentToken) {
                    localStorage.setItem("isSigIn", "true")
                } else {
                    localStorage.setItem("isSigIn", "false")
                }
            }).catch((e) => {
                console.log(e);
            })
        })

    }, [])

    const openHomepage = () => {

        let path = `/`;
        navigate(path);
    }
    const location = useLocation();
    const handleDelete = () => {
        setDeleteProject(true)
    }


    async function storeIdTokenInCookie(user) {
        if (!auth.currentUser) {
            console.error('No user is signed in');
            return;
        }
        const idToken = await user.getIdToken();
        const now = new Date();
        const expiration = new Date(now.getTime() + 1000 * 60 * 60 * 24 * 7);
        document.cookie = `userToken=${idToken}; expires=${expiration.toUTCString()}; path=/;`;
    }

    const getCookie = (name) => {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) {
            return parts.pop().split(";").shift();
        }
    };

    return (
        <div>
            {deleteProject && <DeleteModel setDeleteProject={setDeleteProject}/>}
            <div style={NavbarStyle.navbarDiv}>
                <div style={NavbarStyle.navbarTitleDiv}>
                    <img alt="" style={{...NavbarStyle.mainIcon, ...NavbarStyle.iconMargin}} src={icon} onClick={() => {
                        openHomepage()
                    }}/>
                    <span onClick={() => {
                        openHomepage()
                    }} style={{...NavbarStyle.mainTitle, ...NavbarStyle.iconMargin}}>OpenCode</span>
                </div>

                {location.pathname === "/playground" ? !anchorEl ?
                        <div style={NavbarStyle.playgroundName} onClick={handleClick}>
                    <span
                        style={{...NavbarStyle.mainTitle, ...NavbarStyle.arrowMargin}}>{projectName}</span>
                            <img src={downArrow}
                                 style={{...NavbarStyle.infoIcon, ...NavbarStyle.arrowMargin}}
                                 alt={"arrow"}/>
                        </div>
                        :
                        <>
                            <div style={NavbarStyle.playgroundName}>
                                <input type="text" className={styles.Edit}
                                       id="userEdit"
                                       onChange={(e) => setProjectName(e.target.value)}
                                       style={{width: `${projectName.length}ch`}}
                                       value={projectName}
                                />
                                <img src={UpArrow}
                                     style={{...NavbarStyle.infoIcon, ...NavbarStyle.arrowMargin}}
                                     onClick={handleClick} alt={"arrow"}/>
                            </div>
                            <Popper key={id} open={open} anchorEl={anchorEl}>
                                <div
                                    className={styles.option + " " + (theme === "dark" ? styles.darkTitleModel : styles.lightTitleModel)}>
                                    <div
                                        className={`${styles.item} ${styles.renameDivMargin}  ${(theme === "dark" ? styles.darkItem : styles.lightItem)}`}
                                        onClick={handleClick}>
                                        <img alt="Icon" className={styles.icon} src={theme === "dark" ? renameIcon : Edit}/>
                                        <div>Rename</div>
                                    </div>
                                    <div
                                        className={`${styles.item} ${styles.deleteDivMargin} ${(theme === "dark" ? styles.darkItem : styles.lightItem)}`}
                                        onClick={handleDelete}>
                                        <img alt="Icon" className={styles.icon}
                                             src={theme === "dark" ? deleteIcon : trash}/>
                                        <div> Delete File</div>
                                    </div>
                                </div>
                            </Popper>
                        </>
                    : ""}
                <div style={NavbarStyle.navbarIconDiv}>
                    {location.pathname === "/playground" ?
                        <img className={styles.listStyle} alt={"helpCenter"} src={Images.helpIcon} onClick={() => {
                            setIsHelpCenterModal(true)
                        }} style={{height: 24}}/> : ""}
                    <img alt="" onClick={() => toggleTheme(!theme)} src={moon}
                         style={{...NavbarStyle.moonIcon, ...NavbarStyle.iconMargin}}/>
                    <img alt="" src={Images.line} style={{...NavbarStyle.lineIcon, ...NavbarStyle.iconMargin}}/>
                    {
                        localStorage.getItem("isSigIn") === "true" ?
                            <div onClick={() => setIsProfileModal(true)} className={styles.profileDiv}>
                                {user?.photoURL && <img alt="Profile Icon" src={user.photoURL}
                                                        style={{height: 28, width: 28, borderRadius: 90}}/>}
                                <WhiteText extraStyle={styles.extraStyles} text={user?.displayName.split(" ")[0]}/>
                                <img alt="arrow button" src={downArrow} style={{height: 20, width: 20}}/>
                            </div> :
                            <button onClick={() => {
                                const userToken = getCookie("userToken");
                                console.log("userToken is :", userToken)
                                signInWithGoogle().then((response) => {
                                    localStorage.setItem("isSigIn", "true")
                                    setUser({
                                        photoURL: response.user.photoURL,
                                        displayName: response.user.displayName,
                                        email: response.user.email
                                    });
                                    storeIdTokenInCookie(response.user).then(() => {
                                        // return loadingWorkspace();
                                    });
                                }
                                ).catch((error) => {
                                    console.log(error)
                                })
                            }
                            }

                                    style={{...NavbarStyle.buttonIcon, ...NavbarStyle.iconMargin}}><span>Sign in</span>
                            </button>
                    }
                    {
                        isProfileModal && <ProfileOptionModal isProfileModal={isProfileModal}
                                                              setIsProfileModal={setIsProfileModal}
                                                              setIsEditProfileModal={setIsEditProfileModal}
                                                              setIsLogoutModal={setIsLogoutModal}
                                                              setIsHelpCenterModal={setIsHelpCenterModal}/>
                    }
                    {
                        isEditProfileModal && <EditProfileModal isEditProfileModal={isEditProfileModal}
                                                                setIsEditProfileModal={setIsEditProfileModal}
                                                                user={user}/>
                    }
                    {
                        isLogoutModal && <LogoutModal isLogoutModal={isLogoutModal}
                                                      setIsLogoutModal={setIsLogoutModal}/>
                    }
                    {
                        isHelpCenterModal && <HelpCenterModal isHelpCenterModal={isHelpCenterModal}
                                                              setIsHelpCenterModal={setIsHelpCenterModal}/>
                    }
                </div>
            </div>
        </div>
    )
        ;
}

export function ProfileOptionModal(props) {
    const theme = useContext(ThemeContext);
    const {
        isProfileModal,
        setIsProfileModal,
        setIsEditProfileModal,
        setIsLogoutModal,
    } = props
    const handleClose = () => {
        setIsProfileModal(false)
    }
    return (
        <Modal
            open={isProfileModal}
            onClose={handleClose}>
            <Box className={styles.profileOptionModalStyle}
                 style={{backgroundColor: theme.theme === "dark" ? colors.blackPopupBackground : colors.whiteBackground}}>
                <div onClick={() => {
                    setIsEditProfileModal(true)
                    handleClose()
                }}
                     className={`${styles.item} ${styles.editProfileMargin}  ${(theme.theme === "dark" ? styles.darkItem : styles.lightItem)}`}>
                    <img alt="icon" src={theme.theme === "dark" ? Images.darkUserIcon : Images.userIcon}
                         className={styles.modalIcon}/>
                    {theme.theme === "dark" ?
                        <WhiteText extraStyle={styles.modalText} styles text={"Edit Profile"}/> :
                        <BlueText extraStyle={styles.modalText} styles text={"Edit Profile"}/>
                    }
                </div>

                <div onClick={() => {
                    setIsLogoutModal(true)
                    handleClose()
                }}
                     className={`${styles.item} ${styles.logOutMargin} ${(theme.theme === "dark" ? styles.darkItem : styles.lightItem)}`}>
                    <img alt="icon" src={theme.theme === "dark" ? Images.darkLogoutIcon : Images.logoutIcon}
                         className={styles.modalIcon}/>
                    {theme.theme === "dark" ?
                        <WhiteText extraStyle={styles.modalText} text={"Logout"}/> :
                        <BlueText extraStyle={styles.modalText} text={"Logout"}/>
                    }
                </div>
            </Box>
        </Modal>
    )
}

export function EditProfileModal(props) {
    const {isEditProfileModal, setIsEditProfileModal, user} = props
    const [file, setFile] = useState(user?.photoURL ? user.photoURL : Images.profileImage);
    const inputRef = useRef();
    const [fullName, setFullName] = useState(user?.displayName);
    const [userDetails, setUserDetail] = useState({
        displayName: user?.displayName,
        email: user?.email,
        photoUrl: user?.photoURL
    })
    const {theme} = useContext(ThemeContext)
    const handleClose = () => {
        setIsEditProfileModal(false)
    }

    function handleChange(e) {
        console.log(e.target.files[0].name.replace(/HEIC/g, 'jpg'));
        // setFile(URL.createObjectURL(e.target.files[0]));
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

    return (
        <Modal
            open={isEditProfileModal}
            style={{display: "flex", alignItems: "center", justifyContent: "center"}}>
            <Box className={styles.editProfileModal + " " + (theme === "dark" && styles.darkEditProfileModal)}>
                <div className={styles.crossIconDiv}>
                    {
                        theme === "dark" ?
                            <img onClick={handleClose} alt={"cross icon"} className={styles.crossIcon}
                                 src={Images.darkCrossIcon}/> :
                            <img onClick={handleClose} alt={"cross icon"} className={styles.crossIcon}
                                 src={Images.lightCrossIcon}/>
                    }
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
                    <BlueButton onClick={async () => {
                        await uploadProfilePic(file, file.name).then((photoURL) => {
                            auth.currentUser.updateProfile({
                                displayName: userDetails.displayName,
                                photoURL: photoURL
                            }).then(() => handleClose())
                        })
                    }} buttonType={"contained"} buttonName={"Save"}/>
                    <BlueButton onClick={() => {
                        handleClose()
                    }} buttonName={"Cancel"}/>
                </div>
            </Box>
        </Modal>
    )
}

export function LogoutModal(props) {
    const {setIsLogoutModal, isLogoutModal} = props
    const {theme} = useContext(ThemeContext)
    const handleClose = () => {
        setIsLogoutModal(false)
    }
    return (
        <Modal
            className={styles.logoutModal}
            open={isLogoutModal}
            onClose={() => handleClose()}>
            <Box
                className={styles.logoutModalBox + " " + (theme === "dark" ? styles.darkLogoutModalBox : styles.lightLogoutModalBox)}>
                <BlackText text={"Confirm Logout"}/>
                <div style={{marginTop: 20}}>
                    <BlackText
                        extraStyle={(theme === "dark" ? styles.darkLogoutMessageModal : styles.lightLogoutMessageModal)}
                        text={"Are you sure you want to logout?"}/>
                </div>
                <div className={styles.logoutButtonsDiv}>
                    <BlueButton onClick={handleClose} buttonName={"Cancel"}
                                extraStyle={styles.logoutButtonsExtraStyle}/>
                    <BlueButton buttonType={"contained"} buttonName={"Ok"} extraStyle={styles.logoutButtonsExtraStyle}/>
                </div>
            </Box>
        </Modal>
    )
}

export function HelpCenterModal(props) {

    const {isHelpCenterModal, setIsHelpCenterModal} = props
    const theme = useContext(ThemeContext)
    const handleClose = () => {
        setIsHelpCenterModal(false)
    }
    return (
        <Modal
            style={{display: "flex", alignItems: "center", justifyContent: "center"}}
            open={isHelpCenterModal}>
            <Box
                className={styles.helpCenterModalBox + " " + (theme.theme === "dark" ? styles.darkHelpCenterModal : styles.lightHelpCenterModal)}>
                <div className={styles.dragAndDropDiv + " " + (theme.theme === "dark" && styles.darkModeRightBorder)}>
                    <img alt={"screen display"} src={Images.laptop} style={{width: "80%", marginTop: "15%"}}/>
                    <div style={{width: "80%", marginTop: "15%"}}>
                        <BlackText extraStyle={styles.integerNumber} text={1}/>
                        <div style={{marginLeft: "10%", paddingTop: "8%"}}>
                            <BlackText divStyle={{marginBottom: "12%"}} text={"Drag and Drop"}/>
                            <BlackText extraStyle={styles.helpCenterPara} text={HelpCenterText.dragAndDropFirstLine}/>
                            <BlackText extraStyle={styles.helpCenterPara} text={HelpCenterText.dragAndDropSecondLine}/>
                        </div>
                    </div>
                </div>
                <div>
                    <div
                        className={styles.saveAndDownloadDiv + " " + (theme.theme === "dark" && styles.darkModeBottomBorder)}>
                        <div className={styles.helpCenterCrossIconDiv}>
                            {
                                theme.theme === "dark" ?
                                    <img onClick={handleClose} alt={"cross icon"} className={styles.crossIcon}
                                         src={Images.darkCrossIcon}/> :
                                    <img onClick={handleClose} alt={"cross icon"} className={styles.crossIcon}
                                         src={Images.crossIcon}/>
                            }
                        </div>
                        <div>
                            <BlackText text={2} extraStyle={styles.integerNumber}/>
                        </div>
                        <div style={{width: "70%", paddingLeft: "5%"}}>
                            <BlackText divStyle={{marginTop: "7%", marginBottom: "8%"}} text={"Save and Download"}/>
                            <BlackText extraStyle={styles.helpCenterPara}
                                       text={HelpCenterText.saveAndDownloadFirstLine}/>
                            <BlackText extraStyle={styles.helpCenterPara}
                                       text={HelpCenterText.saveAndDownloadSecondLine}/>
                        </div>
                    </div>
                    <div className={styles.connectAndDriveDiv}>
                        <div>
                            <BlackText text={3} extraStyle={styles.integerNumber}/>
                        </div>
                        <div style={{width: "70%", paddingLeft: "5%"}}>
                            <div style={{marginTop: "10%"}}>
                                <BlackText divStyle={{paddingTop: "5%", marginBottom: "8%"}}
                                           text={"Connect and Drive"}/>
                                <BlackText extraStyle={styles.helpCenterPara}
                                           text={HelpCenterText.connectAndDriveFirstLine}/>
                                <BlackText extraStyle={styles.helpCenterPara}
                                           text={HelpCenterText.connectAndDriveSecondLine}/>
                            </div>
                        </div>
                    </div>
                </div>
            </Box>
        </Modal>
    )
}



