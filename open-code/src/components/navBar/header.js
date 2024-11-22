import React, {useContext, useEffect, useState} from 'react';
import {ThemeContext} from "../../App"
import {useLocation, useNavigate} from "react-router-dom";
import styles from "./navbar.module.css";
import {Images} from "../../utils/images";
import {StoreContext} from "../../context/context";
import {auth, googleSigIn, googleSignOut} from "../../services/firebase";
import {HelpCenterModal} from "../homeComponents/header/helpCenterModal";
import {EditProfileModal} from "../homeComponents/header/editProfileModal";
import {PopUpModal} from "../homeComponents/header/logOutAndDeleteModal";
import {ProfileOptionModal} from "../homeComponents/header/profileOptionModal";
import {Constants, errorToast, localStorageKeys, PathName} from "../../utils/constants";
import {LogoSection, ProfileSignIn, ProjectName, ProjectNamePopUp} from "../homeComponents/header/headerComponents";
import {Backdrop, CircularProgress, useTheme} from "@mui/material";
import useMediaQuery from "@mui/material/useMediaQuery";
import {autoSync, deleteProjectFromStorage} from "../../services/workspace";
import {colors} from "../../utils/color";
import {signOut} from "firebase/auth";
import configData from "../../config.json";

/**
 * Open-code's header which contains logo, project name on playground screen and help button, profile signIn
 * @returns {JSX.Element}
 * @constructor
 */
