
import {useContext, useState,useEffect} from "react";
import { QRCodeCanvas } from "qrcode.react";
import {colors} from "../../utils/color";
import icon from "../../assets/images/ICON.png"
import {qrStyles} from "./styles";
import {StoreContext} from "../../context/Context";
const QrCode = () => {
    const [blockCode, setBlockCode] = useState("");
    const {code,setCode } = useContext(StoreContext);
    const {generate,setGenerateCode} = useContext(StoreContext);
    useEffect(() => {

            qrCodeEncoder();

    }, [generate]);

    const qrCodeEncoder = () => {
        setBlockCode(code);
    };

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
