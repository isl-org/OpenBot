import React, {useEffect, useRef} from 'react';
import ace from 'ace-builds';
import 'ace-builds/src-noconflict/mode-javascript';
import 'ace-builds/src-noconflict/mode-python';
import {useContext} from "react";
import {StoreContext} from "../../context/context";
import {javascriptGenerator} from "blockly/javascript";
import {pythonGenerator} from "blockly/python";
import {Constants} from "../../utils/constants";
import {ThemeContext} from "../../App";
import 'ace-builds/src-noconflict/theme-one_dark';
import 'ace-builds/src-noconflict/theme-textmate';
import {RightSlider} from "../drawer/drawer";
import useMediaQuery from "@mui/material/useMediaQuery";
import {useTheme} from "@mui/material";


/**
 * Code Editor to display Js code and python code.
 * @param params
 * @returns {JSX.Element}
 * @constructor
 */
function CodeEditor(params) {
    const editorRef = useRef(null);
    const {workspace, currentProjectXml, category, drawer} = useContext(StoreContext);
    const themes = useTheme();// Get the current theme breakpoints using useTheme hook
    const isMobile = useMediaQuery(themes.breakpoints.down('md'));// Determine if the screen is a mobile device using useMediaQuery hook
    const isMobileLandscape = window.matchMedia("(max-width: 1000px) and (orientation: landscape)").matches
    const {theme} = useContext(ThemeContext);

    useEffect(() => {
        const editor = ace.edit(editorRef.current);
        let code;
        let mode;

        if (category === Constants.py) {
            code = pythonGenerator.workspaceToCode(workspace);
            mode = "ace/mode/python";

        } else {
            code = javascriptGenerator.workspaceToCode(workspace);
            mode = "ace/mode/javascript";
        }

        editor.session.setMode(mode);
        editor.setReadOnly(true);
        editor.setTheme(theme === "dark" ? "ace/theme/one_dark" : "ace/theme/textmate");
        editor.session.setMode(mode);
        editor.setValue(code);
        const gutterEl = editor.renderer.$gutter;
        gutterEl.style.color = theme === "dark" ? "white" : "black";
        gutterEl.style.width = "70px"
        const cursor = editor.renderer.$cursorLayer.cursor;
        cursor.style.color = theme === "dark" ? "white" : "black";
        editor.renderer.$gutterLayer.element.style.marginLeft = "8px"
        return () => {
            editor.destroy();
        };

    }, [workspace, currentProjectXml, category, drawer, theme]);

    return (<div>
        <div style={{zIndex: 2, position: "absolute", marginTop: isMobileLandscape ? "230px":"300px"}}><RightSlider/></div>
        <div ref={editorRef} style={{
            position: "absolute",
            zIndex: 1,
            height: '100%',
            width: '100%',
            backgroundColor: theme === "dark" ? "#202020" : '#FFFFFF',
        }}/>
    </div>)
}


export default CodeEditor;
