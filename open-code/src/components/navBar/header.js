import React, {useContext, useEffect, useState} from 'react';
import moon from "../../assets/images/icon/whiteMode/white-mode-icon.png";
import {ThemeContext} from "../../App"
import {useLocation, useNavigate} from "react-router-dom";
import styles from "./navbar.module.css";
import {Images} from "../../utils/images";
import {StoreContext} from "../../context/context";
import {auth} from "../../services/firebase";
import {HelpCenterModal} from "../homeComponents/header/helpCenterModal";
import {EditProfileModal} from "../homeComponents/header/editProfileModal";
import {DeleteModel, LogOutModal} from "../homeComponents/header/logOutAndDeleteModal";
import {ProfileOptionModal} from "../homeComponents/header/profileOptionModal";
import {localStorageKeys, PathName} from "../../utils/constants";
import {LogoSection, ProfileSignIn, ProjectName, ProjectNamePopUp} from "../homeComponents/header/headerComponents";

/**
 * Open-code's header which contains logo, project name on playground screen and help button, profile signIn
 * @returns {JSX.Element}
 * @constructor
 */
export function Header() {
    const {theme, toggleTheme} = useContext(ThemeContext);
    const {projectName, setProjectName} = useContext(StoreContext);
    const [anchorEl, setAnchorEl] = useState(null);
    const [deleteProject, setDeleteProject] = useState(false);
    const [isHelpCenterModal, setIsHelpCenterModal] = useState(false)
    const [isProfileModal, setIsProfileModal] = useState(false);
    const [isEditProfileModal, setIsEditProfileModal] = useState(false);
    const [isLogoutModal, setIsLogoutModal] = useState(false);
    const [user, setUser] = useState();
    const location = useLocation();

    useEffect(() => {
        auth.onAuthStateChanged(function (currentUser) {
            setUser({
                photoURL: currentUser?.photoURL,
                displayName: currentUser?.displayName,
                email: currentUser?.email,
            });
        })
    }, [isEditProfileModal])

    //on click event on arrow when clicked set project name.
    const handleClick = (event) => {
        setAnchorEl(anchorEl ? null : event.currentTarget)
    };

    return (
        <div>
            {/*delete project modal*/}
            {deleteProject && <DeleteModel setDeleteProject={setDeleteProject}/>}

            <div className={styles.navbarDiv}>
                {/*logo*/}
                <LogoSection/>

                {/*project name on center when screen is playground*/}
                <ProjectNameSection anchorEl={anchorEl} setProjectName={setProjectName}
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
                    {isLogoutModal && <LogOutModal isLogoutModal={isLogoutModal} setIsLogoutModal={setIsLogoutModal}/>}
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
    return (
        <>
            {/*help icon if screen is playground*/}
            {location.pathname === PathName.playGround &&
                <img className={styles.listStyle} alt={"helpCenter"} src={Images.helpIcon}
                     onClick={() => {
                         setIsHelpCenterModal(true)
                     }}
                     style={{height: 24}}/>
            }

            {/*change theme icon*/}
            <img alt="" onClick={() => toggleTheme(!theme)} src={moon}
                 className={`${styles.moonIcon} ${styles.iconMargin}`}/>

            {/*divider*/}
            <img alt="" src={Images.line} className={`${styles.lineIcon} ${styles.iconMargin}`}/>

            {/*if signed in then show icon and name or else sign in option*/}
            <ProfileSignIn setIsProfileModal={setIsProfileModal} user={user} setUser={setUser}/>
        </>
    )
}


/**
 * when screen is playground will show project name on center of the header
 * @param params
 * @returns {JSX.Element|string}
 * @constructor
 */
function ProjectNameSection(params) {
    const {anchorEl, handleClick, projectName, setDeleteProject, theme, setProjectName} = params
    const location = useLocation();
    return (
        location.pathname === PathName.playGround ? !anchorEl ?
                <ProjectName handleClick={handleClick} projectName={projectName}/>
                :
                <ProjectNamePopUp anchorEl={anchorEl} setProjectName={setProjectName}
                                  handleClick={handleClick} projectName={projectName}
                                  setDeleteProject={setDeleteProject} theme={theme}/>
            : ""
    )
}
