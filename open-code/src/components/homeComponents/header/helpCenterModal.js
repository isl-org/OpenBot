import React, {useContext} from "react";
import {ThemeContext} from "../../../App";
import {Box, Modal} from "@mui/material";
import styles from "../../navBar/navbar.module.css";
import {Images} from "../../../utils/images";
import BlackText from "../../fonts/blackText";
import {HelpCenterText} from "../../../utils/constants";

export function HelpCenterModal(props) {
    const {isHelpCenterModal, setIsHelpCenterModal} = props
    const theme = useContext(ThemeContext)
    const handleClose = () => {
        setIsHelpCenterModal(false)
    }
    return (
        <Modal
            style={{display: "flex", alignItems: "center", justifyContent: "center"}}
            open={isHelpCenterModal}>
            <Box
                className={styles.helpCenterModalBox + " " + (theme.theme === "dark" ? styles.darkHelpCenterModal : styles.lightHelpCenterModal)}>
                <div className={styles.dragAndDropDiv + " " + (theme.theme === "dark" && styles.darkModeRightBorder)}>
                    <img alt={"screen display"} src={Images.laptop} style={{width: "80%", marginTop: "15%"}}/>
                    <div style={{width: "80%", marginTop: "15%"}}>
                        <BlackText extraStyle={styles.integerNumber} text={1}/>
                        <div style={{marginLeft: "10%", paddingTop: "8%"}}>
                            <BlackText divStyle={{marginBottom: "12%"}} text={"Drag and Drop"}/>
                            <BlackText extraStyle={styles.helpCenterPara} text={HelpCenterText.dragAndDropFirstLine}/>
                            <BlackText extraStyle={styles.helpCenterPara} text={HelpCenterText.dragAndDropSecondLine}/>
                        </div>
                    </div>
                </div>
                <div>
                    <div
                        className={styles.saveAndDownloadDiv + " " + (theme.theme === "dark" && styles.darkModeBottomBorder)}>
                        <div className={styles.helpCenterCrossIconDiv}>
                            {
                                theme.theme === "dark" ?
                                    <img onClick={handleClose} alt={"cross icon"} className={styles.crossIcon}
                                         src={Images.darkCrossIcon}/> :
                                    <img onClick={handleClose} alt={"cross icon"} className={styles.crossIcon}
                                         src={Images.lightCrossIcon}/>
                            }
                        </div>
                        <div>
                            <BlackText text={2} extraStyle={styles.integerNumber}/>
                        </div>
                        <div style={{width: "70%", paddingLeft: "5%"}}>
                            <BlackText divStyle={{marginTop: "7%", marginBottom: "8%"}} text={"Save and Download"}/>
                            <BlackText extraStyle={styles.helpCenterPara}
                                       text={HelpCenterText.saveAndDownloadFirstLine}/>
                            <BlackText extraStyle={styles.helpCenterPara}
                                       text={HelpCenterText.saveAndDownloadSecondLine}/>
                        </div>
                    </div>
                    <div className={styles.connectAndDriveDiv}>
                        <div>
                            <BlackText text={3} extraStyle={styles.integerNumber}/>
                        </div>
                        <div style={{width: "70%", paddingLeft: "5%"}}>
                            <div style={{marginTop: "10%"}}>
                                <BlackText divStyle={{paddingTop: "5%", marginBottom: "8%"}}
                                           text={"Connect and Drive"}/>
                                <BlackText extraStyle={styles.helpCenterPara}
                                           text={HelpCenterText.connectAndDriveFirstLine}/>
                                <BlackText extraStyle={styles.helpCenterPara}
                                           text={HelpCenterText.connectAndDriveSecondLine}/>
                            </div>
                        </div>
                    </div>
                </div>
            </Box>
        </Modal>
    )
}