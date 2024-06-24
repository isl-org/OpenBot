import React from "react";
import styles from "./fontStyle.module.css"

/**
 * function for blue text
 * @param props
 * @returns {JSX.Element}
 * @constructor
 */
function BlueText(props) {
    const {extraStyle, text,inlineStyle} = props
    return (
        <div>
            <span style={inlineStyle} className={styles.blueFont + " " + extraStyle}>{text}</span>
        </div>
    );

}

export default BlueText;