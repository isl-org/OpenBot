import React from "react";
import {CircularProgress} from "@mui/material";

export default function LoaderComponent(props) {
    return(
        <div>
            <CircularProgress style={{color: props.color, height: 20, width: 20}}/>
        </div>
    )
}