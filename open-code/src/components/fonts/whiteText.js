import React from "react";
import styles from "./fontStyle.module.css"

function WhiteText(props) {
    const {extraStyle, text} = props
    return (
        <div>
            <span className={styles.whiteFont + " " + extraStyle}>{text}</span>
        </div>
    );

}

export default WhiteText;