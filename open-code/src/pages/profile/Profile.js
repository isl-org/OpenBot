import React, {useState} from 'react';
import {ProfileStyles} from "./styles";
import LeftSection from "../../components/profile/LeftSection";
import RightSection, {EditProfile} from "../../components/profile/RightSection";
import {Content} from "../../utils/constants";
import HowToUpload from "../../components/howToUpLoad/HowToUpload";
import Footer from "../../components/footer/Footer";
import {Navbar} from "../../components/navBar/Navbar";

function Profile(props) {
    const [tab, setTab] = useState(Content[0].title);
    const [logOut, setLogOut] = useState(false);
    return (
        <div>
            <Navbar/>
                <div style={ProfileStyles.Main}>
                    <LeftSection  tab={tab} setTab={setTab} setLogOut={setLogOut}/>
                    <RightSection tab={tab} setLogOut={setLogOut} logOut={logOut}/>
                </div>
            <Footer/>
        </div>
    );
}

export default Profile;


export function handleTabBaseRendering(tab ,theme) {



    switch (tab) {
        case Content[0].title:
            return (
                <EditProfile theme={theme}/>
            )

        case Content[2].title:
            return (
                <HowToUpload theme={theme}/>
            )

    }
}
