import React from 'react';
import {Block, Category, Field, Shadow, Value} from "../index";

export const Toolbox = (props)=>{
    return(
        <>
            <Category name="Control" colour = "#567AE4">
                <Block type="controls_if"/>,
                <Block type="controls_ifelse"/>,
                <Block type="logic_ternary"/>,
                <Block type="logic_compare"/>,
                <Block type="logic_operation"/>,
                <Block type="logic_negate"/>,
                <Block type="logic_boolean"/>,
                <Block type="logic_null" disabled="true"/>
            </Category>

            <Category name="Operators" colour = "#C54E30">
                <Block type="controls_whileUntil"/>,\
                <Block type="controls_repeat"/>,
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
                </Block>,
                <Block type="controls_forEach"/>,
                <Block type="controls_flow_statements"/>,
                <Block type="controls_repeat_ext">
                    <Value name="TIMES">
                        <Shadow type="math_number">
                            <Field name="NUM">10</Field>
                        </Shadow>
                    </Value>
                </Block>
            </Category>

            <Category colour="#8156C9" name="Variables">
                <Block type="test_react_field"/>,
                <Block type="test_react_date_field"/>,
                <Block type="text_charAt">
                    <Value name="VALUE">
                        <Block type="variables_get">
                            <Field name="VAR">text</Field>
                        </Block>
                    </Value>
                </Block>
                <Block type="Add"/>
                <Block type="math_number">
                    <Field name="NUM">0</Field>
                </Block>
                <Block type="area_of_circle">
                    <Value name="Area">
                        <Block type="math_number">
                            <Field name="NUM">9</Field>
                        </Block>
                    </Value>
                </Block>
                <Block type="print"/>
            </Category>

            <Category name="Text" colour = "#D030BA">
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
                <Block type="test_react_field"/>,
                <Block type="test_react_date_field"/>,
                <Block type="text_charAt">
                    <Value name="VALUE">
                        <Block type="variables_get">
                            <Field name="VAR">text</Field>
                        </Block>
                    </Value>
                </Block>
                <Block type="Add"/>
                <Block type="math_number">
                    <Field name="NUM">0</Field>
                </Block>
                <Block type="area_of_circle">
                    <Value name="Area">
                        <Block type="math_number">
                            <Field name="NUM">9</Field>
                        </Block>
                    </Value>
                </Block>
                <Block type="print"/>
            </Category>

            {/*<Category name="Math">*/}
            {/*    <Block type="math_arithmetic"/>,*/}
            {/*    <Block type="math_number"/>,*/}
            {/*    <Block type="math_modulo"/>,*/}
            {/*    <Block type="math_single"/>,*/}
            {/*    <Block type="math_trig"/>,*/}
            {/*    <Block type="math_constant"/>,*/}
            {/*    <Block type="math_number_property"/>,*/}
            {/*    <Block type="math_round"/>,*/}
            {/*    <Block type="math_on_list"/>,*/}
            {/*    <Block type="math_random_int"/>,*/}
            {/*    <Block type="math_atan2"/>,*/}
            {/*</Category>*/}

            <Category colour="#C26F87" name="Events">
                <Block type="test_react_field"/>,
                <Block type="test_react_date_field"/>,
                <Block type="text_charAt">
                    <Value name="VALUE">
                        <Block type="variables_get">
                            <Field name="VAR">text</Field>
                        </Block>
                    </Value>
                </Block>
                <Block type="Add"/>
                <Block type="math_number">
                    <Field name="NUM">0</Field>
                </Block>
                <Block type="area_of_circle">
                    <Value name="Area">
                        <Block type="math_number">
                            <Field name="NUM">9</Field>
                        </Block>
                    </Value>
                </Block>
                <Block type="print"/>
            </Category>
            <Category colour="#5BBC73" name="Sound">
                <Block type="test_react_field"/>,
                <Block type="test_react_date_field"/>,
                <Block type="text_charAt">
                    <Value name="VALUE">
                        <Block type="variables_get">
                            <Field name="VAR">text</Field>
                        </Block>
                    </Value>
                </Block>
                <Block type="Add"/>
                <Block type="math_number">
                    <Field name="NUM">0</Field>
                </Block>
                <Block type="area_of_circle">
                    <Value name="Area">
                        <Block type="math_number">
                            <Field name="NUM">9</Field>
                        </Block>
                    </Value>
                </Block>
                <Block type="print"/>
            </Category>
            <Category colour="#61A8EC" name="Sensing">
                <Block type="test_react_field"/>,
                <Block type="test_react_date_field"/>,
                <Block type="text_charAt">
                    <Value name="VALUE">
                        <Block type="variables_get">
                            <Field name="VAR">text</Field>
                        </Block>
                    </Value>
                </Block>
                <Block type="Add"/>
                <Block type="math_number">
                    <Field name="NUM">0</Field>
                </Block>
                <Block type="area_of_circle">
                    <Value name="Area">
                        <Block type="math_number">
                            <Field name="NUM">9</Field>
                        </Block>
                    </Value>
                </Block>
                <Block type="print"/>
            </Category>
            <Category colour="#DA4B5D" name="Bumper">
                <Block type="test_react_field"/>,
                <Block type="test_react_date_field"/>,
                <Block type="text_charAt">
                    <Value name="VALUE">
                        <Block type="variables_get">
                            <Field name="VAR">text</Field>
                        </Block>
                    </Value>
                </Block>
                <Block type="Add"/>
                <Block type="math_number">
                    <Field name="NUM">0</Field>
                </Block>
                <Block type="area_of_circle">
                    <Value name="Area">
                        <Block type="math_number">
                            <Field name="NUM">9</Field>
                        </Block>
                    </Value>
                </Block>
                <Block type="print"/>
            </Category>
            <Category colour="#9B61E2" name="Movement">
                <Block type="test_react_field"/>,
                <Block type="test_react_date_field"/>,
                <Block type="text_charAt">
                    <Value name="VALUE">
                        <Block type="variables_get">
                            <Field name="VAR">text</Field>
                        </Block>
                    </Value>
                </Block>
                <Block type="Add"/>
                <Block type="math_number">
                    <Field name="NUM">0</Field>
                </Block>
                <Block type="area_of_circle">
                    <Value name="Area">
                        <Block type="math_number">
                            <Field name="NUM">9</Field>
                        </Block>
                    </Value>
                </Block>
                <Block type="print"/>
            </Category>
            <Category colour="#717171" name="Detection">
                <Block type="test_react_field"/>,
                <Block type="test_react_date_field"/>,
                <Block type="text_charAt">
                    <Value name="VALUE">
                        <Block type="variables_get">
                            <Field name="VAR">text</Field>
                        </Block>
                    </Value>
                </Block>
                <Block type="Add"/>
                <Block type="math_number">
                    <Field name="NUM">0</Field>
                </Block>
                <Block type="area_of_circle">
                    <Value name="Area">
                        <Block type="math_number">
                            <Field name="NUM">9</Field>
                        </Block>
                    </Value>
                </Block>
                <Block type="print"/>
            </Category>
        </>
    );
}
