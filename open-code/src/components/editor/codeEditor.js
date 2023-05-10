import React, {useEffect, useRef} from 'react';
import ace from 'ace-builds';
import 'ace-builds/src-noconflict/mode-javascript';
import 'ace-builds/src-noconflict/theme-chrome';
import {useContext} from "react";
import {StoreContext} from "../../context/context";
import {javascriptGenerator} from "blockly/javascript";
import {pythonGenerator} from "blockly/python";
import {Constants} from "../../utils/constants";


/**
 * Code Editor to display Js code and python code.
 * @param params
 * @returns {JSX.Element}
 * @constructor
 */
function CodeEditor(params) {
    const editorRef = useRef(null);
    const {workspace, currentProjectXml,category,drawer} = useContext(StoreContext);

    useEffect(() => {
        console.log("workSpaceChanged::", workspace)
        const editor = ace.edit(editorRef.current);

        let code;
        let mode;
        let theme = "ace/theme/chrome";

        if (category === Constants.py) {
            code = pythonGenerator.workspaceToCode(workspace);
            mode = "ace/mode/python";

        } else {
            code = javascriptGenerator.workspaceToCode(workspace);
            mode = "ace/mode/javascript";

        }
        console.log("code",code)
        editor.session.setMode(mode);
        editor.setTheme('ace/theme/chrome');
        editor.setValue(code);

        return () => {
            editor.destroy();
        };
    }, [workspace, currentProjectXml,category,drawer]);

    return <div ref={editorRef} style={{height: '100%',width:'100%'}}/>;
}

export default CodeEditor;
