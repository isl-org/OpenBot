import "react-responsive-carousel/lib/styles/carousel.min.css";
import React, {useContext} from "react";
import styles from "./carousel.module.css";
import {ThemeContext} from "../../../App";
import {Carousal} from "../../../utils/constants";
import {Carousel} from "react-responsive-carousel";
import dragDropWhite from "../../../assets/images/drag_drop_white.mp4";
import dragDropDark from "../../../assets/images/drag_drop_dark.mp4";


/**
 * HomeCarousel - a component that renders a carousel
 * @returns {JSX.Element}
 * @constructor
 */
export const HomeCarousel = () => {
    // Render the carousel with the array of slides
    return (
        <Carousel axis={'horizontal'} infiniteLoop={true} interval={4000} autoPlay={true} showThumbs={false}>
            {Carousal.map((slide) => (
                <CarousalComponent key={slide} slide={slide}/>
            ))}
        </Carousel>
    )
}

/**
 * CarousalComponent - a component that renders a single slide in the carousel
 * @param {object} params.slide - an object containing the information for a single slide
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
            {slide.carousalHeader === "Drag and Drop" ?
                <video src={theme === "dark" ? dragDropDark : dragDropWhite}
                       className={`${styles.ImageDrag} ${styles.videoStyle}`}
                       autoPlay={true} loop={true}/> :
                <img alt="gif" src={theme === "dark" ? slide.videoDark : slide.videoLight}
                     className={`${styles.ImageDrag} ${styles.videoStyle}`}/>
            }
        </div>
    )
}