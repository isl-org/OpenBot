import React, {useContext} from 'react';
import CookieConsent from "react-cookie-consent";
import Style from "./cookies.module.css";
import CookiesIcon from "../../../assets/images/icon/union.png";
import CookieImage from "../../../assets/images/icon/cookies.png";
import {ThemeContext} from "../../../App";
import BlackText from "../../fonts/blackText";
import {Constants} from "../../../utils/constants";

/**
 *  render Accept Cookie card
 * @returns {JSX.Element}
 * @constructor
 */
function CookiesComponent() {
    const {theme} = useContext(ThemeContext)
    const isAndroid = navigator.userAgent.toLowerCase().indexOf("android") > -1;
    const isIOS = /iPad|iPhone|iPod/.test(navigator.userAgent) && !window.MSStream;

    return (
        <CookieConsent
            containerClasses={Style.main + " " + (theme === "dark" ? Style.mainDark : Style.mainLight)}
            contentClasses={Style.ContentAndImage}
            buttonText={<ButtonText/>}
            declineButtonText={<DeclineButtonText/>}
            buttonClasses={Style.acceptButton}
            declineButtonClasses={Style.declineButton + " " + (theme === "dark" ? Style.declineButtonDark : Style.declineButtonLight)}
            buttonWrapperClasses={isIOS ? Style.iosButtonDiv : isAndroid ? Style.androidButtonDiv : Style.buttonWrapper}
            enableDeclineButton
        >
            <img className={Style.cookieImage} src={CookieImage} alt=""/>
            <BlackText text={Constants.CookieMsg}/>
        </CookieConsent>
    );
}

export default CookiesComponent;

// Accept cookie button
function ButtonText() {
    const isAndroid = navigator.userAgent.toLowerCase().indexOf("android") > -1;
    const isMobileLandscape = window.matchMedia("(max-height:440px) and (max-width: 1000px) and (orientation: landscape)").matches

    return (
        <>
            <img className={Style.btnImage} src={CookiesIcon} alt="icon"/>
            <div className={Style.btnText} style={{fontSize: isAndroid && isMobileLandscape && "16px"}}>Accept cookies
            </div>
        </>
    )
}

//decline button
function DeclineButtonText() {
    const {theme} = useContext(ThemeContext)

    return (
        <div
            className={Style.btnDeclineText + " " + (theme === "dark" ? Style.btnDeclineTextDark : Style.btnDeclineTextLight)}>Decline</div>)
}