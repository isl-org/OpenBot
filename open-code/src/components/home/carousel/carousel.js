import "react-responsive-carousel/lib/styles/carousel.min.css";
import {Carousel} from 'react-responsive-carousel';
import BlocklyCode from "../../../assets/Profile/BlocklyCodeImage.png";
import QRCode from "../../../assets/Profile/QRCode.png";
import OpenBoat from "../../../assets/Profile/OpenBoatImage.png";
import React from "react";
import styles from "./carousel.module.css";

export const HomeCarousel = () => {
    return (
        <Carousel className={styles.Main} axis={'horizontal'}>
            <div className={styles.Content}>
                <div className={styles.HeadingContent}>
                    <div className={styles.DragAndDropHeading}>Drag and Drop</div>
                    <div className={styles.DragAndDropContent}>
                        Drag and drop the selected block of code into the input field.<br/>
                        Repeat the steps of selecting and placing all required code blocks, and then
                        connecting them together to create a sequence of actions.
                    </div>
                </div>
                <img src={BlocklyCode} className={styles.ImageDrag} alt={'BlocklyCodeImg'}/>
            </div>
            <div className={styles.Content}>
                <div className={styles.HeadingContent}>
                    <div className={styles.DragAndDropHeading}>Save and Download</div>
                    <div className={styles.SaveAndDownloadContent}>
                        Check for errors by compiling the code and, upon successful compilation, generate the QR
                        code.<br/>
                        Use the OpenBot android application to scan the QR code and successfully save the block code on
                        your
                        device.
                    </div>
                </div>
                <img src={QRCode} className={styles.ImageSave} alt={'QRCode'}/>

            </div>
            <div className={styles.Content}>
                <div className={styles.HeadingContent}>
                    <div className={styles.DragAndDropHeading}>Connect and Drive</div>
                    <div className={styles.SaveAndDownloadContent}>
                        Pair your smartphone with the OpenBot car and run the code that you have downloaded.<br/>
                        Carry out desired actions such as activating indicator lights, detecting objects, and more on
                        your
                        robot car.
                    </div>
                </div>
                <img src={OpenBoat} className={styles.ImageSave} alt={'OpenBoat'}/>

            </div>
        </Carousel>
    )
}
