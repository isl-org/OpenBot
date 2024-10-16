import React from "react";
import {CircularProgress} from "@mui/material";

/**
 * render circular Loader Component
 * @param props
 * @returns {JSX.Element}
 * @constructor
 */
export default function LoaderComponent(props) {
    return (
        <div>
            <CircularProgress style={{color: props.color}} thickness={props.thickness}/>
        </div>
    )
}