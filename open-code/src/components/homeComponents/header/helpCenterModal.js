import React, {useContext} from "react";
import {ThemeContext} from "../../../App";
import {Box, Modal, useTheme} from "@mui/material";
import styles from "../../navBar/navbar.module.css";
import {Images} from "../../../utils/images";
import BlackText from "../../fonts/blackText";
import {HelpCenterText} from "../../../utils/constants";
import useMediaQuery from "@mui/material/useMediaQuery";

/**
 * HelpCenterModal contains static how to upload code details
 * @param props
 * @returns {JSX.Element}
 * @constructor
 */
export function HelpCenterModal(props) {
    const {isHelpCenterModal, setIsHelpCenterModal} = props
    const theme = useContext(ThemeContext)
    const themes = useTheme();
    const isMobile = useMediaQuery(themes.breakpoints.down('sm'));
    const handleClose = () => {
        setIsHelpCenterModal(false)
    }
    return (
        <Modal
            style={{display: "flex", alignItems: "center", justifyContent: "center", overflow: "scroll"}}
            open={isHelpCenterModal}>
            <Box
                className={styles.helpCenterModalBox + " " + (theme.theme === "dark" ? styles.darkHelpCenterModal : styles.lightHelpCenterModal)}>
                {isMobile ?
                    <div className={styles.helpDetailsModelDiv}>
                        <HelpDetails handleClose={handleClose}/>
                    </div>
                    : <HelpDetails handleClose={handleClose}/>}
            </Box>
        </Modal>
    )
}

/**
 * Help Details modal
 * @param params
 * @returns {JSX.Element}
 * @constructor
 */
function HelpDetails(params) {
    const {handleClose} = params
    const theme = useContext(ThemeContext)
    const themes = useTheme();
    const isMobile = useMediaQuery(themes.breakpoints.down('sm'));
    const tabletQuery = window.matchMedia("(min-width: 768px) and (max-width: 1024px)").matches;

    return (
        <>
            <div
                className={styles.dragAndDropDiv + " " + (theme.theme === "dark" && styles.darkModeRightBorder)}>
                <img alt={"screen display"} src={Images.laptop} className={styles.screenDisplayImage}/>
                <div className={styles.dragAndDropDivContent}>
                    <BlackText extraStyle={styles.integerNumber} text={1}/>
                    <div className={styles.dragAndDropPara}>
                        <BlackText divStyle={{marginBottom: isMobile ? "9%" : "12%"}}
                                   extrastyle={styles.helpCentreHeader}
                                   text={"Drag and Drop"}/>
                        <BlackText extraStyle={styles.helpCenterPara}
                                   text={HelpCenterText.dragAndDropFirstLine}/>
                        <BlackText extraStyle={styles.helpCenterPara}
                                   text={HelpCenterText.dragAndDropSecondLine}/>
                    </div>
                </div>
            </div>
            <div className={styles.helpCentreRightSide}>
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
                    <div className={styles.saveAndDownloadDivContent}>
                        <BlackText
                            divStyle={{marginTop: "7%", marginBottom: "8%"}}
                            text={"Save and Download"}
                            extrastyle={styles.helpCentreHeader}/>
                        <BlackText extraStyle={styles.helpCenterPara}
                                   text={HelpCenterText.saveAndDownloadFirstLine}/>
                        <BlackText extraStyle={styles.helpCenterPara}
                                   text={HelpCenterText.saveAndDownloadSecondLine}/>
                    </div>
                </div>
                <div className={styles.connectAndDriveDiv}>
                    <div>
                        <BlackText text={3} divStyle={{paddingTop:"20px" && tabletQuery}} extraStyle={styles.integerNumber}/>
                    </div>
                    <div className={styles.connectAndDriveDivPara}>
                        <div style={{marginTop: "10%"}}>
                            <BlackText divStyle={{paddingTop: "5%", marginBottom: "8%"}}
                                       text={"Connect and Drive"} extrastyle={styles.helpCentreHeader}/>
                            <BlackText extraStyle={styles.helpCenterPara}
                                       text={HelpCenterText.connectAndDriveFirstLine}/>
                            <BlackText extraStyle={styles.helpCenterPara}
                                       text={HelpCenterText.connectAndDriveSecondLine}/>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}