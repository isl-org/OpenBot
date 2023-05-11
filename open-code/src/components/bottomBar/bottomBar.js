import React, {useContext, useEffect, useState} from "react";
import Blockly from "blockly/core";
import styles from "./style.module.css"
import {javascriptGenerator} from 'blockly/javascript';
import {StoreContext} from "../../context/context";
import {colors} from "../../utils/color";
import {ThemeContext} from "../../App";
import {Constants, errorToast} from "../../utils/constants";
import {CircularProgress, circularProgressClasses, FormControl, MenuItem, Select, useTheme} from "@mui/material";
import WhiteText from "../fonts/whiteText";
import BlackText from "../fonts/blackText";
import {Images} from "../../utils/images";
import {motion, AnimatePresence} from "framer-motion";
import useMediaQuery from "@mui/material/useMediaQuery";
import {uploadToGoogleDrive} from "../../services/googleDrive";
import {getCurrentProject} from "../../services/workspace";


/**
 * Bottom Bar contains generate code, upload on drive icon , zoom in-out and undo redo functionality.
 * @returns {JSX.Element}
 * @constructor
 */
export const BottomBar = () => {
    const [buttonSelected, setButtonSelected] = useState({backgroundColor: colors.openBotBlue});
    const [buttonActive, setButtonActive] = useState(false);
    const [isLoader, setIsLoader] = useState(false);
    const {theme} = useContext(ThemeContext);
    const themes = useTheme();
    const isMobile = useMediaQuery(themes.breakpoints.down('md'));
    const isMobileLandscape = window.matchMedia("(max-height:440px) and (max-width: 1000px) and (orientation: landscape)").matches

    const {
        isOnline,
        generate,
        setGenerateCode,
        setCode,
        setDrawer,
        workspace,
        isError,
        setIsError, setCategory,
    } = useContext(StoreContext);

    //generate javascript or python code and upload to google drive
    const generateCode = () => {
        if (isOnline) {
            if (localStorage.getItem("isSigIn") === "true") {
                setDrawer(false);
                setIsLoader(true);
                //javaScript generator
                let code = javascriptGenerator.workspaceToCode(
                    workspace
                );

                const start = workspace.getBlocksByType("start")
                const forever = workspace.getBlocksByType("forever")
                if (start.length === 0 && forever.length === 0) {
                    setDrawer(false);
                    setIsLoader(false);
                    setIsError(true);
                } else {
                    if (start.length > 0)
                        code += "\nstart();";
                    if (forever.length > 0)
                        code += "\nforever();";
                    setGenerateCode(!generate);
                    console.log(code);
                    uploadToGoogleDrive(code, "js").then((res) => {
                            console.log("res::", res)
                            setCode(res);
                            setIsLoader(false);
                            setDrawer(true);
                            setCategory(Constants.qr);
                        }
                    ).catch((err) => {
                        console.log("err::", err)
                        setIsLoader(false);
                        errorToast("Failed to Upload");

                    })
                }
            } else {
                errorToast("Please sign-In to generate QR.")
            }
        } else {
            errorToast(Constants.InternetOffMsg)
        }
    };


    //handle click  on bottom bar button event which affect workspace
    const clickedButton = (e) => {
        const {name} = e.target;
        setButtonSelected(name);
        switch (name) {
            case "redo": {
                Blockly.getMainWorkspace().undo(true);
                break;
            }
            case "undo": {
                Blockly.getMainWorkspace().undo(false);
                break;
            }
            case "minus": {
                Blockly.getMainWorkspace().zoom(1, 2, -1.5);
                break;
            }
            case "plus": {
                Blockly.getMainWorkspace().zoom(1, 2, 1.5);
                break;
            }
            default: {
                break;
            }
        }
        setButtonActive(true);
        setTimeout(() => {
            setButtonActive(false);
        }, 100);

    };

    //loader when uploading to google drive.
    const CompilationLoader = () => {
        return <div>
            <CircularProgress
                variant="determinate"
                sx={{
                    color: theme === 'light' ? "#E8E8E8" : "gray",
                }}
                size={isMobile ? 20 : 40}
                thickness={6.5}
                value={100}
                style={{position: "absolute"}}
            />
            <CircularProgress
                variant="indeterminate"
                disableShrink
                sx={{
                    color: theme === 'light' ? 'black' : '#FFFFFF',
                    animationDuration: '550ms',
                    left: 0,
                    [`& .${circularProgressClasses.circle}`]: {
                        strokeLinecap: 'round',
                    },
                }}
                size={isMobile ? 20 : 40}
                thickness={6.5}
            />
        </div>
    }

    return (
        <div
            className={isLoader || isError ? styles.loaderBarDiv + " " + (theme === "dark" ? styles.barDivDark : styles.barDivLight) : styles.barDiv + " " + (theme === "dark" ? styles.barDivDark : styles.barDivLight)}>
            <div>
                {isError && <div style={{display: "flex", flexDirection: "column"}}
                                 className={styles.errorDiv}>
                    <div>Compilation failed due to following error(s).</div>
                    <div className={styles.errorItems}>error 1 : &nbsp;&nbsp; No start or forever block
                        present in the playground.
                    </div>
                </div>
                }
                {isLoader &&
                    <div style={{display: "flex", flexDirection: "column"}} className={styles.loaderText}>
                        <CompilationLoader/>
                        {theme === "dark" ?
                            <WhiteText text={"Compiling Code..."} extraStyle={styles.textItem}/> :
                            <BlackText text={"Compiling Code..."} extraStyle={styles.textItem}/>}
                    </div>
                }
            </div>
            <div className={styles.buttonsDiv}>
                {/*generate code*/}
                <GenerateCodeButton buttonSelected={buttonSelected} generateCode={generateCode} setDrawer={setDrawer}
                                    buttonActive={buttonActive} clickedButton={clickedButton}/>

                <div className={styles.operationsDiv}>
                    {/*upload icon*/}
                    <UploadInDrive/>
                    {/*undo redo*/}
                    <UndoRedo clickedButton={clickedButton} buttonSelected={buttonSelected}
                              buttonActive={buttonActive}/>
                    {/*zoom in out*/}
                    {isMobile || isMobileLandscape ? "" :
                        <ZoomInOut clickedButton={clickedButton} buttonSelected={buttonSelected}
                                   buttonActive={buttonActive}/>}
                </div>
            </div>
        </div>
    );
}


