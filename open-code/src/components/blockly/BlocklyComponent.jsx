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
 * Blockly workspace component
 * @param {Object} props - component props
 * @param {string} props.initialXml - initial XML code to load into the workspace
 * @param {Function} props.onWorkspaceChange - function to call when workspace changes
 * @param {Object} props.rest - additional props to pass to Blockly inject method
 * @returns {JSX.Element}
 */
function BlocklyComponent(props) {
    const {initialXml, children, onWorkspaceChange, ...rest} = props;

    // Refs for the workspace, toolbox, and blockly div
    const blocklyDiv = useRef();
    const toolbox = useRef();
    const primaryWorkspace = useRef();

    // Get context values from the store
    const {theme} = useContext(ThemeContext);
    const {projectName, currentProjectId, currentProjectXml, fileId, folderId, setDrawer} = useContext(StoreContext);

    const uniqueId = currentProjectId ? currentProjectId : nanoid()    // Generate a unique ID for the workspace if it doesn't exist
    const themes = useTheme();
    const isMobile = useMediaQuery(themes.breakpoints.down('md'));

    // Save workspace code to local storage when workspace changes
    const handleWorkspaceChange = useCallback(() => {
        setDrawer(false);
        if (projectName !== undefined) {
            updateCurrentProject(uniqueId, projectName, Blockly.Xml.domToText(Blockly.Xml.workspaceToDom(Blockly.getMainWorkspace())), fileId, folderId);
        }
        // if (onWorkspaceChange) {
        //     onWorkspaceChange();
        // }
    }, [projectName, uniqueId, fileId, folderId, setDrawer, onWorkspaceChange]);

    useEffect(() => {
        // Create the primary workspace instance
        primaryWorkspace.current = Blockly.inject(blocklyDiv.current, {
            theme: theme === "dark" ? DarkTheme : LightTheme,
            renderer: "zelos",
            toolbox: toolbox.current,
            ...rest,
        });

        // Set the zoom level for mobile devices
        const zoomLevel = 0.7;
        if (isMobile) {
            primaryWorkspace.current.setScale(zoomLevel);
        }

        // Create and render a new modal instance
        const model = new Modal(primaryWorkspace.current);
        model.init();
        model.render({
            shouldCloseOnOverlayClick: true,
            shouldCloseOnEsc: true,
        });

        // Add change listener to the workspace and disable orphaned blocks
        primaryWorkspace.current.addChangeListener(handleWorkspaceChange);
        primaryWorkspace.current.addChangeListener(Blockly.Events.disableOrphans);

        // Load XML code into the workspace if it exists, otherwise load initial XML code
        if (currentProjectXml) {
            Blockly.Xml.domToWorkspace(Blockly.utils.xml.textToDom(currentProjectXml), primaryWorkspace.current);
        } else {
            Blockly.Xml.domToWorkspace(Blockly.utils.xml.textToDom(initialXml), primaryWorkspace.current);
        }

        // Clean up the workspace when the component unmounts
        return () => {
            primaryWorkspace.current.dispose();
        }
    }, [theme, toolbox, blocklyDiv, props]);

    // Return the blockly div and hidden toolbox
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
