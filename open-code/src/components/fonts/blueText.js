import React from "react";
import styles from "./fontStyle.module.css"

function BlueText(props){
    return(
        <div>
            <span className={styles.blueFont}>{props.text}</span>
        </div>
    );

}
export default BlueText;