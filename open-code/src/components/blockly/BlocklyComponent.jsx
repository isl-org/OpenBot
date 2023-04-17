import './BlocklyComponent.css';
import React, {useCallback, useContext, useEffect, useRef} from 'react';
import Blockly from 'blockly/core';
import locale from 'blockly/msg/en';
import 'blockly/blocks';
import {ThemeContext} from "../../App";
import {DarkTheme, LightTheme} from "../../utils/constants";
import {Modal} from "@blockly/plugin-modal";
import {StoreContext} from "../../context/context";
import {updateCurrentProject} from "../../services/workspace";
import {nanoid} from "nanoid";
import {useTheme} from "@mui/material";
import useMediaQuery from "@mui/material/useMediaQuery";

Blockly.setLocale(locale);

/**
 * WorkSpace
 * @param props
 * @returns {JSX.Element}
 * @constructor
 */
function BlocklyComponent(props) {
    const {initialXml, children, onWorkspaceChange, ...rest} = props;
    const blocklyDiv = useRef();
    const {theme} = useContext(ThemeContext);
    const toolbox = useRef();
    const primaryWorkspace = useRef();
    const {projectName, currentProjectId, currentProjectXml, fileId, folderId, setDrawer} = useContext(StoreContext);
    const uniqueId = currentProjectId ? currentProjectId : nanoid()
    const themes = useTheme();
    const isMobile = useMediaQuery(themes.breakpoints.down('md'));

    // save code in local to restore on reload page
    const handleWorkspaceChange = useCallback(() => {
        setDrawer(false);
        if (projectName !== undefined) {
            updateCurrentProject(uniqueId, projectName, Blockly.Xml.domToText(Blockly.Xml.workspaceToDom(Blockly.getMainWorkspace())), fileId, folderId);
        }

    }, []);


    useEffect(() => {
        primaryWorkspace.current = Blockly.inject(blocklyDiv.current, {
            theme: theme === "dark" ? DarkTheme : LightTheme,
            renderer: "zelos",
            toolbox: toolbox.current,
            ...rest,
        });

        const zoomLevel = 0.7;
        if (isMobile)
            primaryWorkspace.current.setScale(zoomLevel);

        const model = new Modal(primaryWorkspace.current);
        model.init();
        model.render({
            shouldCloseOnOverlayClick: true,
            shouldCloseOnEsc: true,
        });

        primaryWorkspace.current.addChangeListener(handleWorkspaceChange);
        primaryWorkspace.current.addChangeListener(Blockly.Events.disableOrphans);

        //blocks fetching from drive in card.js
        if (currentProjectXml) {
            Blockly.Xml.domToWorkspace(Blockly.utils.xml.textToDom(currentProjectXml), primaryWorkspace.current);
        } else {
            Blockly.Xml.domToWorkspace(Blockly.utils.xml.textToDom(initialXml), primaryWorkspace.current);
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
