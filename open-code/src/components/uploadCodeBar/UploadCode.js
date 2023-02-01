import React, {useState} from "react";
import uploadIcon from "../../assets/images/upload-cloud.png"
import {UploadBarStyle} from "./styles";
import undoIcon from "../../assets/images/undo.png";
import redoIcon from "../../assets/images/redo.png";
import driveIcon from "../../assets/images/drive.png";
import {javascriptGenerator} from 'blockly/javascript';
import {useRef, useContext} from "react";
import {StoreContext} from "../../context/Context";
import {colors} from "../../utils/color";
import driveIconClicked from "../../assets/images/drive-clicked.png"

export const UploadCode = () => {
    const {drawer, setDrawer} = useContext(StoreContext);
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
        setWorkspaceWidth(80)
        setDrawer(true);
    }
    const [buttonSelected, setButtonSelected] = useState({backgroundColor: colors.openBotBlue});
    const [buttonActive, setButtonActive] = useState(false);
    const [driveButtonActive,setDriveButtonActive]=useState(false);
    const clickedButton = (e) => {
        const {name} = e.target;
        setButtonSelected(name);
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

        <div style={UploadBarStyle.barDiv}>

            <div style={UploadBarStyle.iconMargin} onClick={generateCode}>
                <button style={{
                    ...UploadBarStyle.uploadCodeButton,
                    opacity: buttonSelected === "uploadCode" && buttonActive ? UploadBarStyle.buttonColor.opacity : ""
                }} name={"uploadCode"} onClick={clickedButton}>
                    <span style={{...UploadBarStyle.leftButton, ...UploadBarStyle.iconMargin}}>Upload Code</span>
                    <img alt={""}
                         style={{...UploadBarStyle.iconDiv, ...UploadBarStyle.iconMargin}} src={uploadIcon}/>
                </button>
            </div>

            <div style={UploadBarStyle.operationsDiv}>
                <button style={{...UploadBarStyle.iconMargin, ...UploadBarStyle.driveStyle}}
                        onClick={handleDriveButton}>
                    <img alt={""} style={UploadBarStyle.driveIconStyle}
                         src={driveButtonActive ? driveIcon : driveIconClicked}/>
                </button>
                <div style={UploadBarStyle.iconMargin}>
                    <button onClick={clickedButton}
                            style={{
                                ...UploadBarStyle.buttonStyle, ...UploadBarStyle.undoButtonStyle,
                                opacity: buttonSelected === "undo" && buttonActive ? UploadBarStyle.buttonColor.opacity : ""
                            }} name={"undo"}>
                        <img alt={""} style={UploadBarStyle.commandSize} src={undoIcon}/>
                    </button>
                    <button onClick={clickedButton} style={{
                        ...UploadBarStyle.buttonStyle, ...UploadBarStyle.plusStyle,
                        opacity: buttonSelected === "redo" && buttonActive ? UploadBarStyle.buttonColor.opacity : ""
                    }}
                            name={"redo"}>
                        <img alt={""} style={UploadBarStyle.commandSize} src={redoIcon}/>
                    </button>
                </div>

                <div style={UploadBarStyle.iconMargin}>

                    <button onClick={clickedButton}
                            style={{
                                ...UploadBarStyle.buttonStyle, ...UploadBarStyle.minusStyle,
                                opacity: buttonSelected === "minus" && buttonActive ? UploadBarStyle.buttonColor.opacity : ""
                            }} name={"minus"}>
                        <span style={UploadBarStyle.operationSize}>-</span>
                    </button>
                    <button onClick={clickedButton} style={{
                        ...UploadBarStyle.buttonStyle, ...UploadBarStyle.plusStyle,
                        opacity: buttonSelected === "add" && buttonActive ? UploadBarStyle.buttonColor.opacity : ""
                    }}
                            name={"add"}>
                        <span style={UploadBarStyle.operationSize}>+</span>
                    </button>
                </div>
            </div>
        </div>
    );
}
