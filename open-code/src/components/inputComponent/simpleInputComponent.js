import React, {useContext, useState} from "react";
import styles from "./inputComponent.module.css"
import BlackText from "../fonts/blackText";
import {ThemeContext} from "../../App";
import {colors} from "../../utils/color";
import {MenuItem, Select} from "@mui/material";
import {Models} from "../../utils/constants";


/**
 * Simple Input Component :: input field
 * @param props
 * @returns {JSX.Element}
 * @constructor
 */
export default function SimpleInputComponent(props) {
    const {
        inputTitle,
        extraStyle,
        inputType,
        onDataChange,
        extraMargin,
        placeHolder,
        divStyle,
        headStyle,
        value,
        extraInputStyle,
        inlineStyle,
        onWidthDataChange,
        onHeightDataChange,
        OpenNewProjectHandle = () => {
        }
    } = props
    const [inputValue, setInputValue] = useState(value ? value : '');
    const theme = useContext(ThemeContext);
    const date = new Date()
    let currentDate = `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, "0")}-${date.getDate().toString().padStart(2, "0")}`;
    const [inputDOBValue, setInputDOBValue] = useState(value ? value : currentDate)
    const [modelType, setModelType] = useState(inputTitle === "Type" ? "DETECTOR" : "AUTOPILOT_F");
    const [modelWidth, setModelWidth] = useState(322)
    const [modelHeight, setModelHeight] = useState(322)

    //name change event handling
    function handleChange(e) {
        setInputValue(e.target.value);
        onDataChange(e.target.value);
    }

    //DOB change event handling
    function handleDOBValueChange(e) {
        setInputDOBValue(e.target.value);
        onDataChange(e.target.value);
    }

    function onModelTypeChange(e) {
        setModelType(e.target.value);
        onDataChange(e.target.value);
    }

    function onModelWidthChange(e) {
        setModelWidth(e.target.value);
        onWidthDataChange(e.target.value);
    }

    function onModelHeightChange(e) {
        setModelHeight(e.target.value);
        onHeightDataChange(e.target.value);
    }

    return (
        <div className={styles.mainDiv + " " + extraStyle}>
            <BlackText text={inputTitle} extraStyle={headStyle} divStyle={divStyle}/>
            {inputType === "text" ?
                <div className={styles.inputBorder + " " + extraMargin} style={inlineStyle}>
                    <input type={"text"}
                           name={"inputBox"}
                           placeholder={placeHolder}
                           onKeyDown={(e) => {
                               if (e.keyCode === 13) {
                                   OpenNewProjectHandle()
                               }
                           }}
                           className={styles.inputSection + " " + extraInputStyle}
                           value={inputValue} onChange={handleChange}
                           style={{color: theme.theme === "dark" ? colors.whiteFont : colors.blackFont}}
                    />
                </div>
                :
                inputType === "date" ?
                    <div className={styles.inputBorder}>
                        <input type="date" name="date"
                               min="1920-01-01"
                               value={inputDOBValue}
                               max={currentDate}
                               onChange={handleDOBValueChange}
                               className={styles.inputSection + " " + extraInputStyle}
                               style={{color: theme.theme === "dark" ? colors.whiteFont : colors.blackFont}}

                        />
                    </div>
                    :
                    inputType === "email" ?
                        <div className={styles.inputBorder}>
                            <input disabled={true} className={styles.inputSection + " " + extraInputStyle}
                                   value={props.value}/>
                        </div>
                        :
                        inputType === "dropdown" ?
                            <div className={styles.inputBorder} style={{height: "50%"}}>
                                <Select
                                    labelId="model-type"
                                    id="model-type"
                                    value={modelType}
                                    onChange={onModelTypeChange}
                                    sx={{
                                        backgroundColor: "transparent",
                                        fontFamily: "Gilroy-Regular, sans-serif",
                                        fontSize: "15px",
                                        borderRadius: "6px",
                                        width: "100%",
                                        height: "100%",
                                        paddingLeft: "0",
                                        border: "none",
                                        outline: "none",
                                        color: theme.theme === "dark" ? colors.whiteFont : colors.blackFont,
                                    }}
                                >{
                                    (inputTitle === "Type" ? Models.type : Models.class).map((item) => (
                                        <MenuItem
                                            style={theme.theme === "dark" ? {
                                                color: colors.whiteFont,
                                                backgroundColor: colors.blackPopupBackground
                                            } : {
                                                color: colors.blackFont,
                                                backgroundColor: colors.whiteBackground
                                            }}
                                            value={item.value}
                                            key={item.value}
                                            className={styles.dropdownItem}>{item.value}</MenuItem>
                                    ))
                                }
                                </Select>
                            </div>
                            :
                            inputType === "name and extension" ?
                                <div className={styles.inputBorder + " " + extraMargin} style={{height: "50%"}}>
                                    <input type={"text"}
                                           name={"inputBox"}
                                           placeholder={placeHolder}
                                           className={styles.inputSection + " " + extraInputStyle}
                                           value={inputValue} onChange={handleChange}
                                           style={{color: theme.theme === "dark" ? colors.whiteFont : colors.blackFont}}
                                    />
                                    <div style={{color: theme.theme === "dark" ? colors.whiteFont : colors.blackFont}}
                                         className={styles.extensionName}>.tflite
                                    </div>
                                </div>
                                :
                                inputType === "dimensions" ?
                                    <div style={{display: "flex", height: "70%"}}>
                                        <div className={styles.inputBorder + " " + extraMargin} style={{width: "23%"}}>
                                            <input type={"number"}
                                                   name={"inputBox"}
                                                   placeholder={placeHolder}
                                                   className={styles.inputSection + " " + extraInputStyle}
                                                   value={modelWidth} onChange={onModelWidthChange}
                                                   style={{color: theme.theme === "dark" ? colors.whiteFont : colors.blackFont}}
                                            />
                                        </div>
                                        <div style={{padding: "10% 10%"}}>×</div>
                                        <div className={styles.inputBorder + " " + extraMargin}
                                             style={{width: "23%"}}>
                                            <input type={"number"}
                                                   name={"inputBox"}
                                                   placeholder={placeHolder}
                                                   className={styles.inputSection + " " + extraInputStyle}
                                                   value={modelHeight} onChange={onModelHeightChange}
                                                   style={{color: theme.theme === "dark" ? colors.whiteFont : colors.blackFont}}
                                            />
                                        </div>
                                    </div>
                                    :
                                    <div className={styles.inputBorder}>
                                        <input className={styles.inputSection + " " + extraInputStyle}/>
                                    </div>


            }
        </div>
    )
}
