import React, {useContext} from 'react';
import CookieConsent from "react-cookie-consent";
import Style from "./cookies.module.css";
import CookiesIcon from "../../../assets/images/icon/union.png";
import CookieImage from "../../../assets/images/icon/cookies.png";
import {ThemeContext} from "../../../App";
import BlackText from "../../fonts/blackText";
import {Constants} from "../../../utils/constants";
import useMediaQuery from "@mui/material/useMediaQuery";
import {useLocation} from "react-router-dom";
import {useTheme} from "@mui/material";


/**
 *  Cokkie
 * @returns {JSX.Element}
 * @constructor
 */
function CookiesComponent() {
    const {theme} = useContext(ThemeContext)
    const themes = useTheme();
    const isMobile = useMediaQuery(themes.breakpoints.down('md'));


    return (
        <CookieConsent
            containerClasses={Style.main + " " + (theme === "dark" ? Style.mainDark : Style.mainLight)}
            contentClasses={Style.ContentAndImage}
            buttonText={<ButtonText/>}
            declineButtonText={<DeclineButtonText/>}
            buttonClasses={Style.acceptButton}
            declineButtonClasses={Style.declineButton + " " + (theme === "dark" ? Style.declineButtonDark : Style.declineButtonLight)}
            buttonWrapperClasses={Style.buttonWrapper}
            enableDeclineButton
        >
            <img className={Style.cookieImage} src={CookieImage} alt=""/>
            <div style={{width: "608px", color: "#6F6C90"}}>
                <BlackText
                    text={Constants.CookieMsg} divStyle={{width : isMobile &&"42%"}}/>
            </div>
        </CookieConsent>
    );
}

export default CookiesComponent;

function ButtonText() {
    return (
        <>
            <img className={Style.btnImage} src={CookiesIcon} alt="icon"/>
            <div className={Style.btnText}>Accept cookies</div>
        </>
    )
}

function DeclineButtonText() {
    const {theme} = useContext(ThemeContext)
    return (
        <div
            className={Style.btnDeclineText + " " + (theme === "dark" ? Style.btnDeclineTextDark : Style.btnDeclineTextLight)}>Decline</div>)
}