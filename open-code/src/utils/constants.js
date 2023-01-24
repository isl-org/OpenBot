import {Scroll} from "../components/scrolls/scroll";
import blocklyCode from "../assets/images/screenshot.png";
import qrImage from "../assets/images/qrImage.png";
import vehicleImage from "../assets/images/vehiceImage.png";
import User from "../assets/Profile/user.png";
import Moon from "../assets/Profile/moon.png";
import Help from "../assets/Profile/help-circle.png";
import Logout from "../assets/Profile/log-out.png";

export const screens = [
    {
        key: 0, component: <Scroll heading="Drag and Drop" detail="Select the block of code required and drag and drop it to the input field .
Repeat the process of dragging and dropping all necessary code blocks, join them together to form a stack of actions."
                                   img={blocklyCode} width={18.8} height={22}/>
    },
    {
        key: 1, component: <Scroll heading="Save and Download" detail="Check for errors by compiling the code and, upon successful compilation, generate the QR code.
Use the OpenBot android application to scan the QR code and successfully save the block code on your device."
                                   img={qrImage} width={33} height={26}/>
    },
    {
        key: 2, component: <Scroll heading="Connect and Drive" detail="Pair your smartphone with the OpenBot car and run the code that you have downloaded.
Carry out desired actions such as activating indicator lights, detecting objects, and more on your robot car."
                                   img={vehicleImage} width={40} height={26}/>
    },
];


export const Content = [
    {
        Icon: User,
        title: "My Profile",
        selected:true,
    },
    {
        Icon: Moon,
        title: "Change Theme",
        selected:false,
    },
    {
        Icon: Help,
        title: "How To Upload",
        selected:false,
    },
    {
        Icon: Logout,
        title: "Logout",
        selected:false,
    },

]
