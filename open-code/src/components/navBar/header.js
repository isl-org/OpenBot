import React, {useContext, useEffect, useState} from 'react';
import {ThemeContext} from "../../App"
import {useLocation, useNavigate} from "react-router-dom";
import styles from "./navbar.module.css";
import {Images} from "../../utils/images";
import {StoreContext} from "../../context/context";
import {auth, googleSignOut} from "../../services/firebase";
import {HelpCenterModal} from "../homeComponents/header/helpCenterModal";
import {EditProfileModal} from "../homeComponents/header/editProfileModal";
import {PopUpModal} from "../homeComponents/header/logOutAndDeleteModal";
import {ProfileOptionModal} from "../homeComponents/header/profileOptionModal";
import {PathName} from "../../utils/constants";
import {LogoSection, ProfileSignIn, ProjectName, ProjectNamePopUp} from "../homeComponents/header/headerComponents";
import {useTheme} from "@mui/material";
import useMediaQuery from "@mui/material/useMediaQuery";
import {deleteProjectFromStorage} from "../../services/workspace";


/**
 * Open-code's header which contains logo, project name on playground screen and help button, profile signIn
 * @returns {JSX.Element}
 * @constructor
 */
export function Header() {
    const {theme, toggleTheme} = useContext(ThemeContext);
    const {projectName, setProjectName, user, setUser} = useContext(StoreContext);
    const [anchorEl, setAnchorEl] = useState(null);
    const [deleteProject, setDeleteProject] = useState(false);
    const [isHelpCenterModal, setIsHelpCenterModal] = useState(false)
    const [isProfileModal, setIsProfileModal] = useState(false);
    const [isEditProfileModal, setIsEditProfileModal] = useState(false);
    const [isLogoutModal, setIsLogoutModal] = useState(false);
    const [open, setOpen] = useState(false);
    const location = useLocation();
    let navigate = useNavigate();

    useEffect(() => {
        auth.onAuthStateChanged(function (currentUser) {
            setUser({
                photoURL: currentUser?.photoURL,
                displayName: currentUser?.displayName,
                email: currentUser?.email,
            });
        })
    }, [isEditProfileModal,setUser])

    const handleClick = (event) => {
        setOpen(!open); // open and close popup
        setAnchorEl(anchorEl ? null : event.currentTarget); //popup event
    };

    //delete file from local and drive
    const handleDeleteProject = () => {
        deleteProjectFromStorage(projectName).then((res) => {
            navigate(PathName.home);
        });
    }

    const handleSignOut = () => {
        googleSignOut().then(() => {
            setIsLogoutModal(false);
        })
    }

    return (
        <div>
            {/*delete project modal*/}
            {deleteProject &&
                <PopUpModal setVariable={setDeleteProject}
                            headerText={"Delete this file?"}
                            containText={"You cannot restore this file later."}
                            buttonText={"Delete"}
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
                                  location={location}
                                  theme={theme} setIsProfileModal={setIsProfileModal} user={user} setUser={setUser}/>

                    {/* delete edit profile option popup*/}
                    {isProfileModal &&
                        <ProfileOptionModal
                            isProfileModal={isProfileModal}
                            setIsProfileModal={setIsProfileModal}
                            setIsEditProfileModal={setIsEditProfileModal}
                            setIsLogoutModal={setIsLogoutModal}
                            setIsHelpCenterModal={setIsHelpCenterModal}/>
                    }
                    {/*edit profile pop up */}
                    {isEditProfileModal &&
                        <EditProfileModal
                            isEditProfileModal={isEditProfileModal}
                            setIsEditProfileModal={setIsEditProfileModal}
                            user={user}/>
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
    const {setIsHelpCenterModal, toggleTheme, theme, setIsProfileModal, user, setUser, location} = params
    const themes = useTheme();
    const isMobile = useMediaQuery(themes.breakpoints.down('md'));
    return (
        <>
            {/*help icon if screen is playground and device is not mobile*/}
            {location.pathname === PathName.playGround && !isMobile &&
                <img className={styles.listStyle} alt={"helpCenter"} src={Images.helpIcon}
                     onClick={() => setIsHelpCenterModal(true)}
                     style={{height: 24}}/>
            }
            {/*if screen is playground, and it's mobile than do not show change theme icon and divider*/}
            {!(location.pathname === PathName.playGround && isMobile) &&
                <>
                    {/*change theme icon*/}
                    <img alt="icon" onClick={() => toggleTheme(!theme)}
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
