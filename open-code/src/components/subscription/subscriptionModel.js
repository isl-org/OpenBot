import React, {useContext} from "react";
import {Images} from "../../utils/images";
import Styles from "./subscriptionModel.module.css"
import {Box, Modal} from "@mui/material";
import styles from "../navBar/navbar.module.css";
import BlackText from "../fonts/blackText";
import BlueButton from "../buttonComponent/blueButtonComponent";
import {ThemeContext} from "../../App";
import WhiteText from "../fonts/whiteText";

/**
 * function to display subscription expire for user
 * @param props
 * @returns {Element}
 * @constructor
 */
const SubscriptionModel = (props) => {
    const {theme} = useContext(ThemeContext);
    const {setIsSubscriptionExpire, isSubscriptionExpire} = props;

    //function to close the subscription expire model
    function handleClose() {
        setIsSubscriptionExpire(false);
    }

    return (
        <Modal
            className={Styles.expireModel}
            open={isSubscriptionExpire}
            onClose={() => handleClose()}>
            {<Box
                className={Styles.planExpirationModel + " " + (theme === "dark" ? styles.darkLogoutModalBox : styles.lightLogoutModalBox)}>
                <div className={Styles.closeModelDiv}>
                    <img alt={"cross"} src={theme === "dark" ? Images.darkCrossIcon : Images.crossIcon}
                         style={{width: theme === "dark" ? 18 : 25, cursor: "pointer"}}
                         onClick={handleClose}/></div>
                <div className={Styles.expireMain}>
                    <div className={Styles.expire}>
                        <img src={Images.subscriptionExpire} style={{width: 60}} alt={"expireTime"}/>
                        {theme === "dark" ?
                            <WhiteText text={"Your trial has just ended!"} extraStyle={Styles.expireText}/> :
                            <BlackText text={"Your trial has just ended!"} extraStyle={Styles.expireText}/>}
                        {theme === "dark" ?
                            <WhiteText text={"To continue using OpenBot Playground, you"}
                                       extraStyle={Styles.expirationDescription}/> :
                            <BlackText text={"To continue using OpenBot Playground, you"}
                                       extraStyle={Styles.expirationDescription}/>}
                        {theme === "dark" ?
                            <WhiteText text={"will need to upgrade your plan."}
                                       extraStyle={Styles.expirationDescription}/> :
                            <BlackText text={"will need to upgrade your plan."}
                                       extraStyle={Styles.expirationDescription}/>}
                        <BlueButton onClick={() => {
                        }} buttonType={"contained"}
                                    buttonName={"Subscribe Now"}
                                    extraStyle={Styles.subscribeButton}/>
                    </div>
                </div>
            </Box>}
        </Modal>
    );
}

export default SubscriptionModel;