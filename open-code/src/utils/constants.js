import User from "../assets/images/icon/user.png";
import Moon from "../assets/images/icon/darkMode/dark-mode-icon.png";
import Help from "../assets/images/icon/help-circle.png";
import Logout from "../assets/images/icon/log-out.png";
import Blockly from "blockly/core";

export const Content = [
    {
        Icon: User,
        title: "My profile",
    },
    {
        Icon: Moon,
        title: "Change Theme",
    },
    {
        Icon: Help,
        title: "How To Upload",
    },
    {
        Icon: Logout,
        title: "Logout",
    },
]

export const controlBlocksType = ["controls_if", "controls_ifelse", "logic_ternary", "logic_compare", "logic_operation", "logic_negate", "logic_boolean", "logic_null"];
export const loopBlocksType = ["controls_whileUntil", "controls_repeat", "controls_forEach", "controls_flow_statements"];


/**
 * Dark theme.
 */
export const DarkTheme = Blockly.Theme.defineTheme('dark', {
    'base': Blockly.Themes.Classic,
    'componentStyles': {
        'workspaceBackgroundColour': '#1e1e1e',
        'toolboxBackgroundColour': 'blackBackground',
        'toolboxForegroundColour': '#fff',
        'flyoutBackgroundColour': '#252526',
        'flyoutForegroundColour': '#ccc',
        'flyoutOpacity': 1,
        'scrollbarColour': '#797979',
        'insertionMarkerColour': '#fff',
        'insertionMarkerOpacity': 0.3,
        'scrollbarOpacity': 0.4,
        'cursorColour': '#d0d0d0',
        'blackBackground': '#333',
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