/**
 * Generate Code Button
 * @param params
 * @returns {JSX.Element}
 * @constructor
 */
function GenerateCodeButton(params) {
    const {generateCode, buttonSelected, clickedButton, buttonActive, setDrawer} = params
    const themes = useTheme();
    const {setCategory} = useContext(StoreContext);
    const isMobile = useMediaQuery(themes.breakpoints.down('md'));
    const isMobileLandscape = window.matchMedia("(max-height:440px) and (max-width: 1000px) and (orientation: landscape)").matches
    const [language, setLanguage] = useState(Constants.js);

    const handleDropDown = (event) => {
        setLanguage(event.target.value);
        setCategory(event.target.value);
        setDrawer(true);

    }


    return (
        <div className={styles.iconMargin + " " + styles.noSpace}>
            <button
                className={`${styles.uploadCodeButton} ${buttonSelected === "uploadCode" && buttonActive ? styles.buttonColor : ""}`}
                name={"uploadCode"} onClick={generateCode}>
                <img alt={""}
                     className={styles.iconDiv} src={Images.uploadIcon}/>
                {isMobile || isMobileLandscape ? (
                    ""
                ) : (
                    <span className={styles.leftButton + " " + styles.iconMargin}>generate QR </span>
                )}
            </button>
            <FormControl
                variant="standard"
                sx={{
                    "& .MuiInputBase-root": {
                        fontfamily: "Gilroy-Medium, sans-serif",
                        width: "120px",
                        color: "white",
                        border: "none",
                        '&:before': {
                            border: 'none'
                        },
                        '&:after': {
                            border: 'none'
                        },
                        '&:hover:not(.Mui-disabled):before': {
                            border: 'none'
                        }
                    },
                    '& .MuiMenuItem-root': {
                        color: '#000',
                        fontSize: '20px',
                        padding: '10px',
                        '&:hover': {
                            backgroundColor: '#f5f5f5'
                        }
                    },
                    '& .MuiSelect-icon': {
                        color: 'white'
                    }
                }}
                className={`${styles.uploadCodeButton} ${styles.language}`}>
                <Select
                    value={language}
                    onChange={(e) => handleDropDown(e)}
                    displayEmpty
                    inputProps={{'aria-label': 'Without label'}}
                >
                    <MenuItem value={Constants.js}>
                        <em>JavaScript</em>
                    </MenuItem>
                    <MenuItem value={Constants.py}> Python </MenuItem>
                </Select>
            </FormControl>
        </div>
    )
}


/**
 * Undo redo
 */
function UndoRedo(params) {
    const {clickedButton, buttonSelected, buttonActive} = params
    return (
        <div className={styles.buttonMargin + " " + styles.iconMargin}>
            <button
                onClick={clickedButton}
                className={`${styles.buttonStyle} ${styles.minusStyle} ${styles.borderStyle} ${buttonSelected === "undo" && buttonActive ? styles.buttonColor : ""}`}
                name={"undo"}>
                <img alt={""} className={styles.commandSize} src={Images.undoIcon}/>
            </button>
            <button onClick={clickedButton}
                    className={`${styles.buttonStyle} ${styles.plusStyle} ${buttonSelected === "redo" && buttonActive ? styles.buttonColor : ""}`}
                    name={"redo"}>
                <img alt={""} className={styles.commandSize} src={Images.redoIcon}/>
            </button>
        </div>
    )
}


