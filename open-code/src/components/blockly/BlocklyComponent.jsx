import './BlocklyComponent.css';
import React, {useEffect, useRef, useContext, useCallback} from 'react';
import Blockly from 'blockly/core';
import locale from 'blockly/msg/en';
import 'blockly/blocks';
import {ThemeContext} from "../../App";
import {DarkTheme, LightTheme} from "../../utils/constants";
import {Modal} from "@blockly/plugin-modal";
import {StoreContext} from "../../context/context";
import {generatePath, getCurrentProject, saveCurrentProject} from "../../services/workspace";

Blockly.setLocale(locale);

function BlocklyComponent(props) {
    const {initialXml, children, onWorkspaceChange, ...rest} = props;
    const blocklyDiv = useRef();
    const {theme} = useContext(ThemeContext);
    const toolbox = useRef();
    const primaryWorkspace = useRef();

    const {projectName, setProjectName, currentProjectXml, currentProjectId} = useContext(StoreContext);
    const {logOut} = useContext(StoreContext);

    const uniqueId = currentProjectId ? currentProjectId : generatePath(projectName)

    /**
     * save code in local to restore on reload page
     * @type {(function(): void)|*}
     */
    const handleWorkspaceChange = useCallback(() => {
        if (projectName !== undefined) {
            saveCurrentProject(uniqueId, projectName, Blockly.Xml.domToText(Blockly.Xml.workspaceToDom(Blockly.getMainWorkspace())));
        }
    }, []);

    useEffect(() => {
        primaryWorkspace.current = Blockly.inject(blocklyDiv.current, {
            theme: theme === "dark" ? DarkTheme : LightTheme,
            renderer: "zelos",
            toolbox: toolbox.current,
            ...rest,
        });

        const savedXml = getCurrentProject()

        const model = new Modal(primaryWorkspace.current);
        model.init();
        model.render({
            shouldCloseOnOverlayClick: true,
            shouldCloseOnEsc: true
        });

        primaryWorkspace.current.addChangeListener(handleWorkspaceChange);

        //blocks fetching from firebase in card.js
        if (logOut !== true && currentProjectXml) {
            Blockly.Xml.domToWorkspace(Blockly.Xml.textToDom(currentProjectXml), primaryWorkspace.current);
        } else if (savedXml) {
            const getXmlValue = Object.values(savedXml).toString()
            Blockly.Xml.domToWorkspace(Blockly.Xml.textToDom(getXmlValue), primaryWorkspace.current);
        } else {
            Blockly.Xml.domToWorkspace(Blockly.Xml.textToDom(initialXml), primaryWorkspace.current);
        }

        return () => {
            primaryWorkspace.current.dispose();
        }
    }, [theme, toolbox, blocklyDiv, props]);

    return (
        <React.Fragment>
            <div
                ref={blocklyDiv}
                id="blocklyDiv"
                style={{width: "100%", height: "81.6%"}}
            />
            <div style={{display: "none"}} ref={toolbox}>
                {children}
            </div>
        </React.Fragment>
    );
}

export default BlocklyComponent;
