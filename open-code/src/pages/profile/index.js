import React, {useState} from 'react';
import {ProfileStyles} from "./styles";
import LeftSection from "../../components/profile/leftSection";
import RightSection, {EditProfile} from "../../components/profile/rightSection";
import {Content} from "../../utils/constants";
import HowToUpload from "../../components/howToUpLoad/howToUpload";
import Footer from "../../components/footer/footer";
import {Navbar} from "../../components/navBar/navbar";

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
