import React from "react";
import styles from "./fontStyle.module.css"

function BlackText(props){
    return(
        <div>
            <span className={styles.blackFont}>{props.text}</span>
        </div>
    );

}
export default BlackText;