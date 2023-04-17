import React from 'react';
import BlocklyComponent from "../../components/blockly";
import {Toolbox} from "../../components/blockly/toolbox/Toolbox";
import {BottomBar} from "../../components/bottomBar/bottomBar";
import {Header} from "../../components/navBar/header";
import {QrDrawer} from "../../components/drower/drower";
import {useLocation} from 'react-router-dom';
import {PathName} from "../../utils/constants";


/**
 * Playground Screen
 * @returns {JSX.Element}
 * @constructor
 */
function Playground() {
    const location = useLocation();
    return (
        <div>
            <Header/>
            {/* show QR Drawer */}
            {location.pathname === PathName.playGround ? <QrDrawer/> : ""}
            <header>
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
            <BottomBar/>
        </div>


    );

}

export default Playground;
