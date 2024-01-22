import React, {useContext, useEffect, useState} from "react";
import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import {StoreContext} from "../../context/context";
import {qrStyles} from "../qrcode/styles";
import {ThemeContext} from "../../App"
import {colors} from "../../utils/color";
import {useTheme} from "@mui/material";
import useMediaQuery from "@mui/material/useMediaQuery";
import {Images} from "../../utils/images";
import styles from "../qrcode/qrCode.module.css"
import QrCode from "../qrcode/qrcode";
import {Constants} from "../../utils/constants";
import CodeEditor from "../editor/codeEditor";
import {Themes} from "../../utils/constants"

/**
 * QrDrawer component renders a drawer with a QR code and some instructions on how to use it.
 * @returns {JSX.Element}
 * @constructor
 */
export function RightDrawer() {
    const {theme} = useContext(ThemeContext) // Retrieve the current theme from the ThemeContext
    const {drawer, code, category} = useContext(StoreContext)  // Retrieve the drawer state from the StoreContext
    const themes = useTheme();// Get the current theme breakpoints using useTheme hook
    const isMobile = useMediaQuery(themes.breakpoints.down("sm"));// Determine if the screen is a mobile device using useMediaQuery hook
    const [isLandscape, setIsLandscape] = useState(window.matchMedia("(max-height: 450px) and (max-width: 1000px) and (orientation: landscape)").matches);
    const [isTabletQuery, setIsTabletQuery] = useState(window.matchMedia("(min-width: 768px) and (max-width: 1024px)").matches);
    useEffect(() => {
        const handleOrientationChange = () => {
            setIsLandscape(window.matchMedia("(max-height: 500px) and (max-width: 1000px) and (orientation: landscape)").matches);
            setIsTabletQuery(window.matchMedia("(min-width: 768px) and (max-width: 1024px)").matches);
        };
        window.addEventListener("resize", handleOrientationChange);
    }, []);

    return (
        <>
            {(code || category !== Constants.qr) &&
                <Box sx={{display: 'flex', width: 0}}>
                    <Drawer
                        // Styling for the drawer
                        sx={{
                            width: 0,
                            flexShrink: 0,
                            '& .MuiDrawer-paper': {
                                width: drawer ? category !== Constants.qr ? isMobile ? isLandscape ? '35%' : '62%' : '40%' : isMobile ? isLandscape ? '32%' : '62%' : isLandscape ? '50%' : isTabletQuery ? '45%' : '25%' : isMobile ? isLandscape ? '3%' : '6%' : '2%',
                                borderLeft: drawer ? theme === Themes.dark ? "0.5px solid gray" : '1px solid rgba(0, 0, 0, 0.2)' : "0.0",
                                backgroundColor: theme === Themes.dark ? colors.blackBackground : colors.whiteBackground,
                                color: theme === Themes.dark ? colors.whiteFont : colors.blackFont,
                                top: isLandscape ? "4rem" : isTabletQuery ? "6rem" : "5rem",
                                bottom: isMobile ? "9%" : isLandscape ? "18%" : isTabletQuery ? "4.4rem" : "4.4rem",
                            },
                        }}
                        // Drawer is always visible and can only be closed programmatically
                        variant="persistent"
                        anchor="right"
                        open={true}
                    >
                        {category === Constants.qr ?
                            <>
                                <div style={{overflow: "scroll", height: isLandscape ? "63%" : "100%"}}>
                                    <QrCode/>
                                    <div style={{display: "flex"}}>
                                        <RightSlider/>
                                        <DrawerBody isMobile={isMobile}/>
                                    </div>
                                </div>
                            </> :
                            <div style={{
                                display: "flex",
                                height: isLandscape ? "65.5%" : isMobile ? "80%" : "79%",
                                position: "relative",
                                overflow: "scroll"
                            }}>
                                <CodeEditor/>
                            </div>

                        }
                    </Drawer>
                </Box>
            }
        </>
    );
}


/**
 * RightSlider is a component that renders a clickable icon that opens and closes the drawer.
 * @returns {JSX.Element}
 * @constructor
 */
export const RightSlider = () => {
    const {setDrawer, drawer} = useContext(StoreContext)
    const {theme} = useContext(ThemeContext)
    const closeDrawer = () => {
        setDrawer(!drawer);// Function that closes the drawer when the icon is clicked
    }

    // Render the icon with the correct style and theme
    return (
        <div className={styles.slider + " " + (!drawer && styles.leftSlider)} onClick={closeDrawer}>
            {drawer ? <img alt="slider" src={theme === "dark" ? Images.rightSliderDark : Images.rightSlider}
                           style={drawer ? qrStyles.rightSliderIcon : qrStyles.leftSliderIcon}/> :
                <img alt="slider" src={theme === "dark" ? Images.leftSliderDark : Images.leftSlider}
                     style={drawer ? qrStyles.rightSliderIcon : qrStyles.leftSliderIcon}/>}
        </div>
    )
}


/**
 *  DrawerBody is a component that renders the instructions for scanning the QR code.
 * @param props
 * @returns {JSX.Element}
 * @constructor
 */
export const DrawerBody = (props) => {
    const {isMobile} = props
    const qrScanSteps = ["Open OpenBot App on your phone", "Tap ScanQR Icon on Projects page", "Point your phone to this screen to capture the code"]
    return (
        <>
            <div>
                <h1 className={styles.heading} style={isMobile ? qrStyles.mobileHeading : qrStyles.heading}>Scan and
                    upload your code:</h1>
                {qrScanSteps.map((step, key) => {
                    return (
                        <div key={key} style={isMobile ? qrStyles.mobileList : qrStyles.list}>
                            <div>
                                <span style={{marginRight: '.7rem'}}>{key + 1}. </span>
                            </div>
                            <div>{step}</div>
                        </div>
                    )
                })}
            </div>
        </>
    )
}

