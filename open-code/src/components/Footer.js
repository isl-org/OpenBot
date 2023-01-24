import React from 'react';
import youtube from "../assets/images/Youtube.png";
import web from "../assets/images/Web.png";
import playStore from "../assets/images/Google play.png";
import appStore from "../assets/images/App Store.png";
import "../Components/Footer.css"
import line from "../assets/images/line1.png";
function Footer(){

        return (
            <div className={"footer"}>
            <div className={"rules"}>
                <li><div className={"tos"}>Terms of Service</div></li>
                <li><img width={"1.36vh"} src={line} alt="" /></li>
                <li><div className={"pp"}>Privacy Policy</div></li>
            </div>
                <div className={"media"}>
                    <li><img width={"40vh"} src={youtube} alt="" /></li>
                    <li><img width={"40vh"} src={web} alt="" /></li>
                    <li><img width={"120vw"} src={playStore} alt="" /></li>
                    <li><img width={"120vw"} src={appStore} alt="" /></li>
                </div>
            </div>
        );
}

export default Footer;