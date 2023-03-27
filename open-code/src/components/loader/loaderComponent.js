import React from "react";
import {CircularProgress} from "@mui/material";

export default function LoaderComponent(props) {
    return (
        <div>
            <CircularProgress style={{color: props.color}} thickness={props.thickness}/>
        </div>
    )
}