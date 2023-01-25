import uploadIcon from "../../assets/images/upload-cloud.png"
import {UploadBarStyle} from "./styles";
import undoIcon from "../../assets/images/undo.png";
import redoIcon from "../../assets/images/redo.png";
import driveIcon from "../../assets/images/drive.png";

export const UploadCode = ()=>{
    return(

        <div style={UploadBarStyle.barDiv}>

            <div style={UploadBarStyle.iconMargin}>
                <button style={UploadBarStyle.uploadCodeButton}>
                    <span style={{...UploadBarStyle.leftButton, ...UploadBarStyle.iconMargin}}>Upload Code</span>
                    <img style={{...UploadBarStyle.iconDiv,...UploadBarStyle.iconMargin}} src={uploadIcon}/>

                </button>
            </div>

            <div style={UploadBarStyle.operationsDiv}>
                <button style={{...UploadBarStyle.iconMargin,...UploadBarStyle.driveStyle}}>
                    <img style={UploadBarStyle.driveIconStyle} src={driveIcon}/>
                </button>
                <div style={UploadBarStyle.iconMargin}>
                <button style={{...UploadBarStyle.buttonStyle, ...UploadBarStyle.undoButtonStyle}}>
                    <img style={ UploadBarStyle.commandSize} src={undoIcon}/>
                </button>
                <button style={UploadBarStyle.redoStyle}>
                    <img style={ UploadBarStyle.commandSize} src={redoIcon}/>
                </button>
                </div>
                <div style={UploadBarStyle.iconMargin}>
                <button style={{...UploadBarStyle.buttonStyle, ...UploadBarStyle.minusStyle}}>
                    <span style={UploadBarStyle.operationSize}>-</span>
                </button>
                <button style={{...UploadBarStyle.buttonStyle, ...UploadBarStyle.plusStyle}}>
                    <span style={UploadBarStyle.operationSize}>+</span>
                </button>
                </div>
            </div>

        </div>
    );
}
