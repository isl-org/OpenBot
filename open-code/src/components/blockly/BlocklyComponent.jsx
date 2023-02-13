
import './BlocklyComponent.css';
import React,{useEffect, useRef,useContext} from 'react';
import Blockly from 'blockly/core';
import locale from 'blockly/msg/en';
import 'blockly/blocks';
import {ThemeContext} from "../../App";
import {DarkTheme, LightTheme} from "../../utils/constants";
Blockly.setLocale(locale);


function BlocklyComponent(props) {
    const blocklyDiv = useRef();
    const {theme} = useContext(ThemeContext)
    const toolbox = useRef();
    let primaryWorkspace = useRef();
    useEffect(() => {

        const {initialXml, children, ...rest} = props;
        primaryWorkspace.current = Blockly.inject(
            blocklyDiv.current,
            {
                theme: (theme === "dark" ? DarkTheme : LightTheme),
                toolbox: toolbox.current,
                ...rest
            },
        );
        Blockly.Xml.domToWorkspace(Blockly.Xml.textToDom(initialXml), primaryWorkspace.current);

        return () => {
            primaryWorkspace.current.dispose();
        }
    }, [theme,primaryWorkspace, toolbox, blocklyDiv, props]);

    return (
        <React.Fragment>
            <div ref={blocklyDiv} id="blocklyDiv" style={{width : 100 + "%", height: "81.4%"}}/>
            <div style={{display: 'none'}} ref={toolbox}>
                {props.children}
            </div>
        </React.Fragment>)

}


export default BlocklyComponent;

