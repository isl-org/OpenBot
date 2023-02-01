import React, {useContext, useEffect} from "react";
import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import {StoreContext} from "../../context/Context";
import QrCode from "../qrcode/qrcode";
import {qrStyles} from "../qrcode/styles";
import rightSlider from "../../assets/images/icon/right-slider.png"

export function QrDrawer() {

    return (
        <>
            <PersistentDrawerRight/>
        </>
    )
}

export default function PersistentDrawerRight() {
    const {drawer, setDrawer} = useContext(StoreContext)
    return (
        <Box sx={{display: 'flex', height: '100px'}}>
            <Drawer
                sx={{
                    width: 0,
                    height: "200px",
                    flexShrink: 0,
                    '& .MuiDrawer-paper': {
                        width: '22%',
                        height: '79.5vh',
                        marginTop: '5.5%',
                        borderLeft : '1px solid rgba(0, 0, 0, 0.2)'
                    },
                }}
                variant="persistent"
                anchor="right"
                open={drawer}
            >
                <paper>
                    <QrCode/>
                    <div style={{display : 'flex'}}>
                        <RightSlider/>
                        <div><DrawerBody/></div>

                    </div>
                </paper>
            </Drawer>

        </Box>
    );
}

export const RightSlider = ()=>{
    const {drawer, setDrawer} = useContext(StoreContext)
    const {setWorkspaceWidth} = useContext(StoreContext);
    const closeDrawer = ()=>{
        setDrawer(false);
        setWorkspaceWidth(100);
    }
    return(
        <div style={qrStyles.rightSlider} onClick={closeDrawer}>
            <img src={rightSlider} style={qrStyles.rightSliderIcon}/>
        </div>
    )
}

export const DrawerBody = (props) => {
    const qrScanSteps = ["Open OpenBot App on your phone","Tap ScanQR Icon on homepage","Point your phone to this screen to capture the code"]
    return (
        <>
            <div>
                <h1 style={qrStyles.heading}>Scan and upload your code:</h1>
                {qrScanSteps.map((step,key)=>{
                    return(
                        <div style={qrStyles.list}>
                            <div>
                                <span style={{marginRight : '.7rem'}}>{key + 1}. </span>
                            </div>
                            <div>{step}</div>

                        </div>
                    )
                })}
            </div>
        </>
    )
}
