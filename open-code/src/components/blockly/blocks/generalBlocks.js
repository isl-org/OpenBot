import {Block, Field, Shadow, Value} from "../index";
import React from "react";
import './customblocks';

/**
 * Blockly control blocks
 * @param type
 * @param index
 * @returns {JSX.Element}
 */
export const controlsBlocks = (type, index) => {
    return <Block key={index} type={type}/>

}

/**
 * Blockly loop blocks
 * @param type
 * @param index
 * @returns {JSX.Element}
 */
export const loopBlocks = (type, index) => {
    return <Block key={index} type={type}/>
}

/**
 * Blockly conditional loops blocks
 * @param loopProperty
 * @returns {JSX.Element}
 */
export const conditionalLoops = (loopProperty) => {
    return <Block type={loopProperty.blockType}>
        <Value name={loopProperty.valueName}>
            <Shadow type="math_number">
                <Field name="NUM">1</Field>
            </Shadow>
        </Value>
        <Value name="TO">
            <Shadow type="math_number">
                <Field name="NUM">10</Field>
            </Shadow>
        </Value>
        <Value name="BY">
            <Shadow type="math_number">
                <Field name="NUM">1</Field>
            </Shadow>
        </Value>
    </Block>
}

