import {makeStyles} from "@material-ui/core";

export const LeftSectionStyles = makeStyles((theme) => ({
    Main: {
        width: "21.4%",
        paddingTop: "61px",
        borderWidth: "1px",
        borderColor: "#CCCCCC",
        borderStyle: "none solid none none",
    },
    IconContent: {
        display: "flex",
        flexDirection: "column",
        width: "100%",
        // background: "#BEE3FF",
        // opacity: "0.34",
    },
    Items: {
        display: "flex",
        height: "43px",
        gap: "0px 15px",
        paddingLeft: "66px",
        alignItems: "center",
        justifyContent: "flex-start",
        cursor:"pointer",
    },
    ItemsSelected: {
        display: "flex",
        height: "43px",
        gap: "0px 15px",
        paddingLeft: "66px",
        alignItems: "center",
        justifyContent: "flex-start",
        background: "#e8f6fe",
        borderWidth: "4.38px",
        borderColor: "#0071C5",
        borderStyle: "none none none solid",
        cursor:"pointer",
    },

    Icon: {
        width: "24px",
        height: "24px",
    },
    Content: {
        fontFamily: 'Gilroy-Light',
        fontWeight: "400",
        fontSize: "20px",
        lineHeight: "23px",
        letterSpacing: "-0.01em",
        color: "#0071C5",
    }
}));

export const RightSectionStyles = makeStyles((theme) => ({
    Main: {
        width: "78.6%",
        paddingLeft: "90px",
        paddingBottom: "104px",
        paddingTop: "69px",
    },
    Header: {
        fontFamily: 'Gilroy-Medium',
        fontWeight: 600,
        fontSize: "20px",
        lineHeight: "24px",
        color: "#000000",
        marginBottom: "25px",
    },
    ProfileImage: {
        marginBottom: "60px",
    },
    Image: {
        width: "120px",
        height: "120px",
        borderRadius: "50%",
    },
    EditIcon: {
        width: "40px",
        height: "40px",
        position: "relative",
        bottom: "2px",
        right: "43px",
        cursor:"pointer",
    },
    Edit: {
        width: "682px",
        display: "flex",
        flexWrap: "wrap",
        gap: "38px 60px",
        marginBottom: "60px",
    },
    Input: {
        display: "flex",
        flexDirection: "column"
    },
    lable: {
        fontFamily: 'Gilroy-Regular',
        fontSize: "18px",
        lineHeight: "27px",
        color: "#000000",
        marginBottom: "7px",
    },
    InputArea: {
        height: "28px",
        width: "269px",
        fontFamily: 'Gilroy-Light',
        padding: "10px 20px",
        fontSize: "20px",
        lineHeight: "27px",
        borderWidth: "1px",
        borderColor: "#0071C5",
        backgroundColor: "#FFFFFF",
        color: "#000000",
        borderStyle: "solid",
        borderRadius: "6px",
    },
    InputEmail: {
        width: "100%",
    },
    lableEmail: {
        fontFamily: 'Gilroy-Regular',
        fontSize: "18px",
        lineHeight: "27px",
        color: "#CCCCCC",
        marginBottom: "7px",
    },
    InputAreaEmail: {
        height: "28px",
        width: "100%",
        fontFamily: 'Gilroy-Light',
        padding: "10px 20px",
        fontSize: "20px",
        lineHeight: "27px",
        borderWidth: "1px",
        borderColor: "#CCCCCC",
        backgroundColor: "#FFFFFF",
        color: "#CCCCCC",
        borderStyle: "solid",
        borderRadius: "6px",
        marginTop: "7px",
        cursor:"no-drop",
    },
    btn: {
        display: "flex",
        gap: "0px 25px",
    },
    Savebtn: {
        width: "51px",
        height: "26px",
        borderRadius: 6,
        fontFamily: "Gilroy-Medium",
        color: "#ffffff",
        fontSize: "22px",
        background: "#0071c5",
        padding: "13px 58px 14px 58px",
        textDecoration: "none",
        cursor:"pointer",
    },
    Canclebtn: {
        width: "71px",
        height: "24px",
        border: "1px solid #0071C5",
        borderRadius: 6,
        fontFamily: "Gilroy-Medium",
        color: "#0071c5",
        fontSize: "22px",
        background: "#ffffff",
        padding: "13px 47px 14px 47px",
        textDecoration: "none",
        cursor:"pointer",
    }

}));