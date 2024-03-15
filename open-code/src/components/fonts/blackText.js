import React, {useContext} from "react";
import styles from "./fontStyle.module.css"
import {ThemeContext} from "../../App";
import {Themes} from "../../utils/constants";

/**
 * Render the BlackText component with appropriate styles based on the theme
 * @returns {JSX.Element}
 * @constructor
 */
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
                    (theme.theme === Themes.dark ?
                        styles.BlackTextDarkFont :
                        styles.BlackTextLightFont)}>
                {text}
            </span>
        </div>
    );
}

export default BlackText;