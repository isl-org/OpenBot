import Blockly from "blockly/core";
import {colors} from "./color";
import {toast} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import lightCarousal1 from "../assets/images/drag_drop_white.gif";
import darkCarousal1 from "../assets/images/drag_drop_dark.gif";
import lightCarousal2 from "../assets/images/scan_qr_code_light.gif";
import darkCarousal2 from "../assets/images/scan_qr_code_dark.gif";
import lightCarousal3 from "../assets/images/openBot_car_light.gif";
import darkCarousal3 from "../assets/images/openBot_car_dark.gif";


export const controlBlocksType = ["controls_if", "controls_ifelse", "logic_ternary", "logic_compare", "logic_operation", "logic_negate", "logic_boolean", "logic_null"];
export const loopBlocksType = ["controls_whileUntil", "controls_repeat", "controls_forEach", "controls_flow_statements"];


export const DarkTheme = Blockly.Theme.defineTheme('dark', {
    'base': Blockly.Themes.Classic,
    'blockStyles': {},
    'componentStyles': {
        'workspaceBackgroundColour': colors.blocklyBackground,
        'toolboxBackgroundColour': colors.blocklyBackground,
        'toolboxForegroundColour': colors.whiteFont,
        'flyoutBackgroundColour': '#686868',
    },
});

export const LightTheme = Blockly.Theme.defineTheme('light', {
    'base': Blockly.Themes.Classic,
    'componentStyles': {
        'workspaceBackgroundColour': colors.whiteBackground,
        'toolboxBackgroundColour': colors.whiteBackground,
        'toolboxForegroundColour': colors.blackFont,
        'flyoutBackgroundColour': '#686868',
    },
});

export const HelpCenterText = {
    dragAndDropFirstLine: "Drag and drop the selected block of code into the input field.",
    dragAndDropSecondLine: "Repeat the steps of selecting and placing all required code blocks, and then connecting them together to create a sequence of actions.",

    saveAndDownloadFirstLine: "Upload the code on your google drive and generate the QR code.\n",
    saveAndDownloadSecondLine: "Use the OpenBot application to scan the QR code and successfully save the block code on your device.",

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
    configData: "configData"
}

export const Constants = {
    LoadingTime: 800,
    ProfileSuccessMsg: "Profile updated successfully!",
    CookieMsg: "Our website use cookies. By continuing navigating, we assume your permission to deploy cookies as detailed in our Privacy Policy.",
    FolderName: "openBot-Playground",
    baseUrl: "https://www.googleapis.com/drive/v3",
    endCode: "\nstart();\nforever();\n",
    js: "js",
    py: "py",
    qr: "qr",
    xml: "xml",
    json:"json",
    tflite:"tflite",
    InternetOffMsg: "Please check your internet connection.",
}

export const Errors = {
    error1: "No start or forever block present in the playground.",
    error2: "More than one AI blocks enabled in the playground ",
}

export const Models = {
    type: [
        {value: 'AUTOPILOT'},
        {value: 'DETECTOR',},
        {value: 'NAVIGATION'}
    ],
    class: [
        {value: "AUTOPILOT_F"},
        {value: "MOBILENETV1_1_0_Q"},
        {value: "MOBILENETV3_S_Q",},
        {value: "YOLOV4",},
        {value: "NAVIGATION"}
    ]
}

export const Category = [
    "js", "py",
]

export const PathName = {
    "home": "/",
    "playGround": "/playground",
}

export const Carousal = [
    {
        carousalHeader: "Drag and Drop",
        carousalLine1: "Drag and drop the selected block of code into the input field.",
        carousalLine2: "Repeat the steps of selecting and placing all required code blocks, and then connecting them together to create a sequence of actions.",
        videoLight: lightCarousal1,
        videoDark: darkCarousal1,
    },
    {
        carousalHeader: "Save and Download",
        carousalLine1: "Check for errors by compiling the code and, upon successful compilation, generate the QR code.",
        carousalLine2: "Use the OpenBot android application to scan the QR code and successfully save the block code on your device.",
        videoLight: lightCarousal2,
        videoDark: darkCarousal2,
    },
    {
        carousalHeader: "Connect and Drive",
        carousalLine1: "Pair your smartphone with the OpenBot car and run the code that you have downloaded.",
        carousalLine2: "Carry out desired actions such as activating indicator lights, detecting objects, and more on your robot car.",
        videoLight: lightCarousal3,
        videoDark: darkCarousal3,
    }
]


/**
 * error Toast
 * @param message
 */
export const errorToast = (message) => {
    toast.error(message, {
        position: 'top-center',
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: false,
        draggable: true,
        progress: undefined,
        theme: 'colored',
        pauseOnFocusLoss: false,
    })
}
