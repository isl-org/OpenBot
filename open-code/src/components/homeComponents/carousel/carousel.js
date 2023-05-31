import "react-responsive-carousel/lib/styles/carousel.min.css";
import React, {useContext, useEffect, useState} from "react";
import styles from "./carousel.module.css";
import {ThemeContext} from "../../../App";
import {Carousal} from "../../../utils/constants";
import {Carousel} from "react-responsive-carousel";

/**
 * HomeCarousel - a component that renders a carousel
 * @returns {JSX.Element}
 * @constructor
 */
export const HomeCarousel = () => {
    // Render the carousel with the array of slides
    return (
        <Carousel axis={'horizontal'} infiniteLoop={true}
                  swipeScrollTolerance={50}
                  preventMovementUntilSwipeScrollTolerance={true}
                  interval={4000} autoPlay={true} showThumbs={false}>
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
    const isIOS = /iPhone|iPod/.test(navigator.userAgent) && !window.MSStream;
    const [isLandscape, setIsLandscape] = useState(window.matchMedia("(max-height: 500px) and (max-width: 1000px) and (orientation: landscape)").matches);

    useEffect(() => {
        const handleOrientationChange = () => {
            setIsLandscape(
                window.matchMedia("(max-height: 500px) and (max-width: 1000px) and (orientation: landscape)").matches
            );
        };
        window.addEventListener("resize", handleOrientationChange);
    }, []);

    return (
        <div className={styles.Content + " " + (theme === "dark" ? styles.darkBg : styles.lightBg)}>
            <div className={styles.HeadingContent}>
                <div style={{fontSize: isIOS && isLandscape && "18px"}}
                     className={styles.DragAndDropHeading + " " + (theme === "dark" ? styles.MainDark : styles.MainLight)}>{slide.carousalHeader}
                </div>
                <div style={{
                    fontSize: isIOS && isLandscape && "10px",
                    lineHeight: isIOS && isLandscape && "14px"
                }}
                     className={styles.DragAndDropContent + " " + (theme === "dark" ? styles.MainDark : styles.MainLight)}>
                    {slide.carousalLine1}<br/>{slide.carousalLine2}
                </div>
            </div>
            <img alt="gif" src={theme === "dark" ? slide.videoDark : slide.videoLight}
                 className={`${styles.ImageDrag} ${styles.videoStyle}`}/>
        </div>
    )
}