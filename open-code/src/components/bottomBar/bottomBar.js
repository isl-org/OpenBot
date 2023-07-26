import React, {useContext, useEffect, useRef, useState} from "react";
import Blockly from "blockly/core";
import styles from "./style.module.css"
import {javascriptGenerator} from 'blockly/javascript';
import {StoreContext} from "../../context/context";
import {colors} from "../../utils/color";
import {ThemeContext} from "../../App";
import {Constants, Errors, errorToast} from "../../utils/constants";
import {
    CircularProgress,
    circularProgressClasses,
    Popper,
    useTheme
} from "@mui/material";
import WhiteText from "../fonts/whiteText";
import BlackText from "../fonts/blackText";
import {Images} from "../../utils/images";
import useMediaQuery from "@mui/material/useMediaQuery";
import {uploadToGoogleDrive} from "../../services/googleDrive";
import {getCurrentProject} from "../../services/workspace";
import navbarStyle from "../navBar/navbar.module.css"
import BlueText from "../fonts/blueText";
import {ModelUploadingComponent} from "../blockly/blocks/ModelUploadingComponent";

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
    const isMobile = useMediaQuery(themes.breakpoints.down('sm'));
    const [isLandscape, setIsLandscape] = useState(window.matchMedia("(max-height: 500px) and (max-width: 1000px) and (orientation: landscape)").matches);
    const isDesktopSmallerScreen = useMediaQuery(themes.breakpoints.down('md'));
    const [isAIModelComponent, setIsAIModelComponent] = useState(false);
    const [file, setFile] = useState(null);

    const [error, setError] = useState("");
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
                const objectTracking = workspace.getBlocksByType("objectTracking")
                const autopilot = workspace.getBlocksByType("autopilot")
                const objectTrackingConditional = workspace.getBlocksByType("objectTrackingConditional")
                let objectTrackingConditionEnabled = objectTrackingConditional?.filter(obj => obj.disabled === false)
                let objectTrackingEnabledBlocks = objectTracking?.filter(obj => obj.disabled === false) //filtering objectTracking connected blocks
                let autopilotEnabledBlocks = autopilot?.filter(obj => obj.disabled === false) //filtering autopilot connected blocks

                if ((start.length === 0 && forever.length === 0)) {
                    setDrawer(false);
                    setIsLoader(false);
                    setIsError(true);
                    setError(Errors.error1)
                } else if ([objectTrackingEnabledBlocks?.length > 0, autopilotEnabledBlocks?.length > 0, objectTrackingConditionEnabled?.length > 0].filter(Boolean).length > 1) {
                    setDrawer(false);
                    setIsLoader(false);
                    setIsError(true);
                    setError(Errors.error2)
                } else {
                    if (start.length > 0)
                        code += "\nstart();";
                    if (forever.length > 0)
                        code += "\nforever();";
                    setGenerateCode(!generate);
                    console.log(code);
                    uploadToGoogleDrive(code, "js").then((res) => {
                            let linkCode = {
                                driveLink: res,
                                projectName: getCurrentProject().projectName
                            }
                            const data = {
                                projectName: getCurrentProject().projectName,
                                xmlValue: getCurrentProject().xmlValue,
                                createdDate: new Date().toLocaleDateString() // Todo on create button add newly created date and time
                            }
                            uploadToGoogleDrive(data, "xml")   // Call function to upload xml data to Google Drive
                                .then()
                                .catch((err) => {
                                    errorToast("Failed to upload");
                                    console.log(err)
                                    setIsLoader(false);
                                })
                            setCode(linkCode);
                            setIsLoader(false);
                            setCategory(Constants.qr);
                            setDrawer(true);
                        }
                    ).catch((err) => {
                        console.log("err::", err)
                        setIsLoader(false);
                        errorToast("Failed to Upload");
                    })


                }
            } else {
                errorToast("Please sign-In to upload code.")
            }
        } else {
            errorToast(Constants.InternetOffMsg)
        }
    };

    useEffect(() => {
        const handleOrientationChange = () => {
            setIsLandscape(
                window.matchMedia("(max-height: 500px) and (max-width: 1000px) and (orientation: landscape)").matches
            );
        };
        window.addEventListener("resize", handleOrientationChange);
    }, []);

    //handle click  on bottom bar button event which affect workspace
    const clickedButton = (e) => {
        const {name} = e.target;
        setButtonSelected(name);

        //to verify if undoStack has items, or if both undo and redo are empty (first Project)
        const isUndo = () => {
            const workspace = Blockly.getMainWorkspace();
            if (workspace) {
                return workspace.undoStack_.length !== 0 ||
                    (workspace.undoStack_.length === 0 && workspace.redoStack_.length === 0);
            }
            return false;
        };

        //to verify if redoStack has items
        const isRedo = () => {
            const workspace = Blockly.getMainWorkspace();
            if (workspace) {
                return workspace.redoStack_.length !== 0;
            }
            return false;
        };

        switch (name) {
            case "redo": {
                const workspace = Blockly.getMainWorkspace();
                if (!workspace) {
                    return;
                }
                if (isRedo()) {
                    workspace.undo(true);
                }
                break;
            }
            case "undo": {
                if (isUndo()) {
                    const workspace = Blockly.getMainWorkspace();
                    if (workspace && workspace.getUndoStack().length === 0) {
                        return;
                    }
                    workspace.undo(false);
                }
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
                    <div className={styles.errorItems}>error 1 : &nbsp;&nbsp; {error}</div>
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
                                    buttonActive={buttonActive} clickedButton={clickedButton}
                                    setIsAIModelComponent={setIsAIModelComponent} setFile={setFile}/>
                {/*model upload pop up */}
                {
                    isAIModelComponent &&
                    <ModelUploadingComponent isAIModelComponent={isAIModelComponent}
                                             setIsAIModelComponent={setIsAIModelComponent} file={file}/>
                }

                <div className={styles.operationsDiv}>
                    {/*undo redo*/}
                    <UndoRedo clickedButton={clickedButton} buttonSelected={buttonSelected}
                              buttonActive={buttonActive}/>
                    {/*zoom in out*/}
                    {isMobile || isLandscape || isDesktopSmallerScreen ? "" :
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
    const {generateCode, buttonSelected, buttonActive, setDrawer, setIsAIModelComponent, setFile} = params
    const themes = useTheme();
    const {category, setCategory} = useContext(StoreContext);
    const isMobile = useMediaQuery(themes.breakpoints.down('sm'));
    const [language, setLanguage] = useState(category === Constants.py ? Constants.py : Constants.js);
    const theme = useContext(ThemeContext);
    const [anchorEl, setAnchorEl] = useState(null);
    const [openPopupArrow, setOpenPopupArrow] = useState(false);
    const [arrowClick, setArrowClicked] = useState(false);
    const [isTabletQuery, setIsTabletQuery] = useState(window.matchMedia("(min-width: 768px) and (max-width: 1024px)").matches);
    const popUpRef = useRef(null);// Create a reference to the popup element for detecting clicks outside the popup.
    const [isLandscape, setIsLandscape] = useState(window.matchMedia("(max-height: 500px) and (max-width: 1000px) and (orientation: landscape)").matches);
    const isDesktopSmallerScreen = useMediaQuery(themes.breakpoints.down('lg'));
    const inputRef = useRef();

    const handleClick = (event) => {
        event.stopPropagation();
        setOpenPopupArrow(!openPopupArrow);
        setAnchorEl(anchorEl ? null : event.currentTarget);
        setArrowClicked(true);
    };
    const id = openPopupArrow ? 'simple-popper' : undefined;

    const handleLanguageDropDown = (lang) => {
        setLanguage(lang);
        setCategory(lang);
        setDrawer(true);
        setAnchorEl(null);
        setOpenPopupArrow(!openPopupArrow);
    }

    const handleChange = (e) => {
        setFile(e.target.files[0])
        setIsAIModelComponent(true)
    }

    useEffect(() => {
        const handleOrientationChange = () => {
            setIsLandscape(
                window.matchMedia("(max-height: 500px) and (max-width: 1000px) and (orientation: landscape)").matches
            );
            setIsTabletQuery(
                window.matchMedia("(min-width: 768px) and (max-width: 1024px)").matches
            );
        };
        window.addEventListener("resize", handleOrientationChange);
    }, []);

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (popUpRef.current && !popUpRef.current.contains(event.target)) {
                setOpenPopupArrow(false);
                setAnchorEl(null);
            }
        };
        const handleMouseDown = (event) => {
            if (popUpRef.current && !popUpRef.current.contains(event.target)) {
                setArrowClicked(false); // Reset arrowClicked back to false
            }
        };
        document.addEventListener("mousedown", handleMouseDown, {passive: true});
        document.addEventListener("click", handleClickOutside, {passive: true});
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, [popUpRef, arrowClick]);

    return (
        <div className={styles.iconMargin + " " + styles.noSpace}
             style={{width: "25%"}}>
            {/*generate QR code*/}
            <button
                className={`${styles.uploadCodeButton} ${buttonSelected === "uploadCode" && buttonActive ? styles.buttonColor : ""}`}
                name={"uploadCode"} onClick={generateCode}>
                {isMobile || isLandscape || isTabletQuery.matches || isDesktopSmallerScreen ? (
                    ""
                ) : (
                    <span className={styles.leftButton + " " + styles.iconMargin}>Upload Code </span>
                )}
                <img alt={""}
                     className={styles.iconDiv} src={Images.uploadIcon}/>
            </button>
            <div
                className={`${styles.features}`} ref={popUpRef} onClick={handleClick}>
                <img alt={"features"} className={styles.iconDiv} src={Images.darkDots}/>
            </div>

            <Popper ref={popUpRef} id={id} open={openPopupArrow} anchorEl={anchorEl}>
                <div
                    className={`${styles.langOption} ${(theme.theme === "dark" ? styles.darkTitleModel : styles.lightTitleModel)}`}>
                    <div onClick={(event) => {
                        handleLanguageDropDown(Constants.js, event)
                    }}
                         className={`${styles.langItem} ${styles.jsDivMargin}  ${(theme.theme === "dark" ? navbarStyle.darkItem : navbarStyle.lightItem)}`}
                    >
                        <img alt="Icon" className={styles.langIcon}
                             src={theme.theme === "dark" ? Images.jsIconDarkTheme : Images.jsIconLightTheme}/>
                        {theme.theme === "dark" ?
                            <WhiteText inlineStyle={{fontWeight: 400}} text={"Javascript"}/> :
                            <BlueText text={"Javascript"}/>
                        }
                    </div>
                    <div onClick={(event) => handleLanguageDropDown(Constants.py, event)}
                         className={`${styles.langItem} ${styles.pyDivMargin} ${(theme.theme === "dark" ? navbarStyle.darkItem : navbarStyle.lightItem)}`}
                    >
                        <img alt="Icon" className={styles.langIcon}
                             src={theme.theme === "dark" ? Images.pyIconDarkTheme : Images.pyIconLightTheme}/>
                        {theme.theme === "dark" ?
                            <WhiteText extraStyle={styles.pyText} text={"Python"}/> :
                            <BlueText extraStyle={styles.pyText} text={"Python"}/>
                        }
                    </div>
                    <div onClick={() => inputRef.current?.click()}
                         style={{marginTop: 0}}
                         className={`${styles.langItem} ${styles.jsDivMargin} ${(theme.theme === "dark" ? navbarStyle.darkItem : navbarStyle.lightItem)}`}
                    >
                        <img alt="Icon" className={styles.langIcon}
                             src={theme.theme === "dark" ? Images.darkPlusIcon : Images.lightPlusIcon}/>
                        {theme.theme === "dark" ?
                            <WhiteText text={"Add Model"}/> :
                            <BlueText text={"Add Model"}/>
                        }
                        <input ref={inputRef} style={{display: "none"}} type="file"
                               accept=".tflite"
                               onChange={handleChange}/>

                    </div>
                </div>
            </Popper>
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
