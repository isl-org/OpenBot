import {useLocation, useNavigate} from "react-router-dom";
import styles from "../../navBar/navbar.module.css";
import icon from "../../../assets/images/icon/OBplaygroundLogo.png";
import {Popper, useTheme} from "@mui/material";
import renameIcon from "../../../assets/images/icon/rename-icon.png";
import Edit from "../../../assets/images/icon/edit.png";
import deleteIcon from "../../../assets/images/icon/delete-icon.png";
import trash from "../../../assets/images/icon/trash.png";
import LoaderComponent from "../../loader/loaderComponent";
import WhiteText from "../../fonts/whiteText";
import {googleSigIn} from "../../../services/firebase";
import React, {useContext, useEffect, useRef, useState} from "react";
import {renameProject} from "../../../services/workspace";
import {Constants, errorToast, PathName} from "../../../utils/constants";
import {handleRename} from "../myProjects/card";
import useMediaQuery from "@mui/material/useMediaQuery";
import {Images} from "../../../utils/images";
import {StoreContext} from "../../../context/context";

/**
 * Display logo with openCode text (Header's Left side)
 * @returns {JSX.Element}
 * @constructor
 */
export function LogoSection() {
    let navigate = useNavigate()

    //onClickEvent
    const openHomepage = () => {
        navigate(PathName.home);
    }

    return (
        <div className={styles.navbarTitleDiv}>
            {/*icon*/}
            <img alt="openBotIcon" className={`${styles.mainIcon} ${styles.iconMargin}`} src={icon}
                 onClick={() => openHomepage()}/>
            {/*name*/}
            <div className={styles.navbarHeadDiv} onClick={() => openHomepage()}>
                <span className={`${styles.mainTitle} `}>OpenBot</span>
                <span className={`${styles.mainTitle} ${styles.subTitle}`}>PlayGround</span>
            </div>

        </div>
    )
}

/**
 * Project Name with Arrow
 * @param params
 * @returns {JSX.Element}
 * @constructor
 */
export function ProjectName(params) {
    const {projectName, handleClick} = params
    const themes = useTheme();
    const isMobile = useMediaQuery(themes.breakpoints.down('md'));

    return (
        <div className={styles.playgroundName} onClick={handleClick}>
            <span
                className={`${styles.mainTitle} ${styles.arrowMargin}`}>{projectName?.slice(0, isMobile ? 8 : 15) + " " + ((projectName?.length > (isMobile ? 8 : 15)) ? "..." : "")}</span>
            <img src={Images.downArrowIcon}
                 className={`${styles.infoIcon} ${styles.arrowMargin}`}
                 alt={"arrow"}/>
        </div>
    )
}

/**
 * This component renders the pop-up for renaming and deleting a project
 * @param {Object} params - The component parameters
 * @param {Object} params.anchorEl - The anchor element for the pop-up
 * @param {Function} params.setDeleteProject - The function to delete a project
 * @param {Object} params.theme - The current theme
 * @param {string} params.projectName - The name of the project
 * @param {Function} params.setOpen - The function to open the pop-up
 * @param {boolean} params.open - Whether or not the pop-up is open
 * @param {Function} params.setProjectName - The function to set the project name
 * @returns {JSX.Element}
 * @constructor
 */
export function ProjectNamePopUp(params) {
    const {anchorEl, setDeleteProject, theme, projectName, setOpen, open, setProjectName} = params
    const [rename, setRename] = useState(false);
    const [reNameProject, setRenameProject] = useState("")
    const [openPopUp, setOpenPopUp] = useState(true);
    const inputRef = useRef(null);
    const themes = useTheme();
    const isMobile = useMediaQuery(themes.breakpoints.down('md'));

    // Update rename project state variable when the project name changes
    useEffect(() => {
        setRenameProject(projectName);
    }, [projectName]);

    // Define the function to delete a project
    const handleDelete = () => {
        setDeleteProject(true);
    };

    // Handle the blur event on the input element
    const handleBlur = async () => {
        setOpen(false);
        handleClickBlur().then();
    };

    // Handle a click outside the pop-up
    const handleClickOutside = () => {
        setOpenPopUp(false);
        if (!rename) setOpen(false);
    };

    // Handle a blur event on the input element
    const handleClickBlur = async () => {
        setRename(false);
        if (!reNameProject || reNameProject.length <= 0) {
            setRenameProject(projectName);
        }
        if (reNameProject !== projectName && !reNameProject?.trim().length <= 0) {
            await handleRename(reNameProject, projectName, setRenameProject).then(
                async (updatedProjectName) => {
                    setProjectName(updatedProjectName);
                    await renameProject(
                        updatedProjectName,
                        projectName,
                        PathName.playGround
                    ).then();
                }
            );
        }
    };

    return (
        <div className={styles.playgroundName}>
            {/*project name with edit field and arrow*/}
            {rename ?
                <input type="text" className={styles.Edit}
                       id="userEdit"
                       onClick={(e) => {
                           e.stopPropagation();
                       }}
                       ref={inputRef}  // set the ref to the input element
                       onChange={(e) => setRenameProject(e.target.value)}
                       onFocus={(e) => e.target.select()}
                       onBlur={handleBlur}
                       onKeyDown={async (e) => {
                           if (e.keyCode === 13) {
                               handleBlur().then()
                           }
                       }}
                       style={{width: `${reNameProject?.length}ch`}}
                       value={reNameProject}
                />
                :
                <span onClick={() => {
                    setOpen(!open)
                }}
                     className={`${styles.mainTitle} ${styles.arrowMargin}`}>{projectName?.slice(0, isMobile ? 8 : 15) + " " + ((projectName?.length > (isMobile ? 8 : 15)) ? "..." : "")}</span>

            }
            <img src={Images.UpArrowIcon}
                 className={`${styles.infoIcon} ${styles.arrowMargin}`}
                 alt={"arrow"}/>
            {openPopUp &&
                <EditProjectPopUp open={openPopUp}
                                  inputRef={inputRef}
                                  anchorEl={anchorEl}
                                  rename={rename}
                                  projectName={reNameProject}
                                  setOpen={setOpenPopUp}
                                  setRename={setRename}
                                  clickOutside={handleClickOutside}
                                  handleDelete={handleDelete}
                                  handleRename={() => {
                                  }}
                                  theme={theme}/>}
        </div>
    )
}

