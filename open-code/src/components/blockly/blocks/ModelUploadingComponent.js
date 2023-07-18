import React, {useContext, useState} from "react";
import {Box, Modal} from "@mui/material";
import styles from "../../navBar/navbar.module.css";
import {Constants, errorToast, Themes} from "../../../utils/constants";
import {Images} from "../../../utils/images";
import LoaderComponent from "../../loader/loaderComponent";
import SimpleInputComponent from "../../inputComponent/simpleInputComponent";
import BlueButton from "../../buttonComponent/blueButtonComponent";
import {Alert} from "../../homeComponents/header/editProfileModal";
import {ThemeContext} from "../../../App";
import {colors} from "../../../utils/color";
import {StoreContext} from "../../../context/context";
import {getConfigData} from "../../../services/workspace";
import {uploadToGoogleDrive} from "../../../services/googleDrive";

export function ModelUploadingComponent(params) {

    const {isAIModelComponent, setIsAIModelComponent, file} = params
    const {theme} = useContext(ThemeContext);
    const {isOnline} = useContext(StoreContext);
    const localFileName = file?.name.replace(/\.[^/.]+$/, "");
    const [fileName, setFileName] = useState(localFileName ?? "");
    const [modelDetails, setModelDetails] = useState({
        displayName: localFileName,
        type: "DETECTOR",
        class: "AUTOPILOT_F",
        width: 322,
        height: 322
    })
    const handleClose = () => {
        setIsAIModelComponent(false)
    }

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

    function handleTypeChange(e) {
        setModelDetails({
            ...modelDetails,
            type: e
        })
    }

    function handleClassChange(e) {
        setModelDetails({
            ...modelDetails,
            class: e
        })
    }

    function handleWidthChange(e) {
        setModelDetails({
            ...modelDetails,
            width: parseInt(e)
        })
    }

    function handleHeightChange(e) {
        setModelDetails({
            ...modelDetails,
            height: parseInt(e)
        })
    }

    async function handleSubmit() {
        if (isOnline) {
            if (localStorage.getItem("isSigIn") === "true") {
                const data = {
                    fileData: file,
                    name: modelDetails.displayName
                }
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
                    await uploadToGoogleDrive(JSON.stringify(configData), Constants.json)
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
                overflow: "scroll"
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
                <div style={{display: "flex", paddingTop: "3%"}}>
                    <SimpleInputComponent inputType={"name and extension"} extraStyle={styles.inputExtraStyle}
                                          headStyle={styles.headStyle}
                                          value={fileName}
                                          onDataChange={handleTfliteNameChange}
                                          inputTitle={"Model Name"} extraInputStyle={styles.extraInputStyle}
                    />
                    <SimpleInputComponent inputType={"dropdown"} inputTitle={"Type"}
                                          onDataChange={handleTypeChange}
                                          extraStyle={styles.inputExtraStyle}/>
                </div>
                <div style={{display: "flex"}}>
                    <SimpleInputComponent inputType={"dropdown"} inputTitle={"Class"}
                                          extraStyle={styles.inputExtraStyle}
                                          onDataChange={handleClassChange}
                    />
                    <SimpleInputComponent inputType={"dimensions"} inputTitle={"Input(w × h)"}
                                          onWidthDataChange={handleWidthChange}
                                          onHeightDataChange={handleHeightChange}
                                          extraStyle={styles.inputExtraStyle}/>

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