/**
 * Component to handle the uploading of an XML file to Google Drive.
 * Shows a button with a cloud icon, which will trigger the upload when clicked.
 * If the user is not signed in, a sign in popup will be shown.
 * While the upload is in progress, a loader will be shown.
 * After the upload is complete, a tick animation will be shown briefly.
 * @param {Function} params.setSignInPopUp - A function to set the sign in popup state.
 * @param {Boolean} params.signInPopUp - A boolean to determine if the sign in popup should be shown.
 * @returns {JSX.Element}
 */
function UploadInDrive(params) {
    const [isDriveLoader, setIsDriveLoader] = useState(false);
    const [showTick, setShowTick] = useState(false);
    const {isOnline} = useContext(StoreContext)

    //if signIn add code in xml file to google drive or else show signIn pop Up
    const handleDriveButton = () => {
        if (isOnline) {
            if (localStorage.getItem("isSigIn") === "true") {
                setIsDriveLoader(true);
                const data = {
                    projectName: getCurrentProject().projectName,
                    xmlValue: getCurrentProject().xmlValue,
                    createdDate: new Date().toLocaleDateString() // Todo on create button add newly created date and time
                }

                // Call function to upload data to Google Drive
                uploadToGoogleDrive(data, "xml")
                    .then((res) => {
                            setIsDriveLoader(false);
                            //after response show tick after 400ms
                            res && setTimeout(() => {
                                setShowTick(true);
                            }, 400);
                        }
                    )
                    .catch((err) => {
                        setIsDriveLoader(false);
                        errorToast("Failed to upload");
                        console.log(err)
                    })
            } else {
                // If user is not signed in, show sign in alert
                errorToast("Please sign in to save project in drive.")
            }
        } else {
            errorToast(Constants.InternetOffMsg)
        }

    }

    // Remove tick animation after 1000ms
    useEffect(() => {
        if (showTick) {
            const timeout = setTimeout(() => {
                setShowTick(false);
            }, 1000);
            return () => clearTimeout(timeout);
        }
    }, [showTick]);

    //tick animation
    const tickVariants = {
        hidden: {
            pathLength: 0,
            opacity: 0
        },
        visible: {
            pathLength: 1,
            opacity: 1,
            transition: {
                duration: 0.7,
                ease: "easeInOut"
            }
        }
    };

    //drive loader
    const DriveLoader = () => {
        return <div>
            <CircularProgress
                sx={{
                    color: "#0071C5",
                }}
                size={40}
                thickness={2.5}
                value={100}
            />
        </div>
    }

    return (
        <>

            {/* Upload button */}
            <div onClick={() => handleDriveButton()} className={styles.iconMargin + " " + styles.iconSpace}
                 style={{display: "flex", alignItems: "center"}}>

                {/*icon*/}
                <img alt="drive" className={isDriveLoader ? styles.shrinkDriveIcon : styles.driveIconStyle}
                     src={Images.cloud} style={isDriveLoader ? {position: "absolute", marginLeft: 6} : {}}/>

                {/*loader*/}
                <div>{isDriveLoader && <DriveLoader/>}</div>

                {/*tick animation*/}
                <AnimatePresence>
                    {showTick && (
                        <motion.svg
                            key="tick"
                            width="30"
                            height="30"
                            viewBox="0 0 50 50"
                            style={{
                                position: "absolute",
                                marginLeft: 2.3,
                            }}
                            initial={{opacity: 0, scale: 0.2}}
                            animate={{opacity: 1, scale: 1}}
                            exit={{opacity: 0, scale: 0.2}}
                        >
                            <motion.path
                                d="M15.63 27.077l6.842 6.84L34.923 20"
                                fill="transparent"
                                strokeWidth="5"
                                stroke="#fff"
                                variants={tickVariants}
                                initial="hidden"
                                animate="visible"
                            />
                        </motion.svg>
                    )}
                </AnimatePresence>

            </div>
            {/*// <button className={styles.driveStyle + " " + styles.iconMargin}*/}
        </>
    )
}


/**
 Component for zooming in and out of an image or content.
 @param {Function} params.clickedButton - The function to be called when a zoom button is clicked.
 @param {string} params.buttonSelected - The currently selected zoom button ('plus' or 'minus').
 @param {boolean} params.buttonActive - A flag indicating whether or not the zoom buttons are active.
 @returns {JSX.Element}
 */
function ZoomInOut(params) {
    const {clickedButton, buttonSelected, buttonActive} = params;

    return (
        <div className={styles.iconMargin}>
            <button onClick={clickedButton}
                    className={`${styles.buttonStyle} ${styles.minusStyle} ${styles.borderStyle} ${buttonSelected === "minus" && buttonActive ? styles.buttonColor : ""}`}
                    name={"minus"}>
                <span className={styles.operationSize}>-</span>
            </button>
            <button onClick={clickedButton}
                    className={`${styles.buttonStyle} ${styles.plusStyle} ${buttonSelected === "plus" && buttonActive ? styles.buttonColor : ""}`}
                    name={"plus"}>
                <span className={styles.operationSize}>+</span>
            </button>
        </div>
    );
}
