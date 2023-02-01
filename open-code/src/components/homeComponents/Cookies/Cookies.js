import React from 'react';
import CookieConsent from "react-cookie-consent";
import Style from "./cookies.module.css";
import CookiesIcon from "../../../assets/images/Union.png";
import CookieImage from "../../../assets/images/cookies.png";

function CookiesComponent(props) {
    return (
        <CookieConsent
            containerClasses={Style.main}
            contentClasses={Style.ContentAndImage}
            buttonText={
                <>
                    <img className={Style.btnImage} src={CookiesIcon} alt="icon"/>
                    <div className={Style.btnText}>Accept cookies</div>
                </>
            }
            declineButtonText={
                <>
                    <div className={Style.btnDeclineText}>Decline</div>
                </>
            }
            buttonClasses={Style.acceptButton}
            declineButtonClasses={Style.declineButton}
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
            <div style={{width:"608px"}}>
                Our website use cookies. By continuing navigating, we assume your permission to deploy cookies as
                detailed in our Privacy Policy.
            </div>

        </CookieConsent>
    );
}

export default CookiesComponent;