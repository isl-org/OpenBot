import React from 'react';
import BlocklyComponent, {Block, Category, ColorBar, Field, Shadow, Value} from "../Blockly";

function Playground (){
        return (

                <div className="App">
                    <BlocklyComponent readOnly={false}
                                      trashcan={true} media={'media/'}
                                      move={{
                                          scrollbars: true,
                                          drag: true,
                                          wheel: true
                                      }}
                                      initialXml={`
<xml xmlns="http://www.w3.org/1999/xhtml">
<block type="controls_ifelse" x="0" y="0"></block>
</xml>
      `}>
                        <Category name='Basic' >
                            <Block type="test_react_field"/>
                            <Block type ="test_react_date_field"/>
                            <Block type="controls_ifelse"/>
                            <Block type="logic_compare"/>
                            <Block type="logic_operation"/>
                        </Category>
                        <Category name='Loops'>
                            <Block type="controls_repeat_ext">
                                <Value name="TIMES">
                                    <Shadow type="math_number">
                                        <Field name="NUM">10</Field>
                                    </Shadow>
                                </Value>
                            </Block>
                            <Block type="logic_operation"/>
                            <Block type="logic_negate"/>
                            <Block type="logic_boolean"/>
                            <Block type="logic_null" disabled="true"/>
                            <Block type="logic_ternary"/>
                            <Block type="text_charAt">
                                <Value name="VALUE">
                                    <Block type="variables_get">
                                        <Field name="VAR">text</Field>
                                    </Block>
                                </Value>
                            </Block>
                        </Category>
                        <Category name='Custom'>
                            <Block type="print"/>
                            <Block type="input_text"/>
                            <Block type='append_string'/>
                        </Category>
                    </BlocklyComponent>

                </div>
        );

}

export default Playground;