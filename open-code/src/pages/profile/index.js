import React, {useState} from 'react';
import {ProfileStyles} from "./styles";
import LeftSection from "../../components/profile/leftSection";
import RightSection, {EditProfile} from "../../components/profile/rightSection";
import {Content} from "../../utils/constants";
import HowToUpload from "../../components/howToUpLoad/howToUpload";
import {Header} from "../../components/navBar/header";

function Profile(props) {
    const [tab, setTab] = useState(Content[0].title);
    const [logOut, setLogOut] = useState(false);
    return (
        <div>
            <Header/>
                <div style={ProfileStyles.Main}>
                    <LeftSection  tab={tab} setTab={setTab} setLogOut={setLogOut}/>
                    <RightSection tab={tab} setLogOut={setLogOut} logOut={logOut}/>
                </div>
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
        default:

    }
}
