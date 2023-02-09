import React from "react";
import styles from "./fontStyle.module.css"

function BlackText(props){
    const{extraStyle, text, divStyle}=props
    return(
        <div style={divStyle}>
            <span className={styles.blackFont + " " +extraStyle}>{text}</span>
        </div>
    );

}
export default BlackText;