import React, {useContext} from "react";
import {ThemeContext} from "../../../App";
import {Box, Modal, useTheme} from "@mui/material";
import styles from "../../navBar/navbar.module.css";
import {colors} from "../../../utils/color";
import {Images} from "../../../utils/images";
import WhiteText from "../../fonts/whiteText";
import BlueText from "../../fonts/blueText";
import {Constants, errorToast, PathName, Themes} from "../../../utils/constants";
import useMediaQuery from "@mui/material/useMediaQuery";
import {useLocation} from "react-router-dom";
import {googleSigIn} from "../../../services/firebase";
import {StoreContext} from "../../../context/context";


/**
 * profile option modal have edit profile, how to upload, change theme, signIn and logout option
 * @param props
 * @returns {JSX.Element}
 * @constructor
 */
export function ProfileOptionModal(props) {
    const {
        isProfileModal,
        setIsProfileModal,
        setIsEditProfileModal,
        setIsHelpCenterModal,
        setIsLogoutModal,
    } = props
    const location = useLocation();
    const themes = useTheme();
    const {theme, toggleTheme, setUser} = useContext(ThemeContext);
    const {isOnline} = useContext(StoreContext)
    const isMobile = useMediaQuery(themes.breakpoints.down('md'));
    const isSignedIn = localStorage.getItem("isSigIn") === "true";
    const isHomePage = location.pathname === PathName.home;
    const isOnPlaygroundPage = location.pathname === PathName.playGround;
    const tabletQuery = window.matchMedia("(min-width: 768px) and (max-width: 1024px)").matches;

    const handleClose = () => {
        setIsProfileModal(false)
    }

    const handleOnclick = (setVariable) => {
        handleClose()
        setVariable(true)
    }

    const handleSignIn = () => {
        if (isOnline) {
            googleSigIn().then(response => {
                setUser({
                    photoURL: response?.user.photoURL,
                    displayName: response?.user.displayName,
                    email: response?.user.email
                });
            }).catch((error) => {
                console.log("signIn error: ", error)
            });
        } else {
            errorToast(Constants.InternetOffMsg)
        }
    }

    return (
        <Modal
            open={isProfileModal}
            onClose={handleClose}>
            <Box className={styles.profileOptionModalStyle}
                 style={{backgroundColor: theme === Themes.dark ? colors.blackPopupBackground : colors.whiteBackground}}>

                {(isHomePage || (isOnPlaygroundPage && isSignedIn)) &&
                    <PopUpInRowText onClick={() => handleOnclick(setIsEditProfileModal)} text={"Edit Profile"}
                                    icon={theme === Themes.dark ? Images.darkUserIcon : Images.userIcon}/>
                }
                {isOnPlaygroundPage && isMobile &&
                    <>
                        { !tabletQuery && <PopUpInRowText
                            onClick={() => {
                                handleClose();
                                toggleTheme(!theme)
                            }}
                            text={"Change Theme"}
                            icon={theme === Themes.dark ? Images.lightThemeIcon : Images.blueTheme}/>}
                        <PopUpInRowText onClick={() => handleOnclick(setIsHelpCenterModal)} text={"How To Upload"}
                                        icon={theme === Themes.dark ? Images.helpIcon : Images.infoLight}/>
                    </>
                }
                {((isHomePage) || (isOnPlaygroundPage && isSignedIn)) &&
                    <PopUpInRowText onClick={() => handleOnclick(setIsLogoutModal)} text={"Logout"}
                                    icon={theme === Themes.dark ? Images.darkLogoutIcon : Images.logoutIcon}/>
                }
                {isMobile && isOnPlaygroundPage && !isSignedIn &&
                    <PopUpInRowText onClick={() => handleSignIn()} text={"Sign In"}
                                    icon={theme === Themes.dark ? Images.darkLogoutIcon : Images.login}/>
                }
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
