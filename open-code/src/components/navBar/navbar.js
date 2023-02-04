import React, {useContext, useState} from 'react';
import info from "../../assets/images/icon/info.png"
import moon from "../../assets/images/icon/whiteMode/white-mode-icon.png"
import line from "../../assets/images/line.png"
import icon from "../../assets/images/icon/open-bot-logo.png"
import profileImage from "../../assets/images/icon/profile-image.png"
import downArrow from "../../assets/images/icon/down-arrow.png"
import {NavbarStyle} from "./styles";
import {useNavigate} from "react-router-dom";
import {ThemeContext} from "../../App"
import WhiteText from "../fonts/whiteText";
import styles from "./navbar.module.css"
import {Box, Modal} from "@mui/material";
import BlueText from "../fonts/blueText";
import {Images} from "../../utils/images";

export function Navbar() {
    let navigate = useNavigate();
    const {theme, toggleTheme} = useContext(ThemeContext)
    const [isSigIn, setIsSigIn] = useState(false);
    const openHomepage = () => {
        let path = `/`;
        navigate(path);
    }

    return (
        <div style={NavbarStyle.navbarDiv}>
            <div style={NavbarStyle.navbarTitleDiv}>
                <img alt="" style={{...NavbarStyle.mainIcon, ...NavbarStyle.iconMargin}} src={icon} onClick={() => {
                    openHomepage()
                }}/>
                <span style={{...NavbarStyle.mainTitle, ...NavbarStyle.iconMargin}}>OpenCode</span>
            </div>
            <div style={NavbarStyle.navbarIconDiv}>
                <img alt="" src={info} style={{...NavbarStyle.infoIcon, ...NavbarStyle.iconMargin}}/>
                <img alt="" onClick={() => toggleTheme(!theme)} src={moon}
                     style={{...NavbarStyle.moonIcon, ...NavbarStyle.iconMargin}}/>
                <img alt="" src={line} style={{...NavbarStyle.lineIcon, ...NavbarStyle.iconMargin}}/>
                {
                    isSigIn ?
                        <div className={styles.profileDiv}>
                            <img alt="Profile Icon" src={profileImage} style={{height: 28,width: 28}}/>
                            <WhiteText extraStyle  ={styles.extraStyles} text = "sanjeev"/>
                            <img onClick={() => ProfileOption(true)} alt="arrow button" src={downArrow} style={{height: 20,width: 20}}/>
                            <ProfileOption/>
                        </div> :
                        <button onClick={() => setIsSigIn(true)}
                                style={{...NavbarStyle.buttonIcon, ...NavbarStyle.iconMargin}}><span>Sign in</span>
                        </button>
                }
            </div>
        </div>
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

