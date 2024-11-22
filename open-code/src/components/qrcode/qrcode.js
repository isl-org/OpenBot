import {useContext, useEffect, useState} from "react";
import {QRCodeCanvas} from "qrcode.react";
import {colors} from "../../utils/color";
import icon from "../../assets/images/icon/OBplaygroundLogo.png"
import {qrStyles} from "./styles";
import {StoreContext} from "../../context/context";
import {useTheme} from "@mui/material";
import useMediaQuery from "@mui/material/useMediaQuery";

/**
 * functional component for qr code
 * @returns {JSX.Element}
 * @constructor
 */
const QrCode = () => {
    const [blockCode, setBlockCode] = useState(undefined);
    const {code, generate} = useContext(StoreContext);
    const themes = useTheme();
    const isMobile = useMediaQuery(themes.breakpoints.down('md'));

    useEffect(() => {
        const qrCodeEncoder = () => {
            setBlockCode(JSON.stringify(code));
        };
        qrCodeEncoder();
    }, [code, generate]);


    const qrcode = (
        <QRCodeCanvas
            id="qrCode"
            value={blockCode}
            size={isMobile ? 130 : 200}
            bgColor={colors.whiteFont}
            includeMargin={true}
            imageSettings={{src: icon}}
        />

    );
    return (
        <div className="qrcode__container">
            <div style={isMobile ? qrStyles.mobileMain : qrStyles.main}>{qrcode}</div>
        </div>
    );
};

export default QrCode;
