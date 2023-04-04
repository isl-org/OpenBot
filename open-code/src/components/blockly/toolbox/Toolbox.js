import React from 'react';
import {Block, Category, Field, Shadow, Value} from "../index";
import {controlsBlocks, loopBlocks} from "../blocks/generalBlocks";
import {controlBlocksType, loopBlocksType} from "../../../utils/constants";
import '../generator/generator';
import '../blocks/customblocks';
// import styles from "../BlocklyComponent.css"
export const Toolbox = (props) => {

    return (
        <>

            <Category name="Control" colour={"#567AE4"}>
                <Block type="controls_if" gap="8">
                    <Value name="IF0">
                        <Shadow type="logic_boolean">
                            <Field name="BOOL">TRUE</Field>
                        </Shadow>
                    </Value>
                </Block>
                {controlBlocksType.map((type, index) => {
                    return controlsBlocks(type, index)
                })}
                <Block type="logic_compare" gap="8">
                    <Value name="A">
                        <Shadow type="math_number">
                            <Field name="NUM">0</Field>
                        </Shadow>
                    </Value>
                    <Value name="B">
                        <Shadow type="math_number">
                            <Field name="NUM">0</Field>
                        </Shadow>
                    </Value>
                </Block>
                <Block type="start"/>
                <Block type="forever"/>
            </Category>

            <Category name="Loops" colour={"#C54E30"}>

                {loopBlocksType.map((type, index) => {
                    return loopBlocks(type, index)
                })}
                xx
                <Block type="controls_for">
                    <Value name="FROM">
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
            </Category>

            <Category name="Operators" colour={"#8156C9"}>
                <Block type="math_arithmetic"/>,
                <Block type="math_number"/>,
                <Block type="math_modulo"/>,
                <Block type="math_single"/>,
                <Block type="math_constant"/>,
                <Block type="math_number_property"/>,
                <Block type="math_round"/>,
                <Block type="math_on_list"/>,
                <Block type="math_random_int"/>,
            </Category>

            <Category name="Variables" colour={"#D030BA"}>

                <Block type="variables_set"/>
                <Block type="variables_get"/>
                <Block type="math_change"/>
                <Block type="logic_boolean"/>
                <Block type="logic_null"/>
                <Block type="math_number"/>,
                <Block type="text"/>
                <Block type="wait"/>
                <Block type="timer"/>
                <Block type="variables_set" gap="8">
                    <Field name="VAR"></Field>
                    <Value name="VALUE">
                        <Block type="lists_create_with">
                            <Value name="ADD0">
                                <Shadow type="math_number">
                                    <Field name="NUM">0</Field>
                                </Shadow>
                            </Value>
                            <Value name="ADD1">
                                <Shadow type="math_number">
                                    <Field name="NUM">1</Field>
                                </Shadow>
                            </Value>
                        </Block>
                    </Value>
                </Block>
            </Category>
            <Category name="LED" colour={"#506481"}>
                <Block type="leftIndicator_led"/>
                <Block type="rightIndicator_led"/>
                <Block type="indicatorStatus"/>
                {/*<Block type="toggle_block"/>*/}
                {/*<Block type="front_led"/>*/}
                {/*<Block type="back_led"/>*/}
                {/*<Block type="status_led"/>*/}
            </Category>
            <Category name="Phone Sensors" colour={"C26F87"}>
                <Block type="gyroscope_reading"/>
                <Block type="acceleration_reading"/>
                <Block type="magnetic_reading"/>
                {/*<Block type="toggle_switch"/>*/}
            </Category>
            <Category name="Sound" colour={"#5BBC73"}>
                <Block type="soundIs"/>,
                <Block type="soundType"/>,
                <Block type="soundMode">
                </Block>,
            </Category>
            <Category name="Sensors" colour={"#61A8EC"}>
                <Block type="sonarReading"/>
                <Block type="speedReading"/>
                <Block type="voltageDividerReading"/>
                <Block type="wheelOdometerSensors"/>
                <Block type="indicatorLedSensor"/>
                <Block type="frontLedSensor"/>
                <Block type="backLedSensor"/>
                <Block type="ledStatusSensor"/>
            </Category>
            <Category name="Speed" colour={"#567AE4"}>
                <Block type="speedSlow"/>
                <Block type="speedMedium"/>
                <Block type="speedHigh"/>
            </Category>
            <Category name="Movement" colour={"#DA4B5D"}>
                <Block type="forward&BackwardAtSpeed"/>
                <Block type="forward&BackwardAtSpeedForTime"/>
                <Block type="left&RightAtSpeed"/>
                <Block type="moveLeft&Right"/>
                <Block type="moveLeft&RightForTime"/>
                <Block type="movementCircular"/>
                <Block type="circularAtSpeed"/>
                <Block type="circularAtSpeedForTime"/>
                <Block type="movementStop"/>
            </Category>
        </>
    );
}
