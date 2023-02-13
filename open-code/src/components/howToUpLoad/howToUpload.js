import React from 'react';
import {HowToUploadStyles} from "./styles";
import BlocklyCode from "../../assets/images/blockly-code-image.png";
import QRCode from "../../assets/images/qr-image.png";
import OpenBoat from "../../assets/images/open-boat-image.png";
function HowToUpload(props) {
    return (
        <div>
        <div style={HowToUploadStyles.Main}>
            <div style={HowToUploadStyles.HeadingContent}>
                <div style={HowToUploadStyles.MainHeading}>How to Upload?</div>
                <div style={HowToUploadStyles.Content}>
                    <span style={{color: "#0071C5"}}>OpenCode</span> is an open source platform that
                    provides you an easy three step process of turning your smartphone into a robot with
                    the help of block coding.
                </div>
            </div>
            <div style={HowToUploadStyles.DragAndDropImage}>
                <div style={HowToUploadStyles.DragAndDrop}>
                    <div style={HowToUploadStyles.NumberOne}>1</div>
                    <div style={HowToUploadStyles.DragAndDropHeading}>Drag and Drop</div>
                    <div style={HowToUploadStyles.DragAndDropContent}>
                        Drag and drop the selected block of code into the input field.<br/>
                        Repeat the steps of selecting and placing all required code blocks, and then
                        connecting them together to create a sequence of actions.
                    </div>
                </div>
                <img alt="blocklyCodePicture"src={BlocklyCode} style={HowToUploadStyles.ImageDrag}/>
            </div>
            <div style={HowToUploadStyles.SaveAndDownloadImage}>
                <img alt="qrCodePicture" src={QRCode} style={HowToUploadStyles.ImageSave}/>
                <div style={HowToUploadStyles.DragAndDrop}>
                    <div style={HowToUploadStyles.NumberTwo}>2</div>
                    <div style={HowToUploadStyles.SaveAndDownloadHeading}>Save and Download </div>
                    <div style={HowToUploadStyles.SaveAndDownloadContent}>
                        Check for errors by compiling the code and, upon successful compilation, generate the QR code.<br/>
                        Use the OpenBot android application to scan the QR code and successfully save the block code on your device.
                    </div>
                </div>

            </div>
            <div style={HowToUploadStyles.ConnectAndDriveImage}>
                <div style={HowToUploadStyles.DragAndDrop}>
                    <div style={HowToUploadStyles.NumberThree}>3</div>
                    <div style={HowToUploadStyles.DragAndDropHeading}>Connect and Drive </div>
                    <div style={HowToUploadStyles.DragAndDropContent}>
                        Pair your smartphone with the OpenBot car and run the code that you have downloaded.<br/>
                        Carry out desired actions such as activating indicator lights, detecting objects, and more on your robot car.
                    </div>
                </div>
                <img alt="openBotPicture"src={OpenBoat} style={HowToUploadStyles.ImageSave}/>
            </div>

        </div>

    </div>
    );
}

export default HowToUpload;