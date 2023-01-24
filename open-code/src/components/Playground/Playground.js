import React from 'react';
import BlocklyComponent from "../Blockly";
import Toolbox from "../Blockly/toolbox/Toolbox";

function Playground (){
        return (

            <header>
                <BlocklyComponent readOnly={false}
                                  trashcan={true} media={'media/'}
                                  move={{
                                      scrollbars: true,
                                      drag: true,
                                      wheel: true
                                  }}
                                  initialXml={`<xml xmlns="http://www.w3.org/1999/xhtml">
                                       <block type="controls_ifelse" x="0" y="0"></block>
                                       <xml xmlns="https://developers.google.com/blockly/xml"></xml>
                                      </xml>`}
                >
                    <Toolbox/>
                </BlocklyComponent>
            </header>

        );

}

export default Playground;