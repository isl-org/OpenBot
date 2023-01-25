import {colors} from "../../utils/color";
import undoIcon from "../../assets/images/undo.png";

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
        border: "1px solid #000000"
    },
    uploadCodeButton:{
        marginLeft: "10.25rem",
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
        color:colors.whiteFont
    },
    operationsDiv:{
        display: "flex",
        alignItems: "center",
        marginRight: "4.25rem",
    },

    iconDiv:{
        height:"1.5rem",
        width:"1.5rem"
    },
    leftButton:{
        display:"flex",
        alignItems:"center",
        fontSize: "1.2em"
    },

    driveIconStyle:{
        height:"1.5rem",
        width:"1.5rem"
    },

    buttonStyle:{
        width:"4.188rem",
        height:"3.313rem",
        background: colors.openBotBlue,
        border:"0.5px solid black",
        color: colors.whiteFont,
        fontFamily: "Gilroy-Medium",
    },
    undoButtonStyle:{
        borderRadius: "0.375rem 0 0 0.375rem",
        border:"0.3px solid black"
    },

    redoStyle:{
        borderRadius:"0 0.375rem 0.375rem 0",
        width:"4.188rem",
        height:"3.313rem",
        background: "#0071c5",
        opacity: "0.7",
        border:"0.3px solid black"
    },
    commandSize:{
        width:"1.4rem",
        height:"1.2rem",
    },

    minusStyle:{
        borderRadius: "0.375rem 0 0 0.375rem",
    },

    plusStyle:{
        borderRadius:"0 0.375rem 0.375rem 0"
    },
    operationSize:{
        fontSize : "40px",
        fontWeight:"2em",
        color: colors.whiteFont
    },

    iconMargin: {
        margin: 10
    },

    driveStyle:{
        border:"none",
        background:"none"
    }


};