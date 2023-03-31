import Blockly from "blockly/core";
import {colors} from "./color";
import React from "react";
import {Images} from "./images";
import {toast} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
export const controlBlocksType = ["controls_if", "controls_ifelse", "logic_ternary", "logic_compare", "logic_operation", "logic_negate", "logic_boolean", "logic_null"];
export const loopBlocksType = ["controls_whileUntil", "controls_repeat", "controls_forEach", "controls_flow_statements"];


/**
 * Dark theme.
 */
export const DarkTheme = Blockly.Theme.defineTheme('dark', {
    'base': Blockly.Themes.Classic,
    'componentStyles': {
        'workspaceBackgroundColour': colors.blocklyBackground,
        'toolboxBackgroundColour': colors.blocklyBackground,
        'toolboxForegroundColour': colors.whiteFont,
        'flyoutBackgroundColour': 'rgba(211, 211, 211, 0.8)',
    },
});

export const LightTheme = Blockly.Theme.defineTheme('light', {
    'base': Blockly.Themes.Classic,
    'componentStyles': {
        'workspaceBackgroundColour': colors.whiteBackground,
        'toolboxBackgroundColour': colors.whiteBackground,
        'toolboxForegroundColour': colors.blackFont,
        'flyoutBackgroundColour': 'rgba(211, 211, 211, 0.8)',
    },
});

export const HelpCenterText = {
    dragAndDropFirstLine: "Drag and drop the selected block of code into the input field.",
    dragAndDropSecondLine: "Repeat the steps of selecting and placing all required code blocks, and then connecting them together to create a sequence of actions.",

    saveAndDownloadFirstLine: "Check for errors by compiling the code and, upon successful compilation, generate the QR code.",
    saveAndDownloadSecondLine: "Use the OpenBot android application to scan the QR code and successfully save the block code on your device.",

    connectAndDriveFirstLine: "Pair your smartphone with the OpenBot car and run the code that you have downloaded.",
    connectAndDriveSecondLine: "Carry out desired actions such as activating indicator lights, detecting objects, and more on your robot car.",
}

export const Themes = {
    "light": "light",
    "dark": "dark",
}


export const localStorageKeys = {
    allProjects: "projects",
    currentProject: "currentProject",
    accessToken: "accessToken",
}

export const Constants = {
    LoadingTime: 800,
    ProfileSuccessMsg: "Profile updated successfully!",
    CookieMsg: "Our website use cookies. By continuing navigating, we assume your permission to deploy cookies as detailed in our Privacy Policy.",
    FolderName: "openCode-openBot",
    baseUrl: "https://www.googleapis.com/drive/v3",
    endCode: "\nstart();\n" + "forever();\n",
    js: "js",
    xml: "xml"
}

export const PathName = {
    "home": "/",
    "playGround": "/playground",
}

export const Carousal = [
    {
        carousalHeader: "Drag and Drop",
        carousalLine1: "Drag and drop the selected block of code into the input field.",
        carousalLine2: "Repeat the steps of selecting and placing all required code blocks, and then connecting them together to create a sequence of actions.",
        videoLight: Images.dragDropWhite,
        videoDark: Images.dragDropDark,
    },
    {
        carousalHeader: "Save and Download",
        carousalLine1: "Check for errors by compiling the code and, upon successful compilation, generate the QR code.",
        carousalLine2: "Use the OpenBot android application to scan the QR code and successfully save the block code on your device.",
        videoLight: Images.Carousal2,
        videoDark: Images.Carousal2Dark,
    },
    {
        carousalHeader: "Connect and Drive",
        carousalLine1: "Pair your smartphone with the OpenBot car and run the code that you have downloaded.",
        carousalLine2: "Carry out desired actions such as activating indicator lights, detecting objects, and more on your robot car.",
        videoLight: Images.Carousal3,
        videoDark: Images.Carousal3Dark,
    }
]


export const errorToast = (message) => {
    toast.error(message, {
        position: 'top-center',
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: false,
        draggable: true,
        progress: undefined,
        theme: 'colored',
        pauseOnFocusLoss: false,
    })
}
