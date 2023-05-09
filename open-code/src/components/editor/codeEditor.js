import React, {useEffect, useRef} from 'react';
import ace from 'ace-builds';
import 'ace-builds/src-noconflict/mode-javascript';
import 'ace-builds/src-noconflict/theme-chrome';
import {useContext} from "react";
import {StoreContext} from "../../context/context";
import {javascriptGenerator} from "blockly/javascript";


/**
 * Code Editor to display Js code and python code.
 * @param params
 * @returns {JSX.Element}
 * @constructor
 */
function CodeEditor(params) {
    const editorRef = useRef(null);
    const {workspace,currentProjectXml} = useContext(StoreContext);
    useEffect(() => {
        console.log("workSpaceChanged::",workspace)
        const editor = ace.edit(editorRef.current);
        let code = javascriptGenerator.workspaceToCode(
            workspace
        ) ;

        editor.session.setMode('ace/mode/javascript');
        editor.setTheme('ace/theme/chrome');
        editor.setValue(code);

        return () => {
            editor.destroy();
        };
    }, [workspace,currentProjectXml]);

    return <div ref={editorRef} style={{height: '100%'}}/>;
}

export default CodeEditor;
