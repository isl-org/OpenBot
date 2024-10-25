import React, { useContext, useState } from 'react';
import styles from "./personaCard.module.css";
import { personas } from "../../utils/persona";
import { ThemeContext } from "../../App";
import { Themes } from "../../utils/constants";
import { Images } from "../../utils/images";

/**
 * Component for choosing persona character
 * @param handlePersonas
 * @returns {Element}
 * @constructor
 */
const PersonaCard = ({ handlePersonas }) => {
    const theme = useContext(ThemeContext);
    const [selectedPersona, setSelectedPersona] = useState(null); // State to track selected persona

    const handleClick = (key) => {
        if (!selectedPersona) { // Only allow click if no persona is selected
            setSelectedPersona(key);
            handlePersonas(key);
        }
    };

    return (
        <div>
            <div className={styles.headingContainer}>
                <img className={styles.icon}
                     alt={"Person Icon"}
                     src={theme.theme === Themes.dark ? Images.darkUserIcon : Images.userIcon}
                />
                <h3 style={{fontSize: '16px'}}>Choose Your Persona:</h3>
            </div>
            <div className={`${theme.theme === Themes.dark ? styles.dark : ''} ${styles.personaContainer}`}>
                {personas.slice(0, 5).map((item) => (
                    <div
                        key={item.key}
                        className={`${theme.theme === Themes.dark ? styles.dark : ''} ${styles.personaCard} 
                          ${selectedPersona === item.key ? styles.selected : ''}`}
                        onClick={() => handleClick(item.key)}
                        style={{
                         fontSize: '14px',
                            pointerEvents: selectedPersona ? 'none' : 'auto',
                            cursor: selectedPersona ? 'not-allowed' : 'pointer'
                        }}
                    >
                        <h4>{item.name}</h4>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default PersonaCard;
