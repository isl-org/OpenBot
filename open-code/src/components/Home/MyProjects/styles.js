import {makeStyles} from "@material-ui/core";

export const NewProjectStyles = makeStyles((theme) => ({
    Main: {
        minHeight: "450px",
        backgroundColor: "#F8F9FB",
        display: "flex",
        flexDirection: "column",
        alignItems: "baseline",
        paddingLeft: "102px",
        paddingTop: "81px",
        paddingBottom:"62px",
    },
    Heading: {
        fontSize: "25px",
        fontFamily: "Gilroy",
        fontWeight: 600,
        lineHeight: "30px",
    },
    ButtonsMessage:{
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
    },
    Content: {
        marginTop: "22px",
        display: "flex",
        alignItems: "center",
    },
    Button: {
        width: "258px",
        height: "162px",
        backgroundColor: "#ffffff",
        border: "1.3px dashed #0071C5",
        borderRadius: "12px",
        boxShadow: "0px 2px 7px rgba(0, 0, 0, 0.13)",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
    },
    AddIconImage: {
        width: "57px",
        height: "57px",
        border: "1.5px solid #0071C5",
        borderRadius: "50%",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
    },
    Message: {
        width: "294px",
        height: "70px",
        background: " #FFFFFF",
        borderRadius: "4px",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        fontFamily: 'Gilroy-Medium',
        fontSize: "18px",
        lineHeight: "21px",
        marginLeft: "-1px",
        color: "#292929",
    },
    MessageIcon:{
        display:"flex",
        alignItems: "center",
        justifyContent: "center",
    },
    TriangleIcon:{
        width:"19px",
        height:"19px"
    },
    plus: {
        width: "21px",
        height: "21px",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        fontSize: "25px",
        color: "#0071C5",
    }

}));

export const SavedProjectsStyles = makeStyles((theme) => ({
    Main: {
        minHeight: "450px",
        backgroundColor: "#F8F9FB",
        display: "flex",
        paddingBottom:"62px",
        flexDirection: "column",
        alignItems: "baseline",
        paddingLeft: "102px",
        paddingTop: "81px",
    },
    Heading: {
        fontSize: "25px",
        fontFamily: "Gilroy",
        fontWeight: 600,
        lineHeight: "30px",
    },
    Content: {
        marginTop: "22px",
        display: "flex",
        alignItems: "center",
    },
    CardMain:{
      display:"flex",
      gap:"0 42px",
      flexWrap:"wrap"
    },
    Card: {
        width: "260px",
        height: "162px",
        backgroundColor: "#ffffff",
        borderRadius: "12px",
        boxShadow: "0px 2px 7px rgba(0, 0, 0, 0.13)",
    },
    CardHeadingIcon: {
        display: "flex",
        flexDirection: "row",
    },
    CardHeading: {
        paddingLeft: "31px",
        paddingTop: "33px",
        fontFamily: 'Gilroy',
        fontWeight: "600",
        fontSize: "25px",
        lineHeight: "30px",
        color: "#000000",
    },
    Icon: {},
    Date: {
        paddingLeft: "31px",
        paddingTop: "5px",
        fontFamily: 'Gilroy',
        fontWeight: "400",
        fontSize: "15px",
        lineHeight: "17px",
        color: "#000000",
    },


}));
