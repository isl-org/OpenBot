import React, {useContext, useState} from "react";
import {ThemeContext} from "../../../App";
import {Box, Modal} from "@mui/material";
import styles from "../../navBar/navbar.module.css";
import BlackText from "../../fonts/blackText";
import BlueButton from "../../buttonComponent/blueButtonComponent";


/**
 * Pop up for delete,log out and signIn and session expire
 * @param props
 * @returns {JSX.Element}
 * @constructor
 */
export function PopUpModal(props) {
    const {theme} = useContext(ThemeContext)
    const {setVariable, headerText, buttonText, containText, handleButtonClick} = props;
    const [open, setOpen] = useState(true);


    const handleClose = () => {
        setVariable(false);
        return setOpen(false);
    };

    return (
        <div>
            <Modal
                className={styles.logoutModal}
                open={open}
                onClose={() => handleClose()}>
                <Box
                    className={styles.logoutModalBox + " " + (theme === "dark" ? styles.darkLogoutModalBox : styles.lightLogoutModalBox)}>
                    <BlackText extraStyle={styles.headerStyle} text={headerText}/>
                    <div style={{marginTop: 20}}>
                        <BlackText
                            extraStyle={(theme === "dark" ? styles.darkLogoutMessageModal : styles.lightLogoutMessageModal)}
                            text={containText}/>
                    </div>
                    <div className={styles.logoutButtonsDiv}>
                        <BlueButton onClick={handleClose} buttonName={"Cancel"}
                                    buttonStyle={styles.buttonStyle}
                                    extraStyle={styles.logoutButtonsExtraStyle + " " + styles.cancelExtraStyle}/>
                        <BlueButton onClick={() => {
                            handleButtonClick()
                        }} buttonType={"contained"} buttonName={buttonText}
                                    buttonStyle={styles.buttonStyle}
                                    extraStyle={styles.logoutButtonsExtraStyle}/>
                    </div>
                </Box>
            </Modal>
        </div>
    );
}
