import React, {useContext} from 'react';
import ProfileImage from "../../assets/Profile/profileImage.png";
import EditIcon from "../../assets/Profile/EditProfileIcon.png";
import {handleTabBaseRendering} from "../../pages/profile/Profile";
import LogoutModel from "./LogoutModel";
import Styles from "./RightSection.module.css"
import {ThemeContext} from "../../App";

function RightSection(props) {
    const {tab, logOut, setLogOut} = props
    const {theme} = useContext(ThemeContext)
    return (
        <>
            {handleTabBaseRendering(tab, theme)}
            {logOut && <LogoutModel setLogOut={setLogOut}/>}
        </>
    );
}

export default RightSection;

export function EditProfile(theme) {
    return (
        <div className={Styles.Main}>
            <header className={Styles.Header + " " + (theme.theme === "dark" ? Styles.TextDark : Styles.TextLight)}>Edit
                Profile
            </header>
            <form>
                <div className={Styles.ProfileImage}>
                    <img alt="Icon" className={Styles.Image} src={ProfileImage}/>
                    <img alt="Icon" className={Styles.EditIcon} src={EditIcon}/>
                </div>
                <div className={Styles.Edit}>
                    <div className={Styles.Input}>
                        <label
                            className={Styles.lable + " " + (theme.theme === "dark" ? Styles.TextDark : Styles.TextLight)}> First
                            Name </label>
                        <input
                            className={Styles.InputArea + " " + (theme.theme === "dark" ? Styles.InputAreaDark : Styles.InputAreaLight)}
                            type="text"/>
                    </div>
                    <div className={Styles.Input}>
                        <label  className={Styles.lable + " " + (theme.theme === "dark" ? Styles.TextDark : Styles.TextLight)}> Last Name </label>
                        <input
                            className={Styles.InputArea + " " + (theme.theme === "dark" ? Styles.InputAreaDark : Styles.InputAreaLight)}
                            type="text"/>
                    </div>
                    <div className={Styles.Input}>
                        <label  className={Styles.lable + " " + (theme.theme === "dark" ? Styles.TextDark : Styles.TextLight)}> Date Of Birth </label>
                        <input
                            className={Styles.InputArea + " " + (theme.theme === "dark" ? Styles.InputAreaDark : Styles.InputAreaLight)}
                            type="date"
                            name="date"
                            min="1920-01-01"
                            max="2100-01-01"/>
                    </div>
                    <div className={Styles.InputEmail}>
                        <label className={Styles.lableEmail}> Email address </label>
                        <input
                            className={Styles.InputAreaEmail + " " + (theme.theme === "dark" ? Styles.InputAreaDark : Styles.InputAreaLight)}
                            type="text" disabled/>
                    </div>
                </div>
                <div className={Styles.btn}>
                    <div className={Styles.SaveBtn}>Save</div>
                    <div className={Styles.CancelBtn}>Cancel</div>
                </div>
            </form>
        </div>
    )
}

