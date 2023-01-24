import React from 'react';
import './BlocklyComponent.css';
import {useEffect, useRef} from 'react';

import Blockly from 'blockly/core';
import {javascriptGenerator} from 'blockly/javascript';
import locale from 'blockly/msg/en';
import 'blockly/blocks';

Blockly.setLocale(locale);

function BlocklyComponent(props) {
    const blocklyDiv = useRef();
    const toolbox = useRef();
    let primaryWorkspace = useRef();

    const generateCode = () => {
        var code = javascriptGenerator.workspaceToCode(
            primaryWorkspace.current
        );
        console.log(code);
    }

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
            <div ref={blocklyDiv} id="blocklyDiv"/>
            <div style={{display: 'none'}} ref={toolbox}>
                {props.children}
            </div>
            <button onClick={generateCode}>Convert</button>
        </React.Fragment>);
}

export default BlocklyComponent;
 