import "react-responsive-carousel/lib/styles/carousel.min.css";
import React, {useContext} from "react";
import styles from "./carousel.module.css";
import {ThemeContext} from "../../../App";
import {Carousal} from "../../../utils/constants";
import {Carousel} from "react-responsive-carousel";
import {Images} from "../../../utils/images";

export const HomeCarousel = () => {

    return (
        <Carousel axis={'horizontal'} infiniteLoop={true} interval={4000} autoPlay={true}>
            {Carousal.map((slide) => (
                <CarousalComponent key={slide} slide={slide}/>
            ))}
        </Carousel>
    )
}


/**
 * CarousalComponent :: ui to render Carousal
 * @param params
 * @returns {JSX.Element}
 * @constructor
 */
function CarousalComponent(params) {
    const {slide} = params
    const {theme} = useContext(ThemeContext)
    return (
        <div className={styles.Content + " " + (theme === "dark" ? styles.darkBg : styles.lightBg)}>
            <div className={styles.HeadingContent}>
                <div
                    className={styles.DragAndDropHeading + " " + (theme === "dark" ? styles.MainDark : styles.MainLight)}>{slide.carousalHeader}
                </div>
                <div
                    className={styles.DragAndDropContent + " " + (theme === "dark" ? styles.MainDark : styles.MainLight)}>
                    {slide.carousalLine1}<br/>{slide.carousalLine2}
                </div>
            </div>
            <video src={theme === "dark" ? slide.videoDark : slide.videoLight}
                   className={styles.ImageDrag}
                   autoPlay={true} loop={true}/>
        </div>
    )
}