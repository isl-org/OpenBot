import "react-responsive-carousel/lib/styles/carousel.min.css";
import {Carousel} from 'react-responsive-carousel';
import dragDropWhite from "../../../assets/images/drag_drop_white.mp4";
import dragDropDark from "../../../assets/images/drag_drop_dark.mp4";
import QRCode from "../../../assets/images/qr-image.png";
import OpenBoat from "../../../assets/images/open-boat-image.png";
import React, {useContext} from "react";
import styles from "./carousel.module.css";
import {ThemeContext} from "../../../App";

export const HomeCarousel = () => {
    const {theme} = useContext(ThemeContext)

    return (
        <Carousel axis={'horizontal'} infiniteLoop={true} interval={4000} autoPlay={true}>
            <div className={styles.Content + " " + (theme === "dark" ? styles.darkBg : styles.lightBg)}>
                <div className={styles.HeadingContent}>
                    <div
                        className={styles.DragAndDropHeading + " " + (theme === "dark" ? styles.MainDark : styles.MainLight)}>Drag
                        and Drop
                    </div>
                    <div
                        className={styles.DragAndDropContent + " " + (theme === "dark" ? styles.MainDark : styles.MainLight)}>
                        Drag and drop the selected block of code into the input field.<br/>
                        Repeat the steps of selecting and placing all required code blocks, and then
                        connecting them together to create a sequence of actions.
                    </div>
                </div>
                <video src={theme === "dark" ? dragDropDark : dragDropWhite} className={styles.ImageDrag}
                       autoPlay={true} loop={true}/>
            </div>
            <div className={styles.Content + " " + (theme === "dark" ? styles.darkBg : styles.lightBg)}>
                <div className={styles.HeadingContent}>
                    <div
                        className={styles.DragAndDropHeading + " " + (theme === "dark" ? styles.MainDark : styles.MainLight)}>Save
                        and Download
                    </div>
                    <div
                        className={styles.SaveAndDownloadContent + " " + (theme === "dark" ? styles.MainDark : styles.MainLight)}>
                        Check for errors by compiling the code and, upon successful compilation, generate the QR
                        code.<br/>
                        Use the OpenBot android application to scan the QR code and successfully save the block code on
                        your
                        device.
                    </div>
                </div>
                <img src={QRCode} className={styles.ImageSave} alt={'QRCode'}/>

            </div>
            <div className={styles.Content + " " + (theme === "dark" ? styles.darkBg : styles.lightBg)}>
                <div className={styles.HeadingContent}>
                    <div
                        className={styles.DragAndDropHeading + " " + (theme === "dark" ? styles.MainDark : styles.MainLight)}>Connect
                        and Drive
                    </div>
                    <div
                        className={styles.SaveAndDownloadContent + " " + (theme === "dark" ? styles.MainDark : styles.MainLight)}>
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
