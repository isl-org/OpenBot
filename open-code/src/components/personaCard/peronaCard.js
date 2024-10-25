import React, { useContext } from 'react';
import styles from "./personaCard.module.css";
import { personas } from "../../utils/persona";
import { ThemeContext } from "../../App";
import { Themes } from "../../utils/constants";
import {Images} from "../../utils/images";

/**
 * component for choosing persona character
 * @param handlePersonas
 * @returns {Element}
 * @constructor
 */
const PersonaCard = ({ handlePersonas }) => {
    const theme = useContext(ThemeContext);

    return (
        <div>
            <div className={styles.headingContainer}>
                <img className={styles.icon}
                    alt={ "Person Icon"}
                     src={theme.theme === Themes.dark ? Images.darkUserIcon : Images.userIcon}
                />
                <h3>Choose Your Persona : </h3>
            </div>
            <div className={`${theme.theme === Themes.dark ? styles.dark : ''} ${styles.personaContainer}`}>
                {personas.slice(0, 5).map((item, id) => (
                    <div
                        key={id}
                        className={`${theme.theme === Themes.dark ? styles.dark : ''} ${styles.personaCard}`}
                        onClick={() => handlePersonas(item.key)}
                    >
                        <h4>{item.name}</h4>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default PersonaCard;
