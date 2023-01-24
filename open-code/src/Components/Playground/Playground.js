import React from 'react';
import BlocklyComponent from "../Blockly";
import Toolbox from "../Blockly/toolbox/Toolbox";

function Playground (){
        return (

            <header>
                <BlocklyComponent readOnly={false}
                                  trashcan={true}
                                  media={'media/'}
                                  move={{
                                      scrollbars: false,
                                      drag: true,
                                      wheel: true
                                  }}
                                  initialXml={`<xml></xml>`}
                >
                    <Toolbox/>
                </BlocklyComponent>
            </header>

        );

}

export default Playground;