import React from 'react';
import {RightSectionStyles} from "./styles";
import ProfileImage from "../../assets/Profile/profileImage.png";
import EditIcon from "../../assets/Profile/EditProfileIcon.png";
function RightSection(props) {
    const{tab}=props

    return (
        <div style={RightSectionStyles.Main}>
            <header style={RightSectionStyles.Header}>Edit Profile</header>
            <form>
                <div style={RightSectionStyles.ProfileImage}>
                    <img alt="Icon" style={RightSectionStyles.Image} src={ProfileImage}/>
                    <img alt="Icon" style={RightSectionStyles.EditIcon} src={EditIcon}/>
                </div>
                <div style={RightSectionStyles.Edit}>
                    <div style={RightSectionStyles.Input}>
                        <label style={RightSectionStyles.lable}> First Name </label>
                        <input style={RightSectionStyles.InputArea} type="text"/>
                    </div>
                    <div style={RightSectionStyles.Input}>
                        <label style={RightSectionStyles.lable}> Last Name </label>
                        <input style={RightSectionStyles.InputArea} type="text"/>
                    </div>
                    <div style={RightSectionStyles.Input}>
                        <label style={RightSectionStyles.lable}> Date Of Birth </label>
                        <input style={RightSectionStyles.InputArea}
                               type="date"
                               name="date"
                               min="1920-01-01"
                               max="2100-01-01"/>
                    </div>
                    <div style={RightSectionStyles.InputEmail}>
                        <label style={RightSectionStyles.lableEmail}> Email address </label>
                        <input style={RightSectionStyles.InputAreaEmail} type="text" disabled/>
                    </div>
                </div>
                <div style={RightSectionStyles.btn}>
                    <div style={RightSectionStyles.SaveBtn}>Save</div>
                    <div style={RightSectionStyles.CancelBtn}>Cancel</div>
                </div>
            </form>
        </div>
    );
}

export default RightSection;


