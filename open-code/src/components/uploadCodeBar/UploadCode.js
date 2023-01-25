import uploadIcon from "../../assets/images/upload-cloud.png"
import {UploadBarStyle} from "./styles";
function UploadCode(){
    return(
        <div style={UploadBarStyle.barDiv}>
            <div style={UploadBarStyle.buttonDiv}>
                <button style={UploadBarStyle.uploadCodeButton}>
                    <span>Upload Code <img style={UploadBarStyle.Icondiv} src={uploadIcon}/></span>
                </button>
            </div>

        </div>
    );
}

export default UploadCode;