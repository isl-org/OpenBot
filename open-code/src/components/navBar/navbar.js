import React, {useContext, useState} from 'react';
import info from "../../assets/images/icon/info.png"
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
import { useLocation } from 'react-router-dom';
import UpArrow from "../../assets/images/icon/up-arrow.png";
import Edit from "../../assets/images/icon/edit.png";
import trash from "../../assets/images/icon/trash.png";
import {QrDrawer} from "../drower/drower";
import DeleteModel from "../../pages/profile/deleteModel";
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

    const openHomepage = () => {
        let path = `/`;
        navigate(path);
    }

    const location = useLocation();
    const handleDelete = () => {
        setDeleteProject(true)
    }

    return (
        <>
            { deleteProject && <DeleteModel setDeleteProject={setDeleteProject}/>}
           <div style={NavbarStyle.navbarDiv}>
                <div style={NavbarStyle.navbarTitleDiv}>
                    <img alt="" style={{...NavbarStyle.mainIcon, ...NavbarStyle.iconMargin}} src={icon} onClick={() => {
                        openHomepage()
                    }}/>
                    <span style={{...NavbarStyle.mainTitle, ...NavbarStyle.iconMargin}}>OpenCode</span>
                </div>

               {location.pathname==="/playground" ? !anchorEl ?
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
               :""}
                <div style={NavbarStyle.navbarIconDiv}>
                    <img alt="" src={info} style={{...NavbarStyle.infoIcon, ...NavbarStyle.iconMargin}}/>
                    <img alt="" onClick={() => toggleTheme(!theme)} src={moon}
                         style={{...NavbarStyle.moonIcon, ...NavbarStyle.iconMargin}}/>
                    <img alt="" src={line} style={{...NavbarStyle.lineIcon, ...NavbarStyle.iconMargin}}/>
                    {
                        isSigIn ?
                            <div className={styles.profileDiv}>
                                <img alt="Profile Icon" src={profileImage} style={{height: 28, width: 28}}/>
                                <WhiteText extraStyle={styles.extraStyles} text="sanjeev"/>
                                <img onClick={() => ProfileOption(true)} alt="arrow button" src={downArrow}
                                     style={{height: 20, width: 20}}/>
                                <ProfileOption/>
                            </div> :
                            <button onClick={() => setIsSigIn(true)}
                                    style={{...NavbarStyle.buttonIcon, ...NavbarStyle.iconMargin}}><span>Sign in</span>
                            </button>
                    }
                    {location.pathname==="/playground" ? <QrDrawer/>:""}
                </div>
            </div>
        </>
    );
}

export function ProfileOption(props){
    // const {open} = props
    return(
        <Modal
            open={props}>
            <Box className={styles.modalStyle}>
                <div className={styles.listStyle}>
                    <img alt="icon" src={Images.userIcon} className={styles.modalIcon}/>
                    <BlueText extraStyle={styles.modalText} styles text={"Edit Profile"}/>
                </div>
                <div className={styles.listStyle}>
                    <img alt="icon" src={Images.helpIcon} className={styles.modalIcon}/>
                    <BlueText extraStyle={styles.modalText} text={"Help Center"}/>
                </div>
                <div className={styles.listStyle}>
                    <img alt="icon" src={Images.logoutIcon} className={styles.modalIcon}/>
                    <BlueText extraStyle={styles.modalText} text={"Logout"}/>
                </div>
            </Box>
        </Modal>
    )
}

