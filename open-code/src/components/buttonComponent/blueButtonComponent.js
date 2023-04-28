import React from "react";
import styles from "./buttonComponent.module.css"
import BlueText from "../fonts/blueText";
import WhiteText from "../fonts/whiteText";

export default function BlueButton(props) {
    const {buttonName, extraStyle, buttonType, onClick, buttonStyle} = props
    return (
        buttonType === "contained" ?
            <div onClick={onClick} className={styles.containedButton + " " + extraStyle}>
                <WhiteText text={buttonName} extraStyle={buttonStyle}/>
            </div> :
            <div onClick={onClick} className={styles.normalButton + " " + extraStyle}>
                <BlueText text={buttonName} extraStyle={buttonStyle}/>
            </div>
    )
}

