import React, {useContext, useRef, useState} from "react";
import Blockly from "blockly/core";
import uploadIcon from "../../assets/images/icon/upload-cloud.png"
import {UploadBarStyle} from "./styles";
import styles from "./style.module.css"
import undoIcon from "../../assets/images/icon/undo.png";
import redoIcon from "../../assets/images/icon/redo.png";
import driveIcon from "../../assets/images/icon/drive.png";
import {javascriptGenerator} from 'blockly/javascript';
import {StoreContext} from "../../context/context";
import {colors} from "../../utils/color";
import driveIconClicked from "../../assets/images/icon/drive-clicked.png"
import {ThemeContext} from "../../App";
import {getCurrentProject} from "../../services/workspace";
import {uploadToGoogleDrive} from "../../services/googleDrive";


export const UploadCode = () => {
    const [buttonSelected, setButtonSelected] = useState({backgroundColor: colors.openBotBlue});
    const [buttonActive, setButtonActive] = useState(false);
    const [driveButtonActive, setDriveButtonActive] = useState(false);
    const {theme} = useContext(ThemeContext);
    const {generate, setGenerateCode,setCode,setDrawer,setFileId} = useContext(StoreContext);
    let primaryWorkspace = useRef();

    const generateCode = () => {
        const code = javascriptGenerator.workspaceToCode(
            primaryWorkspace.current
        );
        console.log(code+"\nstart()\n"+"forever()\n");
        setGenerateCode(!generate);
        setCode(code+"\nstart()\n"+"forever()\n");
        setDrawer(true);

    };

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


    /**
     * save projects on Google Drive
     */
    const handleDriveButton = () => {
        const data = {
            projectName: getCurrentProject().projectName,
            xmlValue: getCurrentProject().xmlValue,
            time: getCurrentProject().time,
            id: getCurrentProject().id,
            fileId: getCurrentProject().fileId,// require to check if already exist in folder or not
            createdDate: new Date().toLocaleDateString() // Todo on create button add newly created date and time
        }
        //upload on google drive
        uploadToGoogleDrive(data,setFileId).then();
        setDriveButtonActive(true);
        setTimeout(() => {
            setDriveButtonActive(false);
        }, 100);
    }

    return (
        <div className={styles.barDiv + " " + (theme === "dark" ? styles.barDivDark : styles.barDivLight)}>
            <UploadCodeButton buttonSelected={buttonSelected} generateCode={generateCode}
                              buttonActive={buttonActive} clickedButton={clickedButton}/>
            <div className={styles.operationsDiv}>
                <UploadInDrive handleDriveButton={handleDriveButton} driveButtonActive={driveButtonActive}/>
                <UndoRedo clickedButton={clickedButton} buttonSelected={buttonSelected}
                          buttonActive={buttonActive}/>
                <ZoomInOut clickedButton={clickedButton} buttonSelected={buttonSelected}
                           buttonActive={buttonActive}/>
            </div>
        </div>
    );
}


/**
 * Upload Code Button
 * @param params
 * @returns {JSX.Element}
 * @constructor
 */
function UploadCodeButton(params) {
    const {generateCode, buttonSelected, clickedButton, buttonActive} = params
    return (

        <div className={styles.iconMargin} onClick={generateCode}>
            <button className={styles.uploadCodeButton}
                    style={{opacity: buttonSelected === "uploadCode" && buttonActive ? UploadBarStyle.buttonColor.opacity : ""}}
                    name={"uploadCode"} onClick={clickedButton}>
                <span className={styles.leftButton + " " + styles.iconMargin}>Upload Code</span>
                <img alt={""}
                     className={styles.iconDiv + " " + styles.iconMargin} src={uploadIcon}/>
            </button>
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
                style={{
                    ...UploadBarStyle.buttonStyle, ...UploadBarStyle.undoButtonStyle, ...UploadBarStyle.borderStyle,
                    opacity: buttonSelected === "undo" && buttonActive ? UploadBarStyle.buttonColor.opacity : ""
                }} name={"undo"}>
                <img alt={""} className={styles.commandSize} src={undoIcon}/>
            </button>
            <button onClick={clickedButton} style={{
                ...UploadBarStyle.buttonStyle, ...UploadBarStyle.plusStyle,
                opacity: buttonSelected === "redo" && buttonActive ? UploadBarStyle.buttonColor.opacity : ""
            }}
                    name={"redo"}>
                <img alt={""} className={styles.commandSize} src={redoIcon}/>
            </button>
        </div>
    )
}

function UploadInDrive(params) {
    const {handleDriveButton, driveButtonActive} = params
    return (
        <button className={styles.driveStyle + " " + styles.iconMargin}
                onClick={handleDriveButton}>
            <img alt={""} className={styles.driveIconStyle}
                 src={driveButtonActive ? driveIcon : driveIconClicked}/>
        </button>
    )
}

function ZoomInOut(params) {
    const {clickedButton, buttonSelected, buttonActive} = params
    return (
        <div className={styles.iconMargin}>

            <button onClick={clickedButton}
                    className={styles.buttonStyle + " " + styles.minusStyle + " " + styles.borderStyle}
                    style={{
                        opacity: buttonSelected === "minus" && buttonActive ? UploadBarStyle.buttonColor.opacity : ""
                    }} name={"minus"}>
                <span className={styles.operationSize}>-</span>
            </button>
            <button onClick={clickedButton} className={styles.plusStyle + " " + styles.buttonStyle} style={{
                opacity: buttonSelected === "plus" && buttonActive ? UploadBarStyle.buttonColor.opacity : ""
            }}
                    name={"plus"}>
                <span className={styles.operationSize}>+</span>
            </button>
        </div>
    )
}

