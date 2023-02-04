
import './BlocklyComponent.css';
import React,{useEffect, useRef,useContext} from 'react';

import Blockly from 'blockly/core';
import locale from 'blockly/msg/en';
import 'blockly/blocks';
import {StoreContext} from "../../context/context";

Blockly.setLocale(locale);


function BlocklyComponent(props) {
    const blocklyDiv = useRef();
    const toolbox = useRef();
    let primaryWorkspace = useRef();
    const {workspaceWidth,setWorkspaceWidth} = useContext(StoreContext)
    useEffect(() => {

        const {initialXml, children, ...rest} = props;
        primaryWorkspace.current = Blockly.inject(
            blocklyDiv.current,
            {
                toolbox: toolbox.current,
                ...rest
            },
        );
        Blockly.Xml.domToWorkspace(Blockly.Xml.textToDom(initialXml), primaryWorkspace.current);

        return () => {
            primaryWorkspace.current.dispose();
        }
    }, [primaryWorkspace, toolbox, blocklyDiv, props]);

    return (
        <React.Fragment>
            <div ref={blocklyDiv} id="blocklyDiv" style={{width : workspaceWidth + "%"}}/>
            <div style={{display: 'none'}} ref={toolbox}>
                {props.children}
            </div>
        </React.Fragment>)

}


export default BlocklyComponent;
