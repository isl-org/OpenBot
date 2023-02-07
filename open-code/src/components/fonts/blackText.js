import React from "react";
import styles from "./fontStyle.module.css"

function BlackText(props){
    const{extraStyle, text}=props
    return(
        <div>
            <span className={styles.blackFont + " " +extraStyle}>{text}</span>
        </div>
    );

}
export default BlackText;