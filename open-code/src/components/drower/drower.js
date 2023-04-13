import React, {useContext} from "react";
import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import {StoreContext} from "../../context/context";
import QrCode from "../qrcode/qrcode";
import {qrStyles} from "../qrcode/styles";
import rightSlider from "../../assets/images/icon/right-slider.png"
import {ThemeContext} from "../../App"
import {colors} from "../../utils/color";
import rightSliderDark from "../../assets/images/icon/right-slider-dark.png"
import {useTheme} from "@mui/material";
import useMediaQuery from "@mui/material/useMediaQuery";


/**
 *
 * @returns {JSX.Element}
 * @constructor
 */
export function QrDrawer() {

    return (
        <>
            <PersistentDrawerRight/>
        </>
    )
}

export default function PersistentDrawerRight() {
    const {theme} = useContext(ThemeContext)
    const {drawer} = useContext(StoreContext)
    const themes = useTheme();
    const isMobile = useMediaQuery(themes.breakpoints.down('md'));
    return (
        <>
            <Box sx={{display: 'flex', width: 0}}>
                <Drawer
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
        </>
    );
}

export const RightSlider = () => {
    const {setDrawer, drawer} = useContext(StoreContext)
    const {theme} = useContext(ThemeContext)

    const closeDrawer = () => {
        setDrawer(!drawer);
    }

    return (
        <div style={drawer ? qrStyles.rightSlider : qrStyles.leftSlider} onClick={closeDrawer}>
            <img alt="slider" src={theme === "dark" ? rightSliderDark : rightSlider}
                 style={drawer ? qrStyles.rightSliderIcon : qrStyles.leftSliderIcon}/>
        </div>
    )
}


/**
 *
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
