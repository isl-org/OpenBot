import {colors} from "../../utils/color";

export const qrStyles = {
    main: {
        marginTop: '100px',
        marginLeft: 50
    },
    mobileMain: {
        marginTop: '50px',
        marginLeft: 70,
        marginBottom: 20,
    },
    heading: {
        fontFamily: "Gilroy-Medium",
        fontSize: "1.15em",
        marginLeft: '2rem',
        marginTop: "3rem"
    },
    mobileHeading:{
        fontFamily: "Gilroy-Medium",
        fontSize:14,
        marginLeft:'1rem',
        marginTop:'2rem'
    },
    list: {
        display: 'flex',
        fontFamily: 'Gilroy-Medium',
        fontSize: "0.8em",
        marginTop: "1.5rem",
        marginLeft: '2rem',
        flexDirection: 'row',
    },
    mobileList:{
        display: 'flex',
        fontFamily: 'Gilroy-Regular',
        fontSize: 10,
        marginTop: "1rem",
        marginLeft: '1.5rem',
    },

    rightSlider: {
        width: '22px',
        height: '149px',
        backgroundColor: 'rgba(76, 156, 214, 0.15)',
        border: '1px solid #4C9CD626',
        borderRadius: ' 0 15px 15px 0',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
    },
    leftSlider:{
        width: '22px',
        height: '149px',
        backgroundColor: 'rgba(76, 156, 214, 0.15)',
        border: '1px solid #4C9CD626',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        borderRadius: ' 15px 0 0 15px',
    },
    rightSliderIcon: {
        height: '1rem',
        width: '1rem'
    },
    leftSliderIcon:{
        height: '1rem',
        width: '1rem',

    },
    drawerLight: {
        color: colors.blackFont,
    },
    drawerDark: {
        color: colors.whiteFont,
    }


}
