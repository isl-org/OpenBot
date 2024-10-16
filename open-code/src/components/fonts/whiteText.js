import React from "react";
import styles from "./fontStyle.module.css"

/**
 * function for white text
 * @param props
 * @returns {JSX.Element}
 * @constructor
 */
function WhiteText(props) {
    const {extraStyle, text,inlineStyle} = props
    return (
        <div>
            <span style={inlineStyle} className={styles.whiteFont + " " + extraStyle}>{text}</span>
        </div>
    );

}

export default WhiteText;