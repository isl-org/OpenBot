import React from 'react';
import BlocklyComponent from "../../components/blockly";
import {Toolbox} from "../../components/blockly/toolbox/Toolbox";
import {UploadCode} from "../../components/uploadCodeBar/uploadCode";
import {Header} from "../../components/navBar/header";
import {QrDrawer} from "../../components/drower/drower";
import {useLocation} from 'react-router-dom';
import {PathName} from "../../utils/constants";

function Playground() {
    const location = useLocation();
    return (
        <div>
            <Header/>
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

            <UploadCode/>
        </div>


    );

}

export default Playground;
