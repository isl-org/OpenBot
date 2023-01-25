import {PlaygroundNavbarStyles} from "./Styles";
import icon from "../../assets/images/ICON.png";
import info from "../../assets/images/info.png";
import moon from "../../assets/images/moon.png";
import line from "../../assets/images/Line.png";
import React from "react";

export const WorkspaceNavbar= ()=>{
    return (
        <div style={PlaygroundNavbarStyles.navbarDiv}>
            <div style={PlaygroundNavbarStyles.navbarTitleDiv}>
                <img style={{...PlaygroundNavbarStyles.mainIcon,...PlaygroundNavbarStyles.iconMargin}} src={icon} />
                <span style={{...PlaygroundNavbarStyles.mainTitle,...PlaygroundNavbarStyles.iconMargin}}>OpenCode</span>
            </div>
            <div style={PlaygroundNavbarStyles.navbarIconDiv}>
                <img src={info} style={{...PlaygroundNavbarStyles.infoIcon,...PlaygroundNavbarStyles.iconMargin}}/>
                <img src={moon} style={{...PlaygroundNavbarStyles.moonIcon,...PlaygroundNavbarStyles.iconMargin}}/>
                <img src={line} style={{...PlaygroundNavbarStyles.lineIcon,...PlaygroundNavbarStyles.iconMargin}}/>
                <button style={{...PlaygroundNavbarStyles.buttonIcon,...PlaygroundNavbarStyles.iconMargin}}><span >Sign in</span></button>
            </div>
        </div>
    );
}