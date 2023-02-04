import React from "react";
import styles from "./fontStyle.module.css"

function BlueText(props){
    const{extraStyle, text}=props
    return(
        <div>
            <span className={styles.blueFont + " " + extraStyle}>{text}</span>
        </div>
    );

}
export default BlueText;