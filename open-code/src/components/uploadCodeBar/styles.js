import {colors} from "../../utils/color";

export const UploadBarStyle={
    barDiv:{
        display: "flex",
        backgroundColor: "white",
        justifyContent: "space-between",
        height: "5.5rem",
        alignItems: "center",
        position:"fixed",
        bottom:0,
        width:"100%",

    },
    uploadCodeButton:{
        marginLeft: "4.25rem",
        display:"flex",
        alignItems:"center",
        justifyContent: "center",
        border: "0.00621em solid #FFFEFE",
        borderRadius:"0.375rem",
        fontSize: "0.993em",
        height:"3.313rem",
        width:"13.563rem",
        fontFamily: "Gilroy-Medium",
        background:colors.openBotBlue,
        color:colors.whiteFont,
        cursor:"pointer",

    },
    operationsDiv:{
        display: "flex",
        alignItems: "center",
        marginRight: "4.25rem",
    },

    iconDiv:{
        height:"1.5rem",
        width:"1.5rem",
        pointerEvents:"none"
    },
    leftButton:{
        display:"flex",
        alignItems:"center",
        fontSize: "1.2em",
        pointerEvents: "none"
    },

    driveIconStyle:{
        height:"1.5rem",
        width:"1.5rem",
        cursor:"pointer"
    },

    buttonStyle:{
        width:"4.188rem",
        height:"3.313rem",
        background: colors.openBotBlue,
        border:"0.5px solid black",
        color: colors.whiteFont,
        fontFamily: "Gilroy-Medium",
        cursor:"pointer",

    },

    undoButtonStyle:{
        borderRadius: "0.375rem 0 0 0.375rem",
        border:"0.3px solid black"
    },

    commandSize:{
        width:"1.4rem",
        height:"1.2rem",
        pointerEvents:"none"
    },

    minusStyle:{
        borderRadius: "0.375rem 0 0 0.375rem",
    },

    plusStyle:{
        borderRadius:"0 0.375rem 0.375rem 0"
    },
    operationSize:{
        fontSize : "40px",
        color: colors.whiteFont,
        pointerEvents:"none",
        fontWeight:"100"
    },

    iconMargin: {
        margin: 10,
    },

    driveStyle:{
        border:"none",
        background:"none"
    },

    buttonColor:{
        background: "#0071c5",
        opacity: "0.8",
    }


};