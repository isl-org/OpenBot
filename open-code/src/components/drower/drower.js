import React, {useContext, useEffect} from "react";
import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import {StoreContext} from "../../context/Context";
export function QrDrawer(){

    return(
        <>
                <PersistentDrawerRight/>
        </>
    )
}
export default function PersistentDrawerRight() {
    const {drawer,setDrawer} = useContext(StoreContext)
    return (
        <Box sx={{ display: 'flex',height : '100px'}}>
            <Drawer
                sx={{
                    width: 0,
                    height : "200px",
                    flexShrink: 0,
                    '& .MuiDrawer-paper': {
                        width: '21%',
                        height : '79.5vh',
                        marginTop : '5.5%'
                    },
                }}
                variant="persistent"
                anchor="right"
                open={drawer}
            >
            </Drawer>
        </Box>
    );
}
