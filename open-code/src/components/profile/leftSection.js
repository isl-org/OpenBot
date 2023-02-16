import React, {useContext} from 'react';
import {Content} from "../../utils/constants";
import Styles from "./leftSection.module.css"
import {ThemeContext} from "../../App";

function LeftSection(props) {
    const {setTab, tab, setLogOut, logOut} = props
    const {theme} = useContext(ThemeContext)
    return (
        <div className={Styles.Main + " " + (theme === "dark" ? Styles.MainDark : Styles.MainLight)}>
            <div className={Styles.IconContent}>
                {
                    Content.map((data) => (
                        <div
                            className={(theme === "dark") ? (data.title === tab) ? Styles.ItemsSelectedDark : Styles.Items : (data.title === tab) ? Styles.ItemsSelected : Styles.Items}
                            onClick={() => {
                                (data.title === Content[3].title) ? setLogOut(!logOut) : setTab(data.title)
                            }}>
                            <img alt="Icon" className={Styles.Icon} src={data.Icon}/>
                            <div
                                className={Styles.Content + " " + (theme === "dark" ? Styles.ContentDark : Styles.ContentLight)}>{data.title}</div>
                        </div>
                    ))
                }
            </div>
        </div>
    );
}

export default LeftSection;