export function Header() {
    const {theme, toggleTheme} = useContext(ThemeContext);
    const {
        projectName,
        setProjectName,
        user,
        setUser,
        isDob,
        isOnline,
        isSessionExpireModal,
        setIsSessionExpireModal,
        setTimeoutId
    } = useContext(StoreContext);
    const [anchorEl, setAnchorEl] = useState(null);
    const [deleteProject, setDeleteProject] = useState(false);
    const [isHelpCenterModal, setIsHelpCenterModal] = useState(false)
    const [isProfileModal, setIsProfileModal] = useState(false);
    const [isEditProfileModal, setIsEditProfileModal] = useState(false);
    const [isLogoutModal, setIsLogoutModal] = useState(false);
    const [open, setOpen] = useState(false);
    const [deleteLoader, setDeleteLoader] = useState(false);
    const [editProfileLoaderOpen, setEditProfileLoaderOpen] = useState(false);
    const [isDobChanged, setIsDobChanged] = useState(false);
    const [isAutoSync, setIsAutoSync] = useState(false);
    const location = useLocation();
    let navigate = useNavigate();

    //handle click event for opening rename popup
    const handleClick = (event) => {
        setOpen(!open); // open and close popup
        setAnchorEl(anchorEl ? null : event.currentTarget); //popup event
    };

    //delete file from local and drive
    const handleDeleteProject = () => {
        setDeleteLoader(true);
        deleteProjectFromStorage(projectName).then((res) => {
            navigate(PathName.home);
        });
    }

    //Handling sign-out functionality
    const handleSignOut = () => {
        googleSignOut().then(() => {
            setIsLogoutModal(false);
        })
    }

    //function to handle sign in
    const handleSignIn = () => {
        if (isOnline) {
            signOut(auth).then(() => {
                localStorage.setItem("isSigIn", "false")
                localStorage.setItem(localStorageKeys.accessToken, " ");
                localStorage.setItem(localStorageKeys.configData, JSON.stringify(configData));
                googleSigIn().then(response => {
                    setTimeoutId(true);
                    setIsSessionExpireModal(false);
                    setUser({
                        photoURL: response?.user.photoURL,
                        displayName: response?.user.displayName,
                        email: response?.user.email
                    });
                    window.location.reload()
                }).catch((error) => {
                    console.log("signIn error: ", error)
                    setIsSessionExpireModal(false);
                });
            }).catch((error) => {
                console.log("Sign-out error ", error)
            });

        } else {
            errorToast(Constants.InternetOffMsg)
        }
    }

    //Loader while getting date of birth
    function SimpleBackdrop() {
        return (
            <div>
                <Backdrop
                    sx={{color: colors.openBotBlue, zIndex: (theme) => theme.zIndex.drawer + 1}}
                    open={editProfileLoaderOpen}
                >
                    <CircularProgress color="inherit"/>
                </Backdrop>
            </div>
        );
    }

    useEffect(() => {
        auth.onAuthStateChanged(function (currentUser) {
            setUser({
                photoURL: currentUser?.photoURL,
                displayName: currentUser?.displayName,
                email: currentUser?.email,
            });
        })
    }, [isEditProfileModal, setUser])

    return (
        <div>
            {/*delete project modal*/}
            {deleteProject &&
                <PopUpModal setVariable={setDeleteProject}
                            inlineStyle={{backgroundColor: "#E03E1A"}}
                            headerText={"Delete this file?"}
                            containText={"You cannot restore this file later."}
                            buttonText={"Delete"}
                            deleteLoader={deleteLoader}
                            setInlineStyling={true}
                            handleButtonClick={handleDeleteProject}/>
            }

            <div className={styles.navbarDiv}>
                {/*logo*/}
                <LogoSection/>

                {/*project name on center when screen is playground*/}
                <ProjectNameSection anchorEl={anchorEl} setProjectName={setProjectName}
                                    setOpen={setOpen} open={open}
                                    handleClick={handleClick} projectName={projectName}
                                    setDeleteProject={setDeleteProject} theme={theme}/>

                <div className={styles.navbarIconDiv}>
                    <RightSection setIsHelpCenterModal={setIsHelpCenterModal} toggleTheme={toggleTheme}
                                  location={location} isOnline={isOnline} isAutoSync={isAutoSync}
                                  setIsAutoSync={setIsAutoSync}
                                  theme={theme} setIsProfileModal={setIsProfileModal} user={user} setUser={setUser}/>

                    {/* delete edit profile option popup*/}
                    {isProfileModal &&
                        <ProfileOptionModal
                            isProfileModal={isProfileModal}
                            setIsProfileModal={setIsProfileModal}
                            setIsEditProfileModal={setIsEditProfileModal}
                            setIsLogoutModal={setIsLogoutModal}
                            setEditProfileLoaderOpen={setEditProfileLoaderOpen}
                            isAutoSync={isAutoSync} setIsAutoSync={setIsAutoSync}
                            setIsHelpCenterModal={setIsHelpCenterModal} isDobChanged={isDobChanged}/>
                    }
                    {/*edit profile pop up */}
                    {editProfileLoaderOpen && <SimpleBackdrop/>}
                    {isEditProfileModal &&
                        <EditProfileModal
                            isEditProfileModal={isEditProfileModal}
                            setIsEditProfileModal={setIsEditProfileModal}
                            user={user} isDob={isDob} setIsDobChanged={setIsDobChanged}/>
                    }
                    {/*log out pop up*/}
                    {isLogoutModal &&
                        <PopUpModal setVariable={setIsLogoutModal}
                                    headerText={"Confirm Logout"}
                                    containText={"Are you sure you want to logout?"}
                                    buttonText={"Ok"}
                                    handleButtonClick={handleSignOut}/>
                    }
                    {/*help icon pop up*/}
                    {isHelpCenterModal && <HelpCenterModal isHelpCenterModal={isHelpCenterModal}
                                                           setIsHelpCenterModal={setIsHelpCenterModal}/>}
                    {/*Session expire pop up*/}
                    {isSessionExpireModal && <PopUpModal setVariable={setIsSessionExpireModal}
                                                         headerText={"Session Expired"}
                                                         containText={"Your session has expired.Please login again to continue."}
                                                         buttonText={"Login"}
                                                         handleButtonClick={handleSignIn}/>}
                </div>
            </div>
        </div>
    );
}