/**
 * EditProjectPopUp is a popup component that shows options to rename or delete a project file.
 * @param params
 * @returns {JSX.Element}
 * @constructor
 */
export function EditProjectPopUp(params) {
    const {
        open,
        anchorEl,
        setRename,
        handleDelete,
        theme,
        extraStyle,
        clickOutside,
        inputRef,
        handleRename
    } = params
    const id = open ? 'simple-popper' : undefined // Define the ID of the popup element.
    const popUpRef = useRef(null);// Create a reference to the popup element for detecting clicks outside the popup.
    const location = useLocation();

    // Use the useEffect hook to detect clicks outside the popup.
    useEffect(() => {
        const handleClickOutside = (event) => {
            if (popUpRef.current && !popUpRef.current.contains(event.target)) {
                clickOutside()
            }
        };
        document.addEventListener("mousedown", handleClickOutside, {passive: true});
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, [popUpRef, clickOutside]);

    return (
        <Popper
            placement="bottom-start"
            ref={popUpRef}
            key={id} open={open} anchorEl={anchorEl}>
            <div
                className={styles.option + " " + (theme === "dark" ? styles.darkTitleModel : styles.lightTitleModel) + " " + extraStyle}
                style={{marginTop: (location.pathname === PathName.home) && 10}}>
                <div
                    className={`${styles.item} ${styles.renameDivMargin}  ${(theme === "dark" ? styles.darkItem : styles.lightItem)}`}
                    onClick={async (event) => {
                        event.stopPropagation();
                        setRename(true);
                        setTimeout(() => {
                            inputRef.current.focus(); // set focus to the input element when rename is true
                        }, 0);
                        handleRename();
                    }}>
                    <img alt="Icon" className={styles.icon} src={theme === "dark" ? renameIcon : Edit}/>
                    <div className={styles.renameText}>Rename</div>
                </div>
                <div
                    className={`${styles.item} ${styles.deleteDivMargin} ${(theme === "dark" ? styles.darkItem : styles.lightItem)}`}
                    onClick={handleDelete}>
                    <img alt="Icon" className={styles.icon}
                         src={theme === "dark" ? deleteIcon : trash}/>
                    <div className={styles.deleteText}> Delete File</div>
                </div>
            </div>
        </Popper>

    )
}

/**
 * Profile signIn option if not signed in or else show profile icon with name
 * @param params
 * @returns {JSX.Element}
 * @constructor
 */
export function ProfileSignIn(params) {
    const {setIsProfileModal, user, setUser} = params
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('md'));
    const location = useLocation();
    const {isOnline, setIsSignIn} = useContext(StoreContext)

    //open google signIn and set user details in User
    const handleSignIn = () => {
        if (isOnline) {
            googleSigIn().then(response => {
                setUser({
                    photoURL: response?.user.photoURL,
                    displayName: response?.user.displayName,
                    email: response?.user.email
                });
                setIsSignIn(true);
            }).catch((error) => {
                console.log("signIn error: ", error)
            });
        } else {
            errorToast(Constants.InternetOffMsg);
        }
    }
    return (
        localStorage.getItem("isSigIn") === "true" ?
            //signIn then show Image and down Arrow
            <ImageWithArrow setIsProfileModal={setIsProfileModal} user={user} isMobile={isMobile} signIn={true}/>
            :
            (isMobile && location.pathname === PathName.playGround) ?
                //user is not signedIn and in playground screen then show avatar with down arrow
                <ImageWithArrow setIsProfileModal={setIsProfileModal} user={user} isMobile={isMobile} signIn={false}/>
                :
                // signIn button
                <button onClick={() => handleSignIn()} className={`${styles.buttonIcon} ${styles.iconMargin}`}>
                    <span>Sign in</span>
                </button>
    )
}

/**
 * Component render Image with arrow  which is clickable
 * @param params
 * @returns {JSX.Element}
 * @constructor
 */
function ImageWithArrow(params) {
    const {setIsProfileModal, user, isMobile, signIn} = params
    return (
        <div title={"User Profile"} onClick={() => setIsProfileModal(true)} className={styles.profileDiv}>

            {/*image display*/}
            {signIn ? user?.photoURL ?
                    <img alt="Profile Icon" src={user.photoURL}
                         style={{height: 30, width: 30, borderRadius: '50%', objectFit: 'cover'}}/>
                    :
                    <LoaderComponent color="white" height="20" width="20"/>
                :
                //Show avatar when not signIn
                <img alt="Profile Icon" src={Images.avatar}
                     style={{height: 28, width: 28, borderRadius: 90,}}/>
            }

            {/*name*/}
            {!isMobile && <WhiteText extraStyle={styles.extraStyles} text={user?.displayName?.split(" ")[0]}/>}

            {/*dropdown arrow*/}
            <img alt="arrow button" className={styles.icon} src={Images.downArrowIcon}
                 style={{height: isMobile ? 25 : 20, width: isMobile ? 25 : 20}}/>
        </div>
    )
}