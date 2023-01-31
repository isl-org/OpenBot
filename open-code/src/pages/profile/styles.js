export const ProfileStyles = {
    Main: {
        display: "flex",
        width: "100%",
    }
};


export const DeleteStyles = {
    model: {
        width: "464px",
        height: "246px",
        position: 'absolute',
        top: '50%',
        left: '50%',
        transform: 'translate(-50%, -50%)',
        background: '#FFFFFF',
        boxShadow: "0px 4px 15px rgba(0, 0, 0, 0.25)",
        borderRadius: "6px",
        outline : 'none'
    },
    ModelHeading: {
        fontFamily: 'Gilroy-Regular',
        fontSize: "23px",
        lineHeight: "27px",
        color: "#000000",
        paddingTop: "31px",
        paddingLeft: "35px",
    },
    Input: {
        fontFamily: 'Gilroy-Regular',
        fontSize: "18px",
        opacity: "0.5",
        lineHeight: "21px",
        color: "#000000",
        paddingTop: "31px",
        paddingLeft: "35px",
    },
    btnGroup: {
        display: "flex",
        justifyContent: "end",
        alignItems: "center",
        gap: "16px",
        marginTop: ' 67px',
        paddingRight: "40px",
    },
    CancelBtn: {
        width: "133px",
        height: "46px",
        borderRadius: 7.7,
        fontFamily: "Gilroy-SemiBold",
        color: "#0071c5",
        fontSize: "22px",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        textDecoration: "none",
        cursor: "pointer",
        border: "1.5px solid #0071c5",
    },
    DeleteBtn: {
        width: "136px",
        height: "49px",
        borderRadius: 7.7,
        fontFamily: "Gilroy-SemiBold",
        color: "#ffffff",
        fontSize: "22px",
        background: "red",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        textDecoration: "none",
        cursor: "pointer",

    },
}
