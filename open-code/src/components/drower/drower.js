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
        <Box sx={{display: 'flex', width: 0}}>
            <Drawer
                sx={{
                    width: 0,
                    flexShrink: 0,
                    '& .MuiDrawer-paper': {
                        width: isMobile ? '62%' : '23%',
                        height: isMobile ? '75%' : '81.3%',
                        marginTop: '5rem',
                        borderLeft: theme === "dark" ? "0.5px solid gray" : '1px solid rgba(0, 0, 0, 0.2)',
                        backgroundColor: theme === "dark" ? colors.blackBackground : colors.whiteBackground,
                        color: theme === "dark" ? colors.whiteFont : colors.blackFont,
                    },
                }}
                variant="persistent"
                anchor="right"
                open={drawer}
            >

                <QrCode/>
                <div style={{display: 'flex'}}>
                    <RightSlider/>
                    <div><DrawerBody isMobile={isMobile}/></div>

                </div>

            </Drawer>

        </Box>
    );
}

export const RightSlider = () => {
    const {setDrawer} = useContext(StoreContext)
    const {theme} = useContext(ThemeContext)
    const closeDrawer = () => {
        setDrawer(false);
    }
    return (
        <div style={qrStyles.rightSlider} onClick={closeDrawer}>
            <img alt="slider" src={theme === "dark" ? rightSliderDark : rightSlider} style={qrStyles.rightSliderIcon}/>
        </div>
    )
}

export const DrawerBody = (props) => {
const{isMobile}=props
    const qrScanSteps = ["Open OpenBot App on your phone", "Tap ScanQR Icon on homepage", "Point your phone to this screen to capture the code"]

    return (
        <>
            <div>
                <h1 style={isMobile? qrStyles.mobileHeading:qrStyles.heading}>Scan and upload your code:</h1>
                {qrScanSteps.map((step, key) => {
                    return (
                        <div key={key} style={isMobile? qrStyles.mobileList:qrStyles.list}>
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
