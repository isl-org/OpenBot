import React, {useState} from "react";
import Blockly from "blockly/core";
import uploadIcon from "../../assets/images/icon/upload-cloud.png"
import {UploadBarStyle} from "./styles";
import styles from "./style.module.css"
import undoIcon from "../../assets/images/icon/undo.png";
import redoIcon from "../../assets/images/icon/redo.png";
import driveIcon from "../../assets/images/icon/drive.png";
import {javascriptGenerator} from 'blockly/javascript';
import {useRef, useContext} from "react";
import {StoreContext} from "../../context/context";
import {colors} from "../../utils/color";
import driveIconClicked from "../../assets/images/icon/drive-clicked.png"
import {ThemeContext} from "../../App";

export const UploadCode = () => {
    const {drawer, setDrawer} = useContext(StoreContext);
    const {theme} = useContext(ThemeContext)
    const {setWorkspaceWidth} = useContext(StoreContext);
    const {setCode} = useContext(StoreContext);
    const {generate, setGenerateCode} = useContext(StoreContext);

    let primaryWorkspace = useRef();


    const generateCode = () => {
        const code = javascriptGenerator.workspaceToCode(
            primaryWorkspace.current
        );
        console.log(code);
        setGenerateCode(!generate);
        setCode(code)
        setWorkspaceWidth(77.79)
        setDrawer(true);
    }
    const [buttonSelected, setButtonSelected] = useState({backgroundColor: colors.openBotBlue});
    const [buttonActive, setButtonActive] = useState(false);
    const [driveButtonActive, setDriveButtonActive] = useState(false);
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
        }
        setButtonActive(true);
        setTimeout(() => {
            setButtonActive(false);
        }, 200);
    };

    const handleDriveButton = () => {
        setDriveButtonActive(true);
        setTimeout(() => {
            setDriveButtonActive(false);
        }, 200);
    }


    return (

        <div className={styles.barDiv}  className={styles.barDiv + " " + (theme === "dark" ? styles.barDivDark : styles.barDivLight)}>

            <div className={styles.iconMargin} onClick={generateCode}>
                <button className={styles.uploadCodeButton}
                        style={{opacity: buttonSelected === "uploadCode" && buttonActive ? UploadBarStyle.buttonColor.opacity : ""}}
                        name={"uploadCode"} onClick={clickedButton}>
                    <span className={styles.leftButton + " " + styles.iconMargin}>Upload Code</span>
                    <img alt={""}
                         className={styles.iconDiv + " " + styles.iconMargin} src={uploadIcon}/>
                </button>
            </div>

            <div  className={styles.operationsDiv}>
                <button className={styles.driveStyle + " " + styles.iconMargin}
                        onClick={handleDriveButton}>
                    <img alt={""} className={styles.driveIconStyle}
                         src={driveButtonActive ? driveIcon : driveIconClicked}/>
                </button>
                <div  className={styles.buttonMargin + " " + styles.iconMargin}>
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

                <div className={styles.iconMargin}>

                    <button onClick={clickedButton} className={styles.buttonStyle + " " + styles.minusStyle +" "+ styles.borderStyle}
                            style={{
                                opacity: buttonSelected === "minus" && buttonActive ? UploadBarStyle.buttonColor.opacity : ""
                            }} name={"minus"}>
                        <span className={styles.operationSize}>-</span>
                    </button>
                    <button onClick={clickedButton} className={styles.plusStyle + " "+ styles.buttonStyle} style={{
                        opacity: buttonSelected === "plus" && buttonActive ? UploadBarStyle.buttonColor.opacity : ""
                    }}
                            name={"plus"}>
                        <span className={styles.operationSize}>+</span>
                    </button>
                </div>
            </div>
        </div>
    );
}
