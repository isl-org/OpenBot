import React from 'react';
import {Block, Category, Field, Shadow, Value} from "../index";
import {controlsBlocks, loopBlocks} from "../blocks/generalBlocks";
import {controlBlocksType, loopBlocksType} from "../../../utils/constants";
import '../generator/generator'
import '../blocks/customblocks'

let x = [""];
export const Toolbox = (props) => {
    return (
        <>
            <Category name="Control" colour="#567AE4">
                {controlBlocksType.map((type) => {
                    return controlsBlocks(type)
                })}
            </Category>

            <Category name="Loops" colour="#C54E30">

                {loopBlocksType.map((type) => {
                    return loopBlocks(type)
                })}

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

            <Category name="Operators" colour="#C54E30">
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

            <Category colour="#8156C9" name="Variables">

                <Block type="variables_set"/>
                <Block type="variables_get"/>
                <Block type="math_change"/>
                <Block type="logic_boolean"/>
                <Block type="logic_null"/>
                <Block type="math_number"/>,
                <Block type="text"/>
                <Block type="timer"/>
            </Category>

            <Category name="Text" colour="#D030BA">
                <Block type="text"/>
                <Block type="text_multiline"/>
                <Block type="text_join"/>
                <Block type="text_append">
                    <Value name="TEXT">
                        <Shadow type="text"></Shadow>
                    </Value>
                </Block>
                <Block type="text_length">
                    <Value name="VALUE">
                        <Shadow type="text">
                            <Field name="TEXT">abc</Field>
                        </Shadow>
                    </Value>
                </Block>
                <Block type="text_isEmpty">
                    <Value name="VALUE">
                        <Shadow type="text">
                            <Field name="TEXT"></Field>
                        </Shadow>
                    </Value>
                </Block>
                <Block type="text_indexOf">
                    <Value name="VALUE">
                        <Block type="variables_get">
                            <Field name="VAR">text</Field>
                        </Block>
                    </Value>
                    <Value name="FIND">
                        <Shadow type="text">
                            <Field name="TEXT">abc</Field>
                        </Shadow>
                    </Value>
                </Block>
                <Block type="text_charAt">
                    <Value name="VALUE">
                        <Block type="variables_get">
                            <Field name="VAR">text</Field>
                        </Block>
                    </Value>
                </Block>
                <Block type="text_getSubstring">
                    <Value name="STRING">
                        <Block type="variables_get">
                            <Field name="VAR">text</Field>
                        </Block>
                    </Value>
                </Block>
                <Block type="text_changeCase">
                    <Value name="TEXT">
                        <Shadow type="text">
                            <Field name="TEXT">abc</Field>
                        </Shadow>
                    </Value>
                </Block>
                <Block type="text_trim">
                    <Value name="TEXT">
                        <Shadow type="text">
                            <Field name="TEXT">abc</Field>
                        </Shadow>
                    </Value>
                </Block>
                <Block type="text_count">
                    <Value name="SUB">
                        <Shadow type="text"></Shadow>
                    </Value>
                    <Value name="TEXT">
                        <Shadow type="text"></Shadow>
                    </Value>
                </Block>
                <Block type="text_replace">
                    <Value name="FROM">
                        <Shadow type="text"></Shadow>
                    </Value>
                    <Value name="TO">
                        <Shadow type="text"></Shadow>
                    </Value>
                    <Value name="TEXT">
                        <Shadow type="text"></Shadow>
                    </Value>
                </Block>
                <Block type="text_reverse">
                    <Value name="TEXT">
                        <Shadow type="text"></Shadow>
                    </Value>
                </Block>
                <Block type="text_print">
                    <Value name="TEXT">
                        <Shadow type="text">
                            <Field name="TEXT">abc</Field>
                        </Shadow>
                    </Value>
                </Block>
                <Block type="text_prompt_ext">
                    <Value name="TEXT">
                        <Shadow type="text">
                            <Field name="TEXT">abc</Field>
                        </Shadow>
                    </Value>
                </Block>
            </Category>

            <Category colour="#506481" name="List">
                <Block type="lists_create_with"/>,
                <Block type="lists_create_empty"/>,
                <Block type="lists_repeat"/>,
                <Block type="lists_length"/>
                <Block type="lists_isEmpty"/>
                <Block type="lists_indexOf"/>
                <Block type="lists_getIndex"/>
                <Block type="lists_setIndex"/>
                <Block type="lists_getSublist"/>
                <Block type="lists_sort"/>
                <Block type="lists_split"/>
            </Category>
            <Category colour="#5BBC73" name="Sound">
                <Block type="soundIs"/>,
                <Block type="soundType"/>,
                <Block type="soundMode">
                </Block>,
            </Category>
            <Category colour="#61A8EC" name="Sensing">
                <Block type="sonarReading"/>
                <Block type="batteryReading"/>
                <Block type="speedReading"/>
                <Block type="voltageDividerReading"/>
                <Block type="wheelOdometerSensors"/>
                <Block type="indicatorLedSensor"/>
                <Block type="frontLedSensor"/>
                <Block type="backLedSensor"/>
                <Block type="ledStatusSensor"/>
            </Category>
            <Category colour="#9B61E2" name="Movement">
                <Block type="movementDirection"/>,
                <Block type="movementDistance"/>
                <Block type="movementCircular">
                </Block>
                <Block type="movementStop"/>
            </Category>
            <Category colour="#717171" name="Detection">
                <Block type='print'/>
            </Category>
        </>
    );
}
