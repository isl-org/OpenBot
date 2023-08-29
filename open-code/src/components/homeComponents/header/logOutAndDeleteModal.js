import React, {useContext, useState} from "react";
import {ThemeContext} from "../../../App";
import {Box, Modal} from "@mui/material";
import styles from "../../navBar/navbar.module.css";
import BlackText from "../../fonts/blackText";
import BlueButton from "../../buttonComponent/blueButtonComponent";
import LoaderComponent from "../../loader/loaderComponent";
import {StoreContext} from "../../../context/context";
import {Constants, errorToast} from "../../../utils/constants";


/**
 * Pop up for delete,log out and signIn and session expire
 * @param props
 * @returns {JSX.Element}
 * @constructor
 */
export function PopUpModal(props) {
    const {theme} = useContext(ThemeContext)
    const {
        setVariable,
        headerText,
        buttonText,
        containText,
        handleButtonClick,
        deleteLoader,
        inlineStyle,
    } = props;
    const {isOnline} = useContext(StoreContext);
    const [open, setOpen] = useState(true);

    const handleClose = () => {
        setVariable(false);
        return setOpen(false);
    };

    const handleRightButton = () => {
        if (isOnline) {
            handleButtonClick()
        } else {
            errorToast(Constants.InternetOffMsg)
        }
    }

    return (
        <div>
            <Modal
                className={styles.logoutModal}
                open={open}
                onClose={() => handleClose()}>
                {deleteLoader ?
                    <LoaderComponent/>
                    : <Box
                        className={styles.logoutModalBox + " " + (theme === "dark" ? styles.darkLogoutModalBox : styles.lightLogoutModalBox)}>
                        <BlackText extraStyle={styles.headerStyle} text={headerText}/>
                        <div style={{marginTop: 20}}>
                            <BlackText
                                extraStyle={(theme === "dark" ? styles.darkLogoutMessageModal : styles.lightLogoutMessageModal)}
                                text={containText}/>
                        </div>
                        <div className={styles.logoutButtonsDiv}>
                            <BlueButton onClick={() => {
                                handleClose()
                            }} buttonName={"Cancel"}
                                        buttonStyle={styles.buttonStyle}
                                        extraStyle={styles.logoutButtonsExtraStyle + " " + styles.cancelExtraStyle}/>
                            <BlueButton onClick={() => handleRightButton()} buttonType={"contained"}
                                        buttonName={buttonText}
                                        inlineStyle={inlineStyle}
                                        buttonStyle={styles.buttonStyle}
                                        extraStyle={styles.logoutButtonsExtraStyle}/>
                        </div>
                    </Box>}
            </Modal>
        </div>
    );
}
