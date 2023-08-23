import React, {useContext, useEffect, useState} from "react";
import {Backdrop, Box, CircularProgress, Modal} from "@mui/material";
import styles from "../../navBar/navbar.module.css";
import {Constants, errorToast, localStorageKeys, Models, Themes} from "../../../utils/constants";
import {Images} from "../../../utils/images";
import SimpleInputComponent from "../../inputComponent/simpleInputComponent";
import BlueButton from "../../buttonComponent/blueButtonComponent";
import {ThemeContext} from "../../../App";
import {colors} from "../../../utils/color";
import {StoreContext} from "../../../context/context";
import {getConfigData, setConfigData} from "../../../services/workspace";
import {uploadToGoogleDrive} from "../../../services/googleDrive";

/**
 * function to upload new model (.tflite)
 * @param params
 * @returns {JSX.Element}
 * @constructor
 */
export function ModelUploadingComponent(params) {
    const {isAIModelComponent, setIsAIModelComponent, file} = params
    const {theme} = useContext(ThemeContext);
    const {isOnline} = useContext(StoreContext);
    const localFileName = file?.name.replace(/\.[^/.]+$/, "");
    const [fileName, setFileName] = useState(localFileName ?? "");
    const [isDesktopLargerScreen, setIsDesktopLargerScreen] = useState(window.matchMedia("(min-height: 900px)").matches);
    const [fileUploadLoader, setFileUploadLoader] = useState(false);
    const [modelClassDropdown, setModelClassDropdown] = useState("AUTOPILOT");
    const [modelDetails, setModelDetails] = useState({
        displayName: localFileName,
        type: "AUTOPILOT",
        class: "AUTOPILOT",
        width: 322,
        height: 322
    })
    const [handleTypeDependency, setHandleTypeDependency] = useState(["AUTOPILOT"]);

    //function to close the model
    const handleClose = () => {
        setIsAIModelComponent(false)
    }

    useEffect(() => {
        const handleOrientationChange = () => {
            setIsDesktopLargerScreen(
                window.matchMedia("(min-height: 900px)").matches
            );
        };
        window.addEventListener("resize", handleOrientationChange);
    }, []);

    //Loader on submit button
    function SimpleBackdrop() {
        return (
            <div>
                <Backdrop
                    sx={{color: colors.openBotBlue, zIndex: (theme) => theme.zIndex.drawer + 1}}
                    open={fileUploadLoader}
                >
                    <CircularProgress color="inherit"/>
                </Backdrop>
            </div>
        );
    }


    //function to handle file name
    function handleTfliteNameChange(e) {
        if (!(e.trim().length <= 0)) {
            setModelDetails({
                ...modelDetails,
                displayName: e
            })
            setFileName(e);
        } else {
            setFileName(localFileName)
        }
    }

    //function to handle model type and class dependency on type
    function handleTypeChange(e) {
        if (e === "AUTOPILOT") {
            setHandleTypeDependency([e]);
            setModelClassDropdown(e);
        } else if (e === "DETECTOR") {
            setHandleTypeDependency(["MOBILENET", "EFFICIENTDET", "YOLOV4", "YOLOV5"]);
            setModelClassDropdown("MOBILENET");
        } else if (e === "NAVIGATION") {
            setHandleTypeDependency([e]);
            setModelClassDropdown(e);
        }
        setModelDetails({
            ...modelDetails,
            type: e,
            class: modelClassDropdown
        })
    }

    useEffect(() => {
        setModelDetails({
            ...modelDetails,
            class: modelClassDropdown
        })
    }, [handleTypeDependency])

    //function to handle model class
    function handleClassChange(e) {
        setModelDetails({
            ...modelDetails,
            class: e
        })
    }

    //function to handle model width
    function handleWidthChange(e) {
        setModelDetails({
            ...modelDetails,
            width: parseInt(e)
        })
    }

    //function to handle model height
    function handleHeightChange(e) {
        setModelDetails({
            ...modelDetails,
            height: parseInt(e)
        })
    }


    /**
     * function to save and add new model
     * @returns {Promise<void>}
     */
    async function handleSubmit() {
        if (isOnline) {
            if (localStorage.getItem("isSigIn") === "true") {
                setFileUploadLoader(true);
                const data = {
                    fileData: file,
                    name: modelDetails.displayName
                }
                await setConfigData().then(async () => {
                    let configData = getConfigData()
                    await uploadToGoogleDrive(data, Constants.tflite).then(async (res) => {
                        let newModelData = {
                            id: configData.length + 1,
                            name: modelDetails.displayName + `.${Constants.tflite}`,
                            pathType: "URL",
                            path: res,
                            type: `${modelDetails.type}`,
                            class: `${modelDetails.class}`,
                            inputSize: `${modelDetails.width}x${modelDetails.height}`
                        }
                        configData.push(newModelData)
                        await uploadToGoogleDrive(JSON.stringify(configData), Constants.json).then(() => {
                            localStorage.setItem(localStorageKeys.configData, JSON.stringify(configData))
                            setFileUploadLoader(false);
                            handleClose()
                        })
                            .catch((err) => {
                                setFileUploadLoader(false);
                                console.log(err);
                            })
                    })
                        .catch((err) => {
                            setFileUploadLoader(false);
                            console.log(err);
                        })
                })
            } else {
                errorToast("Please sign-In to add model.")
            }
        } else {
            errorToast(Constants.InternetOffMsg)
        }
    }

    return (
        <Modal
            open={isAIModelComponent}
            style={{
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                overflow: "scroll",
            }}>
            <Box
                className={styles.editProfileModal + " " + (theme === Themes.dark && styles.darkEditProfileModal)}>
                <div className={styles.crossIconDiv}>
                    <img onClick={handleClose} alt={"cross icon"} className={styles.crossIcon}
                         src={theme === Themes.dark ? Images.darkCrossIcon : Images.lightCrossIcon}/>
                </div>
                {/* model heading */}
                <div className={styles.modelHeading}
                     style={{color: theme === Themes.dark ? colors.whiteFont : colors.blackFont}}>
                    Model Details
                </div>
                {fileUploadLoader && <SimpleBackdrop/>}
                <div style={{display: "flex", paddingTop: isDesktopLargerScreen && "3%"}}>
                    <SimpleInputComponent inputType={"text"} extraStyle={styles.inputExtraStyle}
                                          headStyle={styles.headStyle}
                                          value={fileName}
                                          inlineStyle={{
                                              height: "50%",
                                              backgroundColor: theme === "dark" ? "#414141" : colors.whiteBackground
                                          }}
                                          onDataChange={handleTfliteNameChange}
                                          modelExtension={true}
                                          inputTitle={"Model Name"} extraInputStyle={styles.extraInputStyle}
                    />
                    <SimpleInputComponent inputType={"dropdown"} inputTitle={"Type"}
                                          onDataChange={handleTypeChange} modelData={Models.type}
                                          extraStyle={styles.inputExtraStyle} extraInputStyle={styles.extraInputStyle}/>
                </div>
                <div style={{display: "flex"}}>
                    <SimpleInputComponent inputType={"dropdown"} inputTitle={"Class"}
                                          modelClassDropdown={modelClassDropdown}
                                          setModelClassDropdown={setModelClassDropdown}
                                          modelData={handleTypeDependency}
                                          extraStyle={styles.inputExtraStyle}
                                          onDataChange={handleClassChange}
                                          extraInputStyle={styles.extraInputStyle}
                    />
                    <SimpleInputComponent inputType={"dimensions"} inputTitle={"Input(w × h)"}
                                          onWidthDataChange={handleWidthChange}
                                          onHeightDataChange={handleHeightChange}
                                          extraInputStyle={styles.extraInputStyle}
                                          extraStyle={styles.inputExtraStyle} extraMargin={styles.dropdownMargin}/>

                </div>
                <div className={styles.buttonSection}>
                    <BlueButton buttonType={"contained"} buttonName={"Save"} onClick={handleSubmit}
                                buttonStyle={styles.buttonText}/>
                    <BlueButton onClick={handleClose} buttonName={"Cancel"} buttonStyle={styles.buttonText}/>
                </div>
            </Box>

        </Modal>
    )
}