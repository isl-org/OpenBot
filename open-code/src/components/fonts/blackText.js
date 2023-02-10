import React, {useContext} from "react";
import styles from "./fontStyle.module.css"
import {ThemeContext} from "../../App";

function BlackText(props) {
    const theme = useContext(ThemeContext)
    const {extraStyle, text, divStyle} = props
    return (
        <div style={divStyle}>
            <span
                className={
                    styles.blackFont +
                    " " +
                    extraStyle +
                    " " +
                    (theme.theme === "dark" ?
                        styles.BlackTextDarkFont :
                        styles.BlackTextLightFont)}>
                {text}
            </span>
        </div>
    );

}

export default BlackText;