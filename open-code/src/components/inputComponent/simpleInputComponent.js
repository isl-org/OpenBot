import React, {useContext, useState} from "react";
import styles from "./inputComponent.module.css"
import BlackText from "../fonts/blackText";
import {ThemeContext} from "../../App";
import {colors} from "../../utils/color";

export default function SimpleInputComponent(props) {
    const {inputTitle, extraStyle, inputType, onDataChange, extraMargin, placeHolder, value} = props
    const [inputValue, setInputValue] = useState(value ? value : '');
    const theme = useContext(ThemeContext);
    const date = new Date()
    let currentDate = `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, "0")}-${date.getDate().toString().padStart(2, "0")}`;

    function handleChange(e) {
        setInputValue(e.target.value)
        onDataChange(e.target.value);
    }

    return (
        <div className={styles.mainDiv + " " + extraStyle}>
            <BlackText text={inputTitle}/>
            {inputType === "text" ?
                <div className={styles.inputBorder + " " + extraMargin}>
                    <input type={"text"}
                           name={"inputBox"}
                           placeholder={placeHolder}
                           className={styles.inputSection}
                           value={inputValue} onChange={handleChange}
                           style={{color: theme.theme === "dark" ? colors.whiteFont : colors.blackFont}}
                    />
                </div>
                :
                inputType === "date" ?
                    <div className={styles.inputBorder}>
                        <input type="date" name="date"
                               min="1920-01-01"
                               max={currentDate}
                               defaultValue={currentDate}
                               className={styles.inputSection}
                               style={{color: theme.theme === "dark" ? colors.whiteFont : colors.blackFont}}

                        />
                    </div>
                    :
                    inputType === "email" ?
                        <div className={styles.inputBorder}>
                            <input disabled={true} className={styles.inputSection} value={props.value}/>
                        </div>
                        :
                        <div className={styles.inputBorder}>
                            <input className={styles.inputSection}/>
                        </div>
            }
        </div>
    )
}
