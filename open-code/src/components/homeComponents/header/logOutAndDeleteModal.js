import React, {useContext, useState} from "react";
import {ThemeContext} from "../../../App";
import {Box, Modal} from "@mui/material";
import styles from "../../navBar/navbar.module.css";
import BlackText from "../../fonts/blackText";
import BlueButton from "../../buttonComponent/blueButtonComponent";
import {googleSignOut} from "../../../services/firebase";
import {useNavigate} from "react-router-dom";
import {StoreContext} from "../../../context/context";
import {deleteProject} from "../../../services/workspace";

export function LogOutModal(props) {
    const {setIsLogoutModal, isLogoutModal} = props
    const {theme} = useContext(ThemeContext)

    const handleClose = () => {
        setIsLogoutModal(false)
    }
    return (
        <Modal
            className={styles.logoutModal}
            open={isLogoutModal}
            onClose={() => handleClose()}>
            <Box
                className={styles.logoutModalBox + " " + (theme === "dark" ? styles.darkLogoutModalBox : styles.lightLogoutModalBox)}>
                <BlackText text={"Confirm Logout"}/>
                <div style={{marginTop: 20}}>
                    <BlackText
                        extraStyle={(theme === "dark" ? styles.darkLogoutMessageModal : styles.lightLogoutMessageModal)}
                        text={"Are you sure you want to logout?"}/>
                </div>
                <div className={styles.logoutButtonsDiv}>
                    <BlueButton onClick={handleClose} buttonName={"Cancel"}
                                extraStyle={styles.logoutButtonsExtraStyle}/>
                    <BlueButton onClick={() => {
                        googleSignOut().then(() => {
                            handleClose()
                        })
                    }} buttonType={"contained"} buttonName={"Ok"}
                                extraStyle={styles.logoutButtonsExtraStyle}/>
                </div>
            </Box>
        </Modal>
    )
}


export function DeleteModel(props) {
    const {theme} = useContext(ThemeContext)
    let navigate = useNavigate();
    const {setDeleteProject} = props;
    const [open, setOpen] = useState(true);
    const {currentProjectId} = useContext(StoreContext);
    const handleClose = () => {
        setDeleteProject(false);
        return setOpen(false);
    };
    const handleDeleteProject = () => {
        deleteProject(currentProjectId).then();
        let path = `/`;
        navigate(path);
    }

    return (
        <div>
            <Modal
                className={styles.logoutModal}
                open={open}
                onClose={() => handleClose()}>
                <Box
                    className={styles.logoutModalBox + " " + (theme === "dark" ? styles.darkLogoutModalBox : styles.lightLogoutModalBox)}>
                    <BlackText extraStyle={styles.headerStyle} text={"Delete this file?"}/>
                    <div style={{marginTop: 20}}>
                        <BlackText
                            extraStyle={(theme === "dark" ? styles.darkLogoutMessageModal : styles.lightLogoutMessageModal)}
                            text={"You cannot restore this file later."}/>
                    </div>
                    <div className={styles.logoutButtonsDiv}>
                        <BlueButton onClick={handleClose} buttonName={"Cancel"}
                                    extraStyle={styles.logoutButtonsExtraStyle}/>
                        <BlueButton onClick={() => {
                            handleDeleteProject()
                        }} buttonType={"contained"} buttonName={"Delete"}
                                    extraStyle={styles.logoutButtonsExtraStyle}/>
                    </div>
                </Box>
            </Modal>
        </div>
    );
}
