import {colors} from "../../utils/color";

export const NavbarStyle = {
    navbarDiv: {
        display: "flex",
        backgroundColor: colors.openBotBlue,
        justifyContent: "space-between",
        height: "5rem",
        alignItems: "center",
    },
    navbarTitleDiv:{
        marginLeft: "4.25rem",
        display:"flex",
        alignItems:"center"
    },

    navbarIconDiv:{
        display: "flex",
        alignItems: "center",
        marginRight: "4.25rem",
    },

    mainIcon:{
        height: "2.031rem",
        width:"2.031rem"
    },

    mainTitle:{
        fontSize: "1.25em",
        fontFamily: "Gilroy-Medium",
        color:colors.whiteFont
    },
    infoIcon:{
        height:"1.5rem",
        width: "1.5rem"
    },
    moonIcon:{
        height:"1.5rem",
        width: "1.5rem"
    },

    lineIcon:{
        width:"0.065em"
    },

    buttonIcon:{
        border: "0.00621em solid #FFFEFE",
        borderRadius:"0.5rem",
        fontSize: "0.993em",
        height:"2.063rem",
        width:"4.563rem",
        fontFamily: "Gilroy-Medium",
        color:colors.whiteFont,
        backgroundColor: colors.openBotBlue,
        cursor:"pointer"
    },

    iconMargin: {
        margin: 10
    },

};