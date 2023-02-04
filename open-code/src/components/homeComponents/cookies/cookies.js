import React, {useContext} from 'react';
import CookieConsent from "react-cookie-consent";
import Style from "./cookies.module.css";
import CookiesIcon from "../../../assets/images/icon/union.png";
import CookieImage from "../../../assets/images/icon/cookies.png";
import {ThemeContext} from "../../../App";

function CookiesComponent() {
    const {theme} = useContext(ThemeContext)
    return (
        <CookieConsent
            containerClasses={Style.main + " " + (theme === "dark" ? Style.mainDark : Style.mainLight) }
            contentClasses={Style.ContentAndImage}
            buttonText={
                <>
                    <img className={Style.btnImage} src={CookiesIcon} alt="icon"/>
                    <div className={Style.btnText}>Accept cookies</div>
                </>
            }
            declineButtonText={
                <>
                    <div className={Style.btnDeclineText + " " + (theme === "dark" ? Style.btnDeclineTextDark : Style.btnDeclineTextLight)}>Decline</div>
                </>
            }
            buttonClasses={Style.acceptButton}
            declineButtonClasses={Style.declineButton + " " + (theme === "dark" ? Style.declineButtonDark : Style.declineButtonLight) }
            buttonWrapperClasses={Style.buttonWrapper}

            // onAccept={(acceptedByScrolling) => {
            //     if (acceptedByScrolling) {
            //         alert("Accept was triggered by user scrolling");
            //     } else {
            //         alert("Accept was triggered by clicking the Accept button");
            //     }
            // }}
            enableDeclineButton
            // onDecline={() => {
            //     alert("nay!");
            // }}
        >

            <img className={Style.cookieImage} src={CookieImage} alt=""/>
            <div style={{width:"608px" ,color:"#6F6C90"}}>
                Our website use cookies. By continuing navigating, we assume your permission to deploy cookies as
                detailed in our Privacy Policy.
            </div>

        </CookieConsent>
    );
}

export default CookiesComponent;