import React from 'react';
import {RightSectionStyles as useStyles} from "./styles";
import ProfileImage from "../../assets/Profile/profileImage.png";
import EditIcon from "../../assets/Profile/EditProfileIcon.png";
import {Content} from "../../utils/constants";

function RightSection(props) {
    const{tab}=props
    const classes = useStyles();

    return (
        <div className={classes.Main}>
            <header className={classes.Header}>Edit Profile</header>
            <form>
                <div className={classes.ProfileImage}>
                    <img alt="Icon" className={classes.Image} src={ProfileImage}/>
                    <img alt="Icon" className={classes.EditIcon} src={EditIcon}/>
                </div>
                <div className={classes.Edit}>
                    <div className={classes.Input}>
                        <label className={classes.lable}> First Name </label>
                        <input className={classes.InputArea} type="text"/>
                    </div>
                    <div className={classes.Input}>
                        <label className={classes.lable}> Last Name </label>
                        <input className={classes.InputArea} type="text"/>
                    </div>
                    <div className={classes.Input}>
                        <label className={classes.lable}> Date Of Birth </label>
                        <input className={classes.InputArea}
                               type="date"
                               name="date"
                               min="1920-01-01"
                               max="2100-01-01"/>
                    </div>
                    <div className={classes.InputEmail}>
                        <label className={classes.lableEmail}> Email address </label>
                        <input className={classes.InputAreaEmail} type="text" disabled/>
                    </div>
                </div>
                <div className={classes.btn}>
                    <div className={classes.Savebtn}>Save</div>
                    <div className={classes.Canclebtn}>Cancel</div>
                </div>
            </form>
        </div>
    );
}

export default RightSection;


