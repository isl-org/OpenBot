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
    const {anchorEl, setDeleteProject, theme, handleClick, setProjectName, projectName, setOpen, open} = params
    const id = open ? 'simple-popper' : undefined
    const [rename, setRename] = useState(false);
    const [renameProject, setRenameProject] = useState("")

    useEffect(() => {
        console.log("projecTNAme::", projectName)
        setRenameProject(projectName)
    }, [])


    const handleDelete = () => {
        setDeleteProject(true)
    }


    return (
        <>
            {/*project name with edit field and arrow*/}
            <div className={styles.playgroundName}>
                <input type="text" className={styles.Edit}
                       id="userEdit"
                       onChange={(e) => setRenameProject(e.target.value)}
                       onClick={(e) => {
                           e.stopPropagation()
                           setOpen(false);
                       }}
                       onFocus={(e) => e.target.select()}
                       onBlur={() => setRename(false)
                       }
                       onKeyDown={(e) => {
                           if (e.keyCode === 13) setRename(false)
                       }}
                       style={{width: `${renameProject?.length}ch`}}
                       value={renameProject}
                /> : <span className={`${styles.mainTitle} ${styles.arrowMargin}`}>{projectName}</span>}
                <img src={UpArrow}
                     className={`${styles.infoIcon} ${styles.arrowMargin}`}
                     onClick={() => setOpen(!open)} alt={"arrow"}/>
            </div>
            <EditProjectPopUp open={open} anchorEl={anchorEl}
                              projectName={renameProject}
                              setOpen={setOpen}
                              setRename={setRename}
                              handleDelete={handleDelete}
                              theme={theme}
            />
        </>
    )
}

export function EditProjectPopUp(params) {
    const {open, anchorEl, setRename, handleDelete, theme, extraStyle, setOpen, projectName} = params
    const id = open ? 'simple-popper' : undefined
    const popUpRef = useRef(null);
    //{/*pop up of rename and delete option for project name */}
    // useEffect(() => {
    //     setOpen(open);
    // }, [open]);

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (popUpRef.current && !popUpRef.current.contains(event.target)) {
                setOpen(false);
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
            key={id} open={open} anchorEl={anchorEl}>
            <div
                ref={popUpRef}
                className={styles.option + " " + (theme === "dark" ? styles.darkTitleModel : styles.lightTitleModel) + " " + extraStyle}>
                <div
                    className={`${styles.item} ${styles.renameDivMargin}  ${(theme === "dark" ? styles.darkItem : styles.lightItem)}`}
                    onClick={async (event) => {
                        event.stopPropagation();
                        setRename(true);
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
