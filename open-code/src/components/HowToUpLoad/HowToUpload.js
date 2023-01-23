import React from 'react';
import {HowToUploadStyles as useStyles} from "./styles";
import BlocklyCode from "../../assets/Profile/BlocklyCodeImage.png";
import QRCode from "../../assets/Profile/QRCode.png";
import OpenBoat from "../../assets/Profile/OpenBoatImage.png";
function HowToUpload(props) {
    const classes = useStyles();
    return (
        <div className={classes.Main}>
            <div className={classes.HeadingContent}>
                <div className={classes.MainHeading}>How to Upload?</div>
                <div className={classes.Content}>
                    <span style={{color: "#0071C5"}}>OpenCode</span> is an open source platform that
                    provides you an easy three step process of turning your smartphone into a robot with
                    the help of block coding.
                </div>
            </div>
            <div className={classes.DragAndDropImage}>
                <div className={classes.DragAndDrop}>
                    <div className={classes.NumberOne}>1</div>
                    <div className={classes.DragAndDropHeading}>Drag and Drop</div>
                    <div className={classes.DragAndDropContent}>
                        Drag and drop the selected block of code into the input field.<br/>
                        Repeat the steps of selecting and placing all required code blocks, and then
                        connecting them together to create a sequence of actions.
                    </div>
                </div>
                <img src={BlocklyCode} className={classes.ImageDrag}/>
            </div>
            <div className={classes.SaveAndDownloadImage}>
                <img src={QRCode} className={classes.ImageSave}/>
                <div className={classes.DragAndDrop}>
                    <div className={classes.NumberTwo}>2</div>
                    <div className={classes.SaveAndDownloadHeading}>Save and Download </div>
                    <div className={classes.SaveAndDownloadContent}>
                        Check for errors by compiling the code and, upon successful compilation, generate the QR code.<br/>
                        Use the OpenBot android application to scan the QR code and successfully save the block code on your device.
                    </div>
                </div>

            </div>
            <div className={classes.ConnectAndDriveImage}>
                <div className={classes.DragAndDrop}>
                    <div className={classes.NumberThree}>3</div>
                    <div className={classes.DragAndDropHeading}>Connect and Drive </div>
                    <div className={classes.DragAndDropContent}>
                        Pair your smartphone with the OpenBot car and run the code that you have downloaded.<br/>
                        Carry out desired actions such as activating indicator lights, detecting objects, and more on your robot car.
                    </div>
                </div>
                <img src={OpenBoat} className={classes.ImageSave}/>
            </div>
        </div>
    );
}

export default HowToUpload;