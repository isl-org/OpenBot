import React from 'react';
import youtube from "../../assets/images/Youtube.png";
import web from "../../assets/images/Web.png";
import playStore from "../../assets/images/Google play.png";
import appStore from "../../assets/images/App Store.png";
import "./styles"
import {FooterStyle} from "./styles";
function Footer(){
    const classes = FooterStyle()
        return (
            <div className={classes.footerDiv}>
            <div className={classes.footerTextDiv}>
                <span className={`${classes.iconMargin} ${classes.textFont}`} >Terms Of Service</span>
                <span className={`${classes.iconMargin} ${classes.textFont}`}>|</span>
                <span className={`${classes.iconMargin} ${classes.textFont} `}>Privacy Policy</span>
            </div>
                <div className={classes.footerIconDiv}>
                    <img className={`${classes.youtubePic} ${classes.iconMargin}`} src={youtube}/>
                    <img className={`${classes.webPic} ${classes.iconMargin}`} src={web}/>
                    <img className={`${classes.playStorePic} ${classes.iconMargin}` } src={playStore}/>
                    <img className={`${classes.appStorePic} ${classes.iconMargin} `} src={appStore}/>

                </div>
            </div>
        );
}

export default Footer;