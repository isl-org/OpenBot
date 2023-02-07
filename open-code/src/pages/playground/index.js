import React from 'react';
import BlocklyComponent from "../../components/blockly";
import {Toolbox} from "../../components/blockly/toolbox/Toolbox";
import {UploadCode} from "../../components/uploadCodeBar/uploadCode";
import {Header} from "../../components/navBar/header";
function Playground (){
        return (
            <div>
                <Header/>
            <header>
                <BlocklyComponent readOnly={false}
                                  move={{
                                      scrollbars: true,
                                      drag: true,
                                      wheel: true
                                  }}
                                  initialXml={`<xml xmlns="http://www.w3.org/1999/xhtml">
                                       <block type="controls_ifelse" x="0" y="100"></block>
                                       <xml xmlns="https://developers.google.com/blockly/xml"></xml>
                                      </xml>`}
                >
                    <Toolbox/>
                </BlocklyComponent>
            </header>

            <UploadCode/>
        </div>


    );

}

export default Playground;
