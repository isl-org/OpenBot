import React, {useContext} from 'react';
import styles from "./newProject.module.css"
import {Images} from "../../../utils/images";
import {ThemeContext} from "../../../App";
import BlackText from "../../fonts/blackText";
import WhiteText from "../../fonts/whiteText";

function Card(props) {
    const {theme} = useContext(ThemeContext)

    return (
        <div className={styles.cardContent}>
            <div className={` ${styles.Card} ${theme==="dark"?styles.darkBoxShadow:styles.lightBoxShadow}`}>
                <div className={styles.CardHeadingIcon}>
                    {theme === "dark" ? <WhiteText extraStyle={styles.CardHeading} text={props.projectTitle}/> :
                        <BlackText extraStyle={styles.CardHeading} text={props.projectTitle}/>}
                    <img alt="pencil-icon" src={theme === "dark" ? Images.darkPencilIcon : Images.pencilIcon}
                         className={styles.PencilIcon}/>
                </div>
                <BlackText extraStyle={styles.Date} text={props.projectDate}/>
            </div>
        </div>
    );
}

export default Card;
