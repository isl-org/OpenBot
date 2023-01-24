import {makeStyles} from "@material-ui/core";
import {colors} from "../../utils/color";

export const FooterStyle = makeStyles(() => ({
        footerDiv: {
            display:"flex",
            backgroundColor: "#e8f4f8",
            justifyContent: "space-between",
            height:"8rem",
            alignItems: "center",
        },

        footerTextDiv:{
            marginLeft:"4.25rem",
        },

        footerIconDiv:{
            display: "flex",
            alignItems: "center",
            marginRight:"4.25rem",
        },

        youtubePic:{
            height:"2.5rem",
            width:"2.5rem"
        },

        webPic:{
            height: "2.5rem",
            width: "2.5rem"
        },

        playStorePic:{
            height:"3rem",
            width:"8rem",
        },
        appStorePic:{
            height:"3rem",
            width:"8rem"
        },

        iconMargin:{
          margin: 10
        },

        textFont:{
            fontSize:"1.1em",
            fontFamily:"Gilroy-Bold"
        },

    }
))