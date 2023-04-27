import React, {useContext} from "react";
import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import {StoreContext} from "../../context/context";
import QrCode from "../qrcode/qrcode";
import {qrStyles} from "../qrcode/styles";
import {ThemeContext} from "../../App"
import {colors} from "../../utils/color";
import {useTheme} from "@mui/material";
import useMediaQuery from "@mui/material/useMediaQuery";
import {Images} from "../../utils/images";


/**
 * QrDrawer component renders a drawer with a QR code and some instructions on how to use it.
 * @returns {JSX.Element}
 * @constructor
 */
export function QrDrawer() {
    const {theme} = useContext(ThemeContext) // Retrieve the current theme from the ThemeContext
    const {drawer, code} = useContext(StoreContext)  // Retrieve the drawer state from the StoreContext
    const themes = useTheme();// Get the current theme breakpoints using useTheme hook
    const isMobile = useMediaQuery(themes.breakpoints.down('md'));// Determine if the screen is a mobile device using useMediaQuery hook

    return (
        <>
            {code &&
                <Box sx={{display: 'flex', width: 0}}>
                    <Drawer
                        // Styling for the drawer
                        sx={{
                            width: 0,
                            flexShrink: 0,
                            '& .MuiDrawer-paper': {
                                width: drawer ? isMobile ? '62%' : '23%' : isMobile ? '6%' : '2%',
                                height: isMobile ? '75%' : '81.3%',
                                marginTop: '5rem',
                                borderLeft: drawer ? theme === "dark" ? "0.5px solid gray" : '1px solid rgba(0, 0, 0, 0.2)' : "0.0",
                                backgroundColor: theme === "dark" ? colors.blackBackground : colors.whiteBackground,
                                color: theme === "dark" ? colors.whiteFont : colors.blackFont,
                            },
                        }}
                        // Drawer is always visible and can only be closed programmatically
                        variant="persistent"
                        anchor="right"
                        open={true}
                    >
                        <QrCode/>
                        <div style={{display: "flex"}}>
                            <RightSlider/>
                            <DrawerBody isMobile={isMobile}/>
                        </div>
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
        <div style={drawer ? qrStyles.rightSlider : qrStyles.leftSlider} onClick={closeDrawer}>
            <img alt="slider" src={theme === "dark" ? Images.rightSliderDark : Images.rightSlider}
                 style={drawer ? qrStyles.rightSliderIcon : qrStyles.leftSliderIcon}/>
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
    const qrScanSteps = ["Open OpenBot App on your phone", "Tap ScanQR Icon on homepage", "Point your phone to this screen to capture the code"]

    return (
        <>
            <div>
                <h1 style={isMobile ? qrStyles.mobileHeading : qrStyles.heading}>Scan and upload your code:</h1>
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
