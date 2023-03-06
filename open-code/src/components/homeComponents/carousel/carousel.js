import "react-responsive-carousel/lib/styles/carousel.min.css";
import React, {useContext} from "react";
import styles from "./carousel.module.css";
import {ThemeContext} from "../../../App";
import {Carousal} from "../../../utils/constants";
import {Carousel} from "react-responsive-carousel";
import {Images} from "../../../utils/images";

export const HomeCarousel = () => {
    const {theme} = useContext(ThemeContext)
    return (
        <Carousel axis={'horizontal'} infiniteLoop={true} interval={4000} autoPlay={true}>
            {Carousal.map((slide) => (
                <CarousalComponent
                    key={slide}
                    header={slide.carousalHeader}
                    line1={slide.carousalLine1}
                    line2={slide.carousalLine2}
                    children={slide.image ??
                        <video src={theme === "dark" ? Images.dragDropDark : Images.dragDropWhite}
                               className={styles.ImageDrag}
                               autoPlay={true} loop={true}/>
                    }
                />
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
    const {header, line2, line1, children} = params
    const {theme} = useContext(ThemeContext)
    return (
        <div className={styles.Content + " " + (theme === "dark" ? styles.darkBg : styles.lightBg)}>
            <div className={styles.HeadingContent}>
                <div
                    className={styles.DragAndDropHeading + " " + (theme === "dark" ? styles.MainDark : styles.MainLight)}>{header}
                </div>
                <div
                    className={styles.DragAndDropContent + " " + (theme === "dark" ? styles.MainDark : styles.MainLight)}>
                    {line1}<br/>{line2}
                </div>
            </div>
            {children}
        </div>
    )
}