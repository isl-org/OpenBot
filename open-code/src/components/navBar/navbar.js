import React, {useContext, useState, useRef} from 'react';
import moon from "../../assets/images/icon/whiteMode/white-mode-icon.png";
import line from "../../assets/images/line.png";
import icon from "../../assets/images/icon/open-bot-logo.png"
import profileImage from "../../assets/images/icon/profile-image.png"
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
import {QrDrawer} from "../drower/drower";
import DeleteModel from "../../pages/profile/deleteModel";
import SimpleInputComponent from "../inputComponent/simpleInputComponent";

export function Navbar() {
    const {theme, toggleTheme} = useContext(ThemeContext)
    const [isSigIn, setIsSigIn] = useState(false);
    const {projectName, setProjectName} = useContext(StoreContext);
    const [anchorEl, setAnchorEl] = useState(null);
    const [deleteProject, setDeleteProject] = useState(false);
    const handleClick = (event) => {
        setAnchorEl(anchorEl ? null : event.currentTarget);
    };
    const open = Boolean(anchorEl);
    const id = open ? 'simple-popper' : undefined;
    let navigate = useNavigate();
    const [isProfileModal, setIsProfileModal] = useState(false)
    const [isEditProfileModal, setIsEditProfileModal] = useState(false)
    const openHomepage = () => {
        let path = `/`;
        navigate(path);
    }
    const location = useLocation();
    const handleDelete = () => {
        setDeleteProject(true)
    }

    return (
        <div>
            {deleteProject && <DeleteModel setDeleteProject={setDeleteProject}/>}
            <div style={NavbarStyle.navbarDiv}>
                <div style={NavbarStyle.navbarTitleDiv}>
                    <img alt="" style={{...NavbarStyle.mainIcon, ...NavbarStyle.iconMargin}} src={icon} onClick={() => {
                        openHomepage()
                    }}/>
                    <span style={{...NavbarStyle.mainTitle, ...NavbarStyle.iconMargin}}>OpenCode</span>
                </div>

                {location.pathname === "/playground" ? !anchorEl ?
                        <div style={NavbarStyle.playgroundName} onClick={handleClick}>
                    <span
                        style={{...NavbarStyle.mainTitle, ...NavbarStyle.arrowMargin}}>{projectName}</span>
                            <img src={downArrow}
                                 style={{...NavbarStyle.infoIcon, ...NavbarStyle.arrowMargin}}
                                 alt={icon}/>
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
                                     onClick={handleClick} alt={icon}/>
                            </div>
                            <Popper id={id} open={open} anchorEl={anchorEl}>
                                <div className={styles.option}>
                                    <div className={styles.item} onClick={handleClick}>
                                        <img alt="Icon" className={styles.icon} src={Edit}/>
                                        <div>Rename</div>
                                    </div>
                                    <div className={styles.item} onClick={handleDelete}>
                                        <img alt="Icon" className={styles.icon} src={trash}/>
                                        <div> Delete File</div>
                                    </div>
                                </div>
                            </Popper>
                        </>
                    : ""}
                <div style={NavbarStyle.navbarIconDiv}>
                    <img alt="" onClick={() => toggleTheme(!theme)} src={moon}
                         style={{...NavbarStyle.moonIcon, ...NavbarStyle.iconMargin}}/>
                    <img alt="" src={line} style={{...NavbarStyle.lineIcon, ...NavbarStyle.iconMargin}}/>
                    {
                        isSigIn ?
                            <div onClick={() => setIsProfileModal(true)} className={styles.profileDiv}>
                                <img alt="Profile Icon" src={profileImage} style={{height: 28, width: 28}}/>
                                <WhiteText extraStyle={styles.extraStyles} text="sanjeev"/>
                                <img alt="arrow button" src={downArrow} style={{height: 20, width: 20}}/>
                            </div> :
                            <button onClick={() => setIsSigIn(true)}
                                    style={{...NavbarStyle.buttonIcon, ...NavbarStyle.iconMargin}}><span>Sign in</span>
                            </button>
                    }
                    {
                        isProfileModal && <ProfileOptionModal isProfileModal={isProfileModal}
                                                              setIsProfileModal={setIsProfileModal}
                                                              setIsEditProfileModal={setIsEditProfileModal}/>
                    }
                    {
                        isEditProfileModal && <EditProfileModal isEditProfileModal={isEditProfileModal}
                                                                setIsEditProfileModal={setIsEditProfileModal}/>
                    }
                    {location.pathname === "/playground" ? <QrDrawer/> : ""}
                </div>
            </div>
        </div>
    )
        ;
}

export function ProfileOptionModal(props) {
    const {isProfileModal, setIsProfileModal, setIsEditProfileModal} = props
    const location = useLocation();
    const handleClose = () => {
        setIsProfileModal(false)
    }
    return (
        <Modal
            open={isProfileModal}
            onClose={handleClose}>
            <Box className={styles.profileOptionModalStyle}>
                <div onClick={() => {
                    setIsEditProfileModal(true)
                    handleClose()
                }} className={styles.listStyle}>
                    <img alt="icon" src={Images.userIcon} className={styles.modalIcon}/>
                    <BlueText extraStyle={styles.modalText} styles text={"Edit Profile"}/>
                </div>
                {
                    location.pathname === "/playground" &&
                    <div onClick={() => {
                        handleClose()
                    }} className={styles.listStyle}>
                        <img alt="icon" src={Images.helpIcon} className={styles.modalIcon}/>
                        <BlueText extraStyle={styles.modalText} text={"Help Center"}/>
                    </div>
                }
                <div onClick={() => {
                    handleClose()
                }} className={styles.listStyle}>
                    <img alt="icon" src={Images.logoutIcon} className={styles.modalIcon}/>
                    <BlueText extraStyle={styles.modalText} text={"Logout"}/>
                </div>
            </Box>
        </Modal>
    )
}

export function EditProfileModal(props) {
    const {isEditProfileModal, setIsEditProfileModal} = props
    const [file, setFile] = useState(null);
    const inputRef = useRef();
    const handleClose = () => {
        setIsEditProfileModal(false)
    }

    function handleChange(e) {
        console.log(e.target.files[0].name.replace(/HEIC/g, 'jpg'));
        setFile(URL.createObjectURL(e.target.files[0]));
    }

    return (
        <Modal
            open={isEditProfileModal}
            style={{display: "flex", alignItems: "center", justifyContent: "center"}}>
            <Box className={styles.editProfileModal}>
                <div onClick={handleClose} className={styles.crossIconDiv}>
                    <img alt={"cross icon"} style={{height: 20, width: 20}} src={Images.crossIcon}/>
                </div>
                <div style={{backgroundImage: `url(${file})`}} className={styles.profileImg}>
                    <input ref={inputRef} style={{display: "none",}} type="file" onChange={handleChange}/>
                    <img onClick={() => inputRef.current.click()} alt={"edit profile icon"}
                         className={styles.editProfileIcon} src={Images.editProfileIcon}/>
                </div>
                <div style={{display: "flex"}}>
                    <SimpleInputComponent extraStyle={styles.inputExtraStyle} inputTitle={"Full Name"}/>
                    <SimpleInputComponent inputType={"date"} extraStyle={styles.inputExtraStyle} inputTitle={"Date Of Birth"}/>
                </div>
                <SimpleInputComponent inputType={"email"} extraStyle={styles.emailInputExtraStyle} inputTitle={"Email address"}/>
            </Box>
        </Modal>
    )
}

