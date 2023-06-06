import React from 'react';
import BlocklyComponent from "../../components/blockly";
import {Toolbox} from "../../components/blockly/toolbox/Toolbox";
import {BottomBar} from "../../components/bottomBar/bottomBar";
import {Header} from "../../components/navBar/header";

/**
 * Playground Screen :: Displays the Playground screen which contains a Blockly workspace.
 * @returns {JSX.Element}
 * @constructor
 */
function Playground() {
    return (
        <div>
            <Header/>
            {/* If the current URL pathname is for the Playground screen, display the QR Drawer component */}
            <header>

                {/* Display the Blockly workspace */}
                <BlocklyComponent readOnly={false}
                                  move={{
                                      scrollbars: true,
                                      drag: true,
                                      wheel: true
                                  }}
                                  initialXml={`<xml xmlns="http://www.w3.org/1999/xhtml">
                                       <Block type="start" x="0" y="100"/>
                                       <Block type="forever" x="250" y="100"/>
                                      </xml>`}
                >
                    <Toolbox/>
                </BlocklyComponent>
            </header>
            {/* Display the BottomBar component */}
            <BottomBar/>
        </div>
    );

}

export default Playground;
