import {useNavigate} from "react-router-dom";
import styles from "../../navBar/navbar.module.css";
import icon from "../../../assets/images/icon/open-bot-logo.png";
import downArrow from "../../../assets/images/icon/down-arrow.png";
import {Popper} from "@mui/material";
import UpArrow from "../../../assets/images/icon/up-arrow.png";
import renameIcon from "../../../assets/images/icon/rename-icon.png";
import Edit from "../../../assets/images/icon/edit.png";
import deleteIcon from "../../../assets/images/icon/delete-icon.png";
import trash from "../../../assets/images/icon/trash.png";
import LoaderComponent from "../../loader/loaderComponent";
import WhiteText from "../../fonts/whiteText";
import {googleSigIn} from "../../../services/firebase";
import React, {useEffect, useRef, useState} from "react";
import {renameProject} from "../../../services/workspace";


/**
 * Display logo with openCode text (Header's Left side)
 * @returns {JSX.Element}
 * @constructor
 */
export function LogoSection() {
    let navigate = useNavigate();

    //onClickEvent
    const openHomepage = () => {
        let path = `/`;
        navigate(path);
    }
    return (
        <div className={styles.navbarTitleDiv}>
            <img alt="" className={`${styles.mainIcon} ${styles.iconMargin}`} src={icon} onClick={() => {
                openHomepage()
            }}/>
            <span onClick={() => {
                openHomepage()
            }} className={`${styles.mainTitle} ${styles.iconMargin}`}>OpenCode</span>
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

    return (
        <div className={styles.playgroundName} onClick={handleClick}>
            <span className={`${styles.mainTitle} ${styles.arrowMargin}`}>{projectName}</span>
            <img src={downArrow}
                 className={`${styles.infoIcon} ${styles.arrowMargin}`}
                 alt={"arrow"}/>
        </div>
    )
}


/**
 *ProjectName's POP up which has reName and delete file options
 * @returns {JSX.Element}
 * @constructor
 */
export function ProjectNamePopUp(params) {
    const {anchorEl, setDeleteProject, theme, projectName, setOpen, open, setProjectName} = params
    const [rename, setRename] = useState(false);
    const [reNameProject, setRenameProject] = useState("")
    const [openPopUp, setOpenPopUp] = useState(true);
    const inputRef = useRef(null);


    useEffect(() => {
        setRenameProject(projectName)
    }, [])


    const handleDelete = () => {
        setDeleteProject(true)
    }
    const handleBlur = async () => {
        setProjectName(reNameProject)
        setRename(false)
        setOpen(false)
        await renameProject(reNameProject, projectName).then()
    }
    const handleClickOutside = () => {
        setOpenPopUp(false);
        if (!rename)
            setOpen(false)

    }


    return (
        <div className={styles.playgroundName}>
            {/*project name with edit field and arrow*/}
            {rename ?
                <input type="text" className={styles.Edit}
                       id="userEdit"
                       onClick={(e) => {
                           e.stopPropagation();
                       }}
                       ref={inputRef} // set the ref to the input element
                       onChange={(e) => setRenameProject(e.target.value)}
                       onFocus={(e) => e.target.select()}
                       onBlur={handleBlur}
                       onKeyDown={async (e) => {
                           if (e.keyCode === 13) {
                               setRename(false)
                               setOpen(false)
                               setProjectName(reNameProject)
                               await renameProject(reNameProject, projectName).then()
                           }
                       }}
                       style={{width: `${reNameProject?.length}ch`}}
                       value={reNameProject}
                />
                :
                <span onClick={() => {
                    setOpen(!open)
                }} className={`${styles.mainTitle} ${styles.arrowMargin}`}>{projectName}</span>
            }
            <img src={UpArrow}
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
                                  theme={theme}/>}
        </div>
    )
}

export function EditProjectPopUp(params) {
    const {open, anchorEl, setRename, handleDelete, theme, extraStyle, clickOutside, rename, inputRef} = params
    const id = open ? 'simple-popper' : undefined
    const popUpRef = useRef(null);

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (popUpRef.current && !popUpRef.current.contains(event.target)) {
                clickOutside()
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, [popUpRef]);

    return (
        <Popper
            placement="bottom-start"
            ref={popUpRef}
            key={id} open={open} anchorEl={anchorEl}>
            <div
                className={styles.option + " " + (theme === "dark" ? styles.darkTitleModel : styles.lightTitleModel) + " " + extraStyle}>
                <div
                    className={`${styles.item} ${styles.renameDivMargin}  ${(theme === "dark" ? styles.darkItem : styles.lightItem)}`}
                    onClick={async (event) => {
                        event.stopPropagation();
                        setRename(true);
                        setTimeout(() => {
                            inputRef.current.focus(); // set focus to the input element when rename is true
                        }, 0);
                        // await renameProject(projectName).then()
                    }}>
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

    )
}

/**
 * Profile signin option if not signed in or else show profile icon with name
 * @param params
 * @returns {JSX.Element}
 * @constructor
 */
export function ProfileSignIn(params) {
    const {setIsProfileModal, user, setUser} = params

    const handleSignIn = () => {
        googleSigIn().then(response => {
            setUser({
                photoURL: response?.user.photoURL,
                displayName: response?.user.displayName,
                email: response?.user.email
            });
        }).catch((error) => {
            console.log("signIn error: ", error)
        });
    }

    return (
        localStorage.getItem("isSigIn") === "true" ?
            <div onClick={() => setIsProfileModal(true)} className={styles.profileDiv}>
                {/*image display*/}
                {user?.photoURL ?
                    <img alt="Profile Icon" src={user.photoURL}
                         style={{height: 28, width: 28, borderRadius: 90,}}/>
                    :
                    <LoaderComponent color="white" height="20" width="20"/>
                }
                {/*name*/}
                <WhiteText extraStyle={styles.extraStyles} text={user?.displayName.split(" ")[0]}/>
                {/*dropdown arrow*/}
                <img alt="arrow button" src={downArrow} style={{height: 20, width: 20}}/>
            </div>
            :
            //signIn button
            <button onClick={() => handleSignIn()} className={`${styles.buttonIcon} ${styles.iconMargin}`}>
                <span>Sign in</span>
            </button>
    )
}
