import React, {useContext} from "react";
import {ThemeContext} from "../../../App";
import {Box, Modal} from "@mui/material";
import styles from "../../navBar/navbar.module.css";
import {colors} from "../../../utils/color";
import {Images} from "../../../utils/images";
import WhiteText from "../../fonts/whiteText";
import BlueText from "../../fonts/blueText";
import {Themes} from "../../../utils/constants";

export function ProfileOptionModal(props) {
    const {
        isProfileModal,
        setIsProfileModal,
        setIsEditProfileModal,
        setIsLogoutModal,
    } = props
    const theme = useContext(ThemeContext);

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

                <div onClick={handleEditProfile}
                     className={`${styles.item} ${styles.editProfileMargin}  ${(theme.theme === Themes.dark ? styles.darkItem : styles.lightItem)}`}>

                    <img alt="icon" src={theme.theme === Themes.dark ? Images.darkUserIcon : Images.userIcon}
                         className={styles.modalIcon}/>
                    {theme.theme === Themes.dark ?
                        <WhiteText extraStyle={styles.modalText} styles text={"Edit Profile"}/> :
                        <BlueText extraStyle={styles.modalText} styles text={"Edit Profile"}/>}
                </div>

                <div onClick={handleLogOut}
                     className={`${styles.item} ${styles.logOutMargin} ${(theme.theme === Themes.dark ? styles.darkItem : styles.lightItem)}`}>

                    <img alt="icon" src={theme.theme === Themes.dark ? Images.darkLogoutIcon : Images.logoutIcon}
                         className={styles.modalIcon}/>
                    {theme.theme === Themes.dark ?
                        <WhiteText extraStyle={styles.modalText} text={"Logout"}/> :
                        <BlueText extraStyle={styles.modalText} text={"Logout"}/>}
                </div>

            </Box>
        </Modal>
    )
}