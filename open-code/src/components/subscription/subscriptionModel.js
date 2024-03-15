import React, {useContext} from "react";
import {Images} from "../../utils/images";
import Styles from "./subscriptionModel.module.css"
import {Box, Modal} from "@mui/material";
import styles from "../navBar/navbar.module.css";
import BlackText from "../fonts/blackText";
import BlueButton from "../buttonComponent/blueButtonComponent";
import {ThemeContext} from "../../App";
import WhiteText from "../fonts/whiteText";
import {Constants, Themes} from "../../utils/constants";

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
                className={Styles.planExpirationModel + " " + (theme === Themes.dark ? styles.darkLogoutModalBox : styles.lightLogoutModalBox)}>
                <div className={Styles.closeModelDiv}>
                    <img alt={"cross"} src={theme === Themes.dark ? Images.darkCrossIcon : Images.crossIcon}
                         style={{width: theme === Themes.dark ? 18 : 25, cursor: "pointer"}}
                         onClick={handleClose}/></div>
                <div className={Styles.expireMain}>
                    <div className={Styles.expire}>
                        <img src={Images.subscriptionExpire} style={{width: 60}} alt={"expireTime"}/>
                        {theme === Themes.dark ?
                            <WhiteText text={Constants.subscriptionEnded} extraStyle={Styles.expireText}/> :
                            <BlackText text={Constants.subscriptionEnded} extraStyle={Styles.expireText}/>}
                        {theme === Themes.dark ?
                            <WhiteText text={Constants.subscriptionContinueService}
                                       extraStyle={Styles.expirationDescription}/> :
                            <BlackText text={Constants.subscriptionContinueService}
                                       extraStyle={Styles.expirationDescription}/>}
                        {theme === Themes.dark ?
                            <WhiteText text={Constants.subscriptionContinueInfo}
                                       extraStyle={Styles.expirationDescription}/> :
                            <BlackText text={Constants.subscriptionContinueInfo}
                                       extraStyle={Styles.expirationDescription}/>}
                        <BlueButton onClick={() => {
                            //TODO add subscriptions page of dashboard
                        }} buttonType={"contained"}
                                    buttonName={Constants.subscribeButton}
                                    extraStyle={Styles.subscribeButton}/>
                    </div>
                </div>
            </Box>}
        </Modal>
    );
}

export default SubscriptionModel;