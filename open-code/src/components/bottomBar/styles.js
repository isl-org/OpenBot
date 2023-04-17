import {colors} from "../../utils/color";

export const UploadBarStyle = {
    barDiv: {
        display: "flex",
        backgroundColor: "white",
        justifyContent: "space-between",
        alignItems: "center",
        position: "fixed",
        bottom: 0,

    },
    uploadCodeButton: {
        marginLeft: "4rem",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        border: "none",
        borderRadius: "0.5rem",
        height: "3.1rem",
        width: "95%",
        background: colors.openBotBlue,
        cursor: "pointer",

    },
    operationsDiv: {
        display: "flex",
        alignItems: "center",
        marginRight: "4rem",
    },

    iconDiv: {
        height: "1.5rem",
        width: "1.5rem",
        pointerEvents: "none"
    },
    leftButton: {
        display: "flex",
        fontFamily: "Gilroy-medium",
        alignItems: "center",
        fontSize: "1.2rem",
        color: colors.whiteFont,
        pointerEvents: "none"
    },

    driveIconStyle: {
        height: "1.5rem",
        width: "1.5rem",
        cursor: "pointer"
    },

    buttonStyle: {
        width: "4.2rem",
        height: "3.1rem",
        background: colors.openBotBlue,
        border: "none",
        color: colors.whiteFont,
        fontFamily: "Gilroy-Medium",
        cursor: "pointer",

    },
    undoButtonStyle: {
        borderRadius: "0.4rem 0 0 0.4rem",
        border: "none",
    },

    borderStyle: {
        borderRight: "1px solid black"
    },

    commandSize: {
        width: "1.4rem",
        height: "1.2rem",
        pointerEvents: "none"
    },
    minusStyle: {
        borderRadius: "0.4rem 0 0 0.4rem",
    },

    plusStyle: {
        borderRadius: "0 0.4rem 0.4rem 0"
    },
    operationSize: {
        fontSize: "2.4rem",
        color: colors.whiteFont,
        pointerEvents: "none",
        fontWeight: "100"
    },

    iconMargin: {
        margin: 10,
    },

    driveStyle: {
        border: "none",
        background: "none"
    },

    buttonColor: {
        background: "#0071c5",
        opacity: "0.8",
    },
    buttonMargin: {
        marginRight: 1
    }


};