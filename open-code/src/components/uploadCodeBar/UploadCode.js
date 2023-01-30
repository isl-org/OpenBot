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

export const UploadCode = () => {
    const {drawer, setDrawer} = useContext(StoreContext);
    const {workspaceWidth, setWorkspaceWidth} = useContext(StoreContext)

    let primaryWorkspace = useRef();
    const generateCode = () => {
        const code = javascriptGenerator.workspaceToCode(
            primaryWorkspace.current
        );
        console.log(code);
        const changeWorkspaceWidth = drawer ? setWorkspaceWidth(100) : setWorkspaceWidth(80)
        const openDrawer = drawer ? setDrawer(false) : setDrawer(true);
    }


    const [buttonSelected, setButtonSelected] = useState({backgroundColor: colors.openBotBlue});
    const [buttonActive, setButtonActive] = useState(false);


    const clickedButton = (e) => {
        const {name} = e.target;
        setButtonSelected(name);
        setButtonActive((current) => !current);
    };

    return (

        <div style={UploadBarStyle.barDiv}>

            <div style={UploadBarStyle.iconMargin} onClick={generateCode}>
                <button style={{
                    ...UploadBarStyle.uploadCodeButton,
                    opacity: buttonSelected === "first" && buttonActive ? UploadBarStyle.buttonColor.opacity : ""
                }} name={"first"} onClick={clickedButton}>
                    <span onClick={clickedButton} style={{...UploadBarStyle.leftButton, ...UploadBarStyle.iconMargin}}>Upload Code</span>
                    <img onClick={clickedButton} alt={""}
                         style={{...UploadBarStyle.iconDiv, ...UploadBarStyle.iconMargin}} src={uploadIcon}/>
                </button>
            </div>

            <div style={UploadBarStyle.operationsDiv}>
                <button style={{...UploadBarStyle.iconMargin, ...UploadBarStyle.driveStyle}}>
                    <img alt={""} style={UploadBarStyle.driveIconStyle} src={driveIcon}/>
                </button>
                <div style={UploadBarStyle.iconMargin}>
                    <button onClick={clickedButton}
                            style={{
                                ...UploadBarStyle.buttonStyle, ...UploadBarStyle.undoButtonStyle,
                                opacity: buttonSelected === "second" && buttonActive ? UploadBarStyle.buttonColor.opacity : ""
                            }} name={"second"}>
                        <img onClick={clickedButton} alt={""} style={UploadBarStyle.commandSize} src={undoIcon}/>
                    </button>
                    <button onClick={clickedButton} style={{
                        ...UploadBarStyle.buttonStyle, ...UploadBarStyle.plusStyle,
                        opacity: buttonSelected === "third" && buttonActive ? UploadBarStyle.buttonColor.opacity : ""
                    }}
                            name={"third"}>
                        <img onClick={clickedButton} alt={""} style={UploadBarStyle.commandSize} src={redoIcon}/>
                    </button>
                </div>

                <div style={UploadBarStyle.iconMargin}>

                    <button onClick={clickedButton}
                            style={{
                                ...UploadBarStyle.buttonStyle, ...UploadBarStyle.minusStyle,
                                opacity: buttonSelected === "fourth" && buttonActive ? UploadBarStyle.buttonColor.opacity : ""
                            }} name={"fourth"}>
                        <span onClick={clickedButton} style={UploadBarStyle.operationSize}>-</span>
                    </button>
                    <button onClick={clickedButton} style={{
                        ...UploadBarStyle.buttonStyle, ...UploadBarStyle.plusStyle,
                        opacity: buttonSelected === "fifth" && buttonActive ? UploadBarStyle.buttonColor.opacity : ""
                    }}
                            name={"fifth"}>
                        <span onClick={clickedButton} style={UploadBarStyle.operationSize}>+</span>
                    </button>
                </div>
            </div>
        </div>
    );
}