/**
 * Right Component which has help icon ,signIn, profile picture
 * @param params
 * @returns {JSX.Element}
 * @constructor
 */
function RightSection(params) {
    const {
        setIsHelpCenterModal,
        toggleTheme,
        theme,
        setIsProfileModal,
        user,
        setUser,
        location,
        isOnline,
        isAutoSync,
        setIsAutoSync
    } = params
    const themes = useTheme();
    const isMobile = useMediaQuery(themes.breakpoints.down('sm'));
    const tabletQuery = window.matchMedia("(min-width: 768px) and (max-width: 1024px)").matches;
    const isMobileLandscape = window.matchMedia("(max-height:440px) and (max-width: 1000px) and (orientation: landscape)").matches
    const isSignedIn = localStorage.getItem("isSigIn") === "true";
    const {setIsAutoSyncEnabled} = useContext(StoreContext);
    return (
        <>
            {location.pathname === PathName.playGround && isSignedIn && !isMobile && !tabletQuery && !isMobileLandscape &&
                <img title={"Auto Sync"} className={`${styles.listStyle} ${isAutoSync && styles.sync}`} alt={"syncIcon"}
                     src={Images.darkSyncIcon}
                     onClick={async () => {
                         if (isOnline) {
                             if (localStorage.getItem("isSigIn") === "true") {
                                 setIsAutoSync(true)
                                 await autoSync().then(() => {
                                     setIsAutoSyncEnabled(true);
                                     setIsAutoSync(false);
                                 })
                                     .catch((e) => {
                                         errorToast("Something went wrong!")
                                         setIsAutoSync(false);
                                     })
                             } else {
                                 errorToast("Please sign-In to auto sync.")
                             }
                         } else {
                             errorToast(Constants.InternetOffMsg)
                         }
                     }
                     }
                     style={{height: 24}}/>
            }
            {/*help icon if screen is playground and device is not mobile*/}
            {location.pathname === PathName.playGround && !isMobile && !tabletQuery && !isMobileLandscape &&
                <img className={styles.listStyle} alt={"helpCenter"} src={Images.helpIcon}
                     onClick={() => setIsHelpCenterModal(true)}
                     style={{height: 24}} title={"Help"}/>
            }
            {/*if screen is playground, and it's mobile than do not show change theme icon and divider*/}
            {!(location.pathname === PathName.playGround && isMobile) &&
                <>
                    {/*change theme icon*/}
                    <img title={"Theme"} alt="icon" onClick={() => toggleTheme(!theme)}
                         src={theme === "dark" ? Images.lightThemeIcon : Images.darkThemeIcon}
                         className={`${theme === "dark" ? styles.lightThemeIcon : styles.darkThemeIcon} ${styles.iconMargin}`}/>
                    {/*divider*/}
                    <img alt="icon" src={Images.line} className={`${styles.lineIcon} ${styles.iconMargin}`}/>
                </>
            }
            {/*if signed in then show icon and name or else sign in option*/}
            <ProfileSignIn setIsProfileModal={setIsProfileModal} user={user} setUser={setUser}/>
        </>
    )
}


/**
 * when screen is playground will show project name on center of the header
 * @param params
 * @returns {JSX.Element | string}
 * @constructor
 */
function ProjectNameSection(params) {
    const {anchorEl, handleClick, projectName, open, setOpen, setDeleteProject, theme, setProjectName} = params
    const location = useLocation();
    return (
        //when screen is playground then show project name and if clicked on project name then show projectName with popUp
        location.pathname === PathName.playGround ? !open ?
                <ProjectName handleClick={handleClick} projectName={projectName}/>
                :
                <ProjectNamePopUp anchorEl={anchorEl} setOpen={setOpen} setProjectName={setProjectName}
                                  handleClick={handleClick} projectName={projectName} open={open}
                                  setDeleteProject={setDeleteProject} theme={theme}/>
            : ""
    )
}
