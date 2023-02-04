import React from "react";
import styles from "./fontStyle.module.css"

function WhiteText(props){
    return(
        <div>
            <span className={styles.whiteFont}>{props.text}</span>
        </div>
    );

}
export default WhiteText;