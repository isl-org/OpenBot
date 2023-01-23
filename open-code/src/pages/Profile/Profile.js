import React, {useState} from 'react';
import {ProfileStyles as useStyles} from "./styles";
import LeftSection from "../../components/Profile/LeftSection";
import RightSection from "../../components/Profile/RightSection";
import {Content} from "../../utils/constants";
import HowToUpload from "../../components/HowToUpLoad/HowToUpload";


/**
 *
 * @param props
 * @returns {JSX.Element}
 * @constructor
 */
function Profile(props) {
    const classes = useStyles();
    const [tab, setTab] = useState(Content[0].title);
    return (
        <div className={classes.Main}>
            <LeftSection content={Content} tab={tab} setTab={setTab}/>
            {handleTabBaseRendering(tab)}
        </div>
    );
}

export default Profile;

function handleTabBaseRendering(tab){
    switch (tab){
        case Content[0].title:
            return(
                <RightSection/>
            )
        case Content[2].title:
            return (
               <HowToUpload/>
            )

    }
}