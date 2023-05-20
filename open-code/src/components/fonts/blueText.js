import React from "react";
import styles from "./fontStyle.module.css"

function BlueText(props) {
    const {extraStyle, text,inlineStyle} = props
    return (
        <div>
            <span style={inlineStyle} className={styles.blueFont + " " + extraStyle}>{text}</span>
        </div>
    );

}

export default BlueText;