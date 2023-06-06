import './BlocklyComponent.css';
import React, {useCallback, useContext, useEffect, useRef, useState} from 'react';
import Blockly from 'blockly/core';
import locale from 'blockly/msg/en';
import 'blockly/blocks';
import {ThemeContext} from "../../App";
import {Constants, DarkTheme, errorToast, LightTheme} from "../../utils/constants";
import {Modal} from "@blockly/plugin-modal";
import {StoreContext} from "../../context/context";
import {getCurrentProject, updateCurrentProject} from "../../services/workspace";
import {useTheme} from "@mui/material";
import useMediaQuery from "@mui/material/useMediaQuery";
import {checkFileExistsInFolder, getFolderId, getShareableLink} from "../../services/googleDrive";


Blockly.setLocale(locale);

/**
 * Blockly workspace component
 *
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
    const {
        projectName,
        currentProjectXml,
        fileId,
        folderId,
        setDrawer,
        setWorkspace,
        setIsError,
        setCurrentProjectXml,
        isOnline,
        setCode,
        setCategory,
        category,
    } = useContext(StoreContext);
    const themes = useTheme();
    const isMobile = useMediaQuery(themes.breakpoints.down('sm'));

    // Save workspace code to local storage when workspace changes
    const handleWorkspaceChange = useCallback(() => {
        if (category === Constants.xml) {
            setDrawer(false);
        }
        setIsError(false);
        if (projectName !== undefined) {
            setCurrentProjectXml(Blockly.Xml.domToText(Blockly.Xml.workspaceToDom(Blockly.getMainWorkspace())));
            updateCurrentProject(projectName, Blockly.Xml.domToText(Blockly.Xml.workspaceToDom(Blockly.getMainWorkspace())), fileId, folderId);
        }

    }, []);

    const enableAllChildBlocks = (block) => {
        if (block) {
            block.setEnabled(true);
            const children = block.getChildren();
            for (let i = 0; i < children.length; i++) {
                enableAllChildBlocks(children[i]);
            }
        }
    };
    const disableChildBlocks = (blocks) => {
        if (blocks.length > 1) {
            for (let i = 1; i < blocks.length; i++) {
                blocks[i].setEnabled(false);
            }
        }
    };
    const handlingBlocks = (event, blockType) => {
        let existingBlocks = primaryWorkspace.current.getBlocksByType(blockType);
        if (event.type === Blockly.Events.CREATE && event.blockId) {
            let block = primaryWorkspace.current.getBlockById(event.blockId);
            if (block.type === blockType) {
                disableChildBlocks(existingBlocks);
            }
        } else if (event.type === Blockly.Events.DELETE && event.blockId) {
            if (existingBlocks.length > 0) {
                existingBlocks[0].setEnabled(true);
                enableAllChildBlocks(existingBlocks[0]);
            }
        }
    };

    //handling duplicate for start and forever
    const handleDuplicateBlocks = useCallback((event) => {
        const blockTypes = ["start", "forever", "onBumper"];
        blockTypes.forEach((blockType) => {
            handlingBlocks(event, blockType);
        });
    }, []);

    const checkQRCode = async () => {

        if (localStorage.getItem("isSigIn") === "true") {
            if (isOnline) {
                let folderId = await getFolderId();
                if (folderId) {
                    let fileExistWithFileID = await checkFileExistsInFolder(folderId, getCurrentProject().projectName, 'js')
                    if (fileExistWithFileID.exists) {
                        let QrLink = await getShareableLink(fileExistWithFileID.fileId, folderId)
                        let linkCode = {
                            driveLink: QrLink,
                            projectName: getCurrentProject().projectName
                        }
                        category === "" && setCategory(Constants.qr);
                        setCode(linkCode);
                    }
                }
            } else {
                errorToast("Please Check your internet connection.")
            }
        } else {
            // setCategory(category === Constants.py ? Constants.py : Constants.js);
        }
    }

    useEffect(() => {
        setCategory(Constants.js);
        checkQRCode().catch(err => {
            console.log(err);
        });

    }, [])

    useEffect(() => {
        // Create the primary workspace instance
        primaryWorkspace.current = Blockly.inject(blocklyDiv.current, {
            theme: theme === "dark" ? DarkTheme : LightTheme,
            renderer: "zelos",
            toolbox: toolbox.current,
            zoom:
                {
                    controls: false,
                    wheel: true,
                    startScale: 1.0,
                    maxScale: 3,
                    minScale: 0.2,
                    scaleSpeed: 1.5,
                    pinch: true
                },
            ...rest,
        });

        // Set the zoom level for mobile devices
        //decreased size of blocks in mobile view
        const zoomLevel = 0.7;
        if (isMobile) {
            primaryWorkspace.current.setScale(zoomLevel);
        }

        //storing current workspace behaviour and features
        setWorkspace(primaryWorkspace.current)

        // Create and render a new modal instance
        const model = new Modal(primaryWorkspace.current);
        model.init();
        model.render({
            shouldCloseOnOverlayClick: true,
            shouldCloseOnEsc: true,
        });

        // Add change listener to the workspace
        primaryWorkspace.current.addChangeListener(handleWorkspaceChange);

        //blocks who are not attach to start or forever gets disabled
        primaryWorkspace.current.addChangeListener(Blockly.Events.disableOrphans);

        //handle start and forever duplicate blocks occurrence
        primaryWorkspace.current.addChangeListener(handleDuplicateBlocks);

        // Load XML code into the workspace if it exists, otherwise load initial XML code
        if (currentProjectXml) {
            Blockly.Xml.domToWorkspace(Blockly.utils.xml.textToDom(currentProjectXml), primaryWorkspace.current);
            Blockly.getMainWorkspace().clearUndo() //to clear undoStack on loading blocks
        } else {
            Blockly.Xml.domToWorkspace(Blockly.utils.xml.textToDom(initialXml), primaryWorkspace.current);
            Blockly.getMainWorkspace().clearUndo() //to clear undoStack on loading blocks
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
                style={{
                    width: "100%",
                    backgroundColor: theme === "dark" ? "#202020" : "#FFFFFF",
                }}
            />
            <div style={{display: "none"}} ref={toolbox}>
                {children}
            </div>
        </React.Fragment>
    );
}

export default BlocklyComponent;
