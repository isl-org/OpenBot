import React from 'react';
import info from "../../assets/images/info.png"
import moon from "../../assets/images/moon.png"
import line from "../../assets/images/Line.png"
import icon from "../../assets/images/ICON.png"
import {NavbarStyle} from "./styles";
export function Navbar(){
        return (
            <div style={NavbarStyle.navbarDiv}>
                <div style={NavbarStyle.navbarTitleDiv}>
                    <img style={{...NavbarStyle.mainIcon,...NavbarStyle.iconMargin}} src={icon} />
                    <span style={{...NavbarStyle.mainTitle,...NavbarStyle.iconMargin}}>OpenCode</span>
                </div>
            <div style={NavbarStyle.navbarIconDiv}>
                <img src={info} style={{...NavbarStyle.infoIcon,...NavbarStyle.iconMargin}}/>
                <img src={moon} style={{...NavbarStyle.moonIcon,...NavbarStyle.iconMargin}}/>
                <img src={line} style={{...NavbarStyle.lineIcon,...NavbarStyle.iconMargin}}/>
                <button style={{...NavbarStyle.buttonIcon,...NavbarStyle.iconMargin}}><span >Sign in</span></button>
            </div>
            </div>
        );
    }

