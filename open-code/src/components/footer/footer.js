import React, {useContext} from 'react';
import {FooterStyle} from "./styles";
import youtube from "../../assets/images/icon/youtube-logo.png"
import web from "../../assets/images/icon/web-logo.png"
import playStore from "../../assets/images/icon/play-store-logo.png"
import appStore from "../../assets/images/icon/app-store-logo.png"
import {ThemeContext} from "../../App";
function Footer() {

    const {theme}=useContext(ThemeContext)

    return (
        <div style={FooterStyle.footerDiv}>
            <div style={FooterStyle.footerTextDiv}>
                <span style={{...FooterStyle.iconMargin, ...FooterStyle.textFont}}>Terms Of Service</span>
                <span style={{...FooterStyle.iconMargin, ...FooterStyle.textFont}}>|</span>
                <span style={{...FooterStyle.iconMargin, ...FooterStyle.textFont}}>Privacy Policy</span>
            </div>
            <div style={FooterStyle.footerIconDiv}>

                <img alt="" style={{...FooterStyle.youtubePic,...FooterStyle.iconMargin}} src={youtube}/>
                <img alt="" style={{...FooterStyle.webPic,...FooterStyle.iconMargin}} src={web}/>
                <img alt="" style={{...FooterStyle.playStorePic,...FooterStyle.iconMargin}} src={playStore}/>
                <img alt="" style={{...FooterStyle.appStorePic,...FooterStyle.iconMargin}} src={appStore}/>

            </div>
        </div>
    );
}

export default Footer;
