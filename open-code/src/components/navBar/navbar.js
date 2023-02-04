import React, {useContext, useState} from 'react';
import info from "../../assets/images/icon/info.png"
import moon from "../../assets/images/icon/whiteMode/white-mode-icon.png"
import line from "../../assets/images/Line.png"
import icon from "../../assets/images/icon/open-bot-logo.png"
import profileImage from "../../assets/images/icon/profile-image.png"
import {NavbarStyle} from "./styles";
import {useNavigate} from "react-router-dom";
import {ThemeContext} from "../../App"
import WhiteText from "../fonts/whiteText";

export function Navbar() {
    let navigate = useNavigate();
    const {theme, toggleTheme} = useContext(ThemeContext)
    const [isSigIn, setIsSigIn] = useState(false);
    const openHomepage = () => {
        let path = `/`;
        navigate(path);
    }

    return (
        <div style={NavbarStyle.navbarDiv}>
            <div style={NavbarStyle.navbarTitleDiv}>
                <img alt="" style={{...NavbarStyle.mainIcon, ...NavbarStyle.iconMargin}} src={icon} onClick={() => {
                    openHomepage()
                }}/>
                <span style={{...NavbarStyle.mainTitle, ...NavbarStyle.iconMargin}}>OpenCode</span>
            </div>
            <div style={NavbarStyle.navbarIconDiv}>
                <img alt="" src={info} style={{...NavbarStyle.infoIcon, ...NavbarStyle.iconMargin}}/>
                <img alt="" onClick={() => toggleTheme(!theme)} src={moon}
                     style={{...NavbarStyle.moonIcon, ...NavbarStyle.iconMargin}}/>
                <img alt="" src={line} style={{...NavbarStyle.lineIcon, ...NavbarStyle.iconMargin}}/>
                {
                    isSigIn ?
                        <div>
                            <img alt="Profile Icon" src={profileImage} style={{height: 28,width: 28}}/>
                            <WhiteText text = "sanjeev"/>
                        </div> :
                        <button onClick={() => setIsSigIn(true)}
                                style={{...NavbarStyle.buttonIcon, ...NavbarStyle.iconMargin}}><span>Sign in</span>
                        </button>
                }

            </div>
        </div>
    );
}

