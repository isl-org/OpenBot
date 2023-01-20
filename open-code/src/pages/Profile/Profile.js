import React from 'react';
import {ProfileStyles as useStyles} from "./styles";
import LeftSection from "../../components/Profile/LeftSection";
import RightSection from "../../components/Profile/RightSection";
import Moon from "../../assets/Profile/moon.png";
import User from "../../assets/Profile/user.png";
import Logout from "../../assets/Profile/log-out.png";
import Help from "../../assets/Profile/help-circle.png";

function Profile(props) {
    const classes = useStyles();
    return (
        <div className={classes.Main}>
            <LeftSection content={Content}/>
            <RightSection/>
        </div>
    );
}

const Content = [
    {
        Icon: User,
        title: "My Profile",
        selected:true,
    },
    {
        Icon: Moon,
        title: "Change Theme",
        selected:false,
    },
    {
        Icon: Help,
        title: "How To Upload",
        selected:false,
    },
    {
        Icon: Logout,
        title: "Logout",
        selected:false,
    },

]

export default Profile;