import React from 'react';
import {FooterStyle} from "./styles";
import youtube from "../../assets/images/Youtube.png"
import web from "../../assets/images/Web.png"
import playStore from "../../assets/images/Google play.png"
import appStore from "../../assets/images/App Store.png"

function Footer() {

    return (
        <div style={FooterStyle.footerDiv}>
            <div style={FooterStyle.footerTextDiv}>
                <span style={{...FooterStyle.iconMargin, ...FooterStyle.textFont}}>Terms Of Service</span>
                <span style={{...FooterStyle.iconMargin, ...FooterStyle.textFont}}>|</span>
                <span style={{...FooterStyle.iconMargin, ...FooterStyle.textFont}}>Privacy Policy</span>
            </div>
            <div style={FooterStyle.footerIconDiv}>
                <img style={{...FooterStyle.youtubePic, ...FooterStyle.iconMargin}} src={youtube}/>
                <img style={{...FooterStyle.webPic, ...FooterStyle.iconMargin}} src={web}/>
                <img style={{...FooterStyle.playStorePic, ...FooterStyle.iconMargin}} src={playStore}/>
                <img style={{...FooterStyle.appStorePic, ...FooterStyle.iconMargin}} src={appStore}/>

            </div>
        </div>
    );
}

export default Footer;
