
import {useContext, useState,useEffect} from "react";
import { QRCodeCanvas } from "qrcode.react";
import {colors} from "../../utils/color";
import icon from "../../assets/images/icon/open-bot-logo.png"
import {qrStyles} from "./styles";
import {StoreContext} from "../../context/context";
const QrCode = () => {
    const [blockCode, setBlockCode] = useState("");
    const {code } = useContext(StoreContext);
    const {generate} = useContext(StoreContext);

    useEffect(() => {
        const qrCodeEncoder = () => {
            setBlockCode(code);
        };
            qrCodeEncoder();

    }, [code,generate]);



    const qrcode = (
        <QRCodeCanvas
            id="qrCode"
            value={blockCode}
            size={200}
            bgColor={colors.whiteFont}
            includeMargin={true}
            imageSettings={{src: icon}}
        />
    );
    return (
        <div className="qrcode__container">
            <div style={qrStyles.main}>{qrcode}</div>
        </div>
    );
};

export default QrCode;
