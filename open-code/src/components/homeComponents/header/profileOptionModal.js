import React, {useContext} from "react";
import {ThemeContext} from "../../../App";
import {Box, Modal, useTheme} from "@mui/material";
import styles from "../../navBar/navbar.module.css";
import {colors} from "../../../utils/color";
import {Images} from "../../../utils/images";
import WhiteText from "../../fonts/whiteText";
import BlueText from "../../fonts/blueText";
import {PathName, Themes} from "../../../utils/constants";
import useMediaQuery from "@mui/material/useMediaQuery";
import {useLocation} from "react-router-dom";

export function ProfileOptionModal(props) {
    const {
        isProfileModal,
        setIsProfileModal,
        setIsEditProfileModal,
        setIsLogoutModal,
    } = props
    const theme = useContext(ThemeContext);
    const themes = useTheme();
    const isMobile = useMediaQuery(themes.breakpoints.down('md'));
    const location = useLocation();

    const handleClose = () => {
        setIsProfileModal(false)
    }
    const handleEditProfile = () => {
        setIsEditProfileModal(true)
        handleClose()
    }
    const handleLogOut = () => {
        setIsLogoutModal(true)
        handleClose()
    }
    return (
        <Modal
            open={isProfileModal}
            onClose={handleClose}>
            <Box className={styles.profileOptionModalStyle}
                 style={{backgroundColor: theme.theme === Themes.dark ? colors.blackPopupBackground : colors.whiteBackground}}>
                <PopUpInRowText onClick={handleEditProfile} text={"Edit Profile"}
                              icon={theme.theme === Themes.dark ? Images.darkUserIcon : Images.userIcon}/>
                <PopUpInRowText onClick={handleLogOut} text={"Logout"}
                              icon={theme.theme === Themes.dark ? Images.darkLogoutIcon : Images.logoutIcon}/>
                {/*{isMobile && location.pathname === PathName.playGround &&*/}
                {/*    <PopUpInRowText onClick={handleLogOut} text={"Logout"}*/}
                {/*    icon={theme.theme === Themes.dark ? Images.darkLogoutIcon : Images.logoutIcon}/>*/}
                {/*}*/}

            </Box>
        </Modal>
    )
}


/**
 * display icon with text in popup
 * @param params
 * @returns {JSX.Element}
 * @constructor
 */
function PopUpInRowText(params) {
    const theme = useContext(ThemeContext);
    const {onClick, text, icon} = params
    return (
        <div onClick={onClick}
             className={`${styles.item} ${styles.editProfileMargin}  ${(theme.theme === Themes.dark ? styles.darkItem : styles.lightItem)}`}>
            <img alt="icon" src={icon}
                 className={styles.modalIcon}/>
            <span className={`${styles.inRowText}`}>
                {theme.theme === Themes.dark ?
                    <WhiteText extraStyle={styles.modalText} styles text={text}/> :
                    <BlueText extraStyle={styles.modalText} styles text={text}/>}
            </span>
        </div>
    )
}