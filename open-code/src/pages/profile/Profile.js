import React, {useState} from 'react';
import {ProfileStyles} from "./styles";
import LeftSection from "../../components/profile/LeftSection";
import RightSection from "../../components/profile/RightSection";
import {Content} from "../../utils/constants";
import HowToUpload from "../../components/howToUpLoad/HowToUpload";
import Footer from "../../components/footer/Footer";
import LogoutModel from "../../components/profile/LogoutModel";
import {Navbar} from "../../components/navBar/Navbar";


/**
 *
 * @param props
 * @returns {JSX.Element}
 * @constructor
 */
function Profile(props) {
    const [tab, setTab] = useState(Content[0].title);

    return (
        <div>
            <Navbar/>
            <div style={ProfileStyles.Main}>
                <LeftSection content={Content} tab={tab} setTab={setTab}/>
                {handleTabBaseRendering(tab)}
            </div>
            <Footer/>
        </div>
    );
}

export default Profile;

function handleTabBaseRendering(tab) {
    switch (tab) {
        case Content[0].title:
            return (
                <RightSection/>
            )

        case Content[2].title:
            return (
                <HowToUpload/>
            )

        case Content[3].title:
            return (
                <LogoutModel/>
            )

    }
}