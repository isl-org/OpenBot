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
        marginLeft: "4rem",
        display:"flex",
        alignItems:"center"
    },

    navbarIconDiv:{
        display: "flex",
        alignItems: "center",
        marginRight: "4rem",
    },

    mainIcon:{
        height: "2rem",
        width:"2rem",
        cursor: "pointer"
    },

    mainTitle:{
        fontSize: "1.3rem",
        fontFamily: "Gilroy-Medium",
        color:colors.whiteFont
    },

    infoIcon:{
        height:"1.5rem",
        width: "1.5rem",
        cursor: "pointer"
    },

    moonIcon:{
        height:"1.3rem",
        width: "1.3rem",
        cursor: "pointer"
    },

    lineIcon:{
        width:"0.5%"
    },

    buttonIcon:{
        border: "1px solid #FFFEFE",
        borderRadius:"0.5rem",
        fontSize: "1rem",
        height:"2rem",
        width:"5rem",
        fontFamily: "Gilroy-Medium",
        color:colors.whiteFont,
        backgroundColor: colors.openBotBlue,
        cursor:"pointer"
    },

    iconMargin: {
        margin: 10
    },
    arrowMargin:{
        margin: 2
    },
    playgroundName: {
        display: "flex",
        alignItems: "center",
        cursor:"pointer"
    },


};
