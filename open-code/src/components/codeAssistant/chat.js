import React, {useContext, useEffect, useState} from 'react';
import styles from "./chat.module.css";
import ChatBox from '../chatBox/messagebox';
import {getAIMessage} from "../../services/chatAssistant";
import {Images} from "../../utils/images.js";
import {Constants, Themes, Errors} from '../../utils/constants.js';
import {ThemeContext} from "../../App";
import {colors as Colors} from "../../utils/color";

const Chat = () => {
    const theme = useContext(ThemeContext)
    const [inputValue, setInputValue] = useState('');
    const [allChatMessages, setAllChatMessages] = useState([
        {
            userMessage: "",
            AIMessage: Constants.Message,
            id: 1,
            userTimestamp: "",
            AITimestamp: new Date().toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'}),
        },
    ]);

    const [currentMessage, setCurrentMessage] = useState({
        userMessage: "",
        AIMessage: "",
        id: 2,
        userTimestamp: new Date().toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'}),
        AITimestamp: "",
    });

    const timestamp = new Date().toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'});
    const handleSendClick = async () => {
        const userInput = inputValue.toLowerCase();
        setCurrentMessage((prevState) => ({
            ...prevState,
            userMessage: userInput,
            userTimestamp: timestamp,
            AIMessage: "",
            id: allChatMessages.length + 1,
            AITimestamp: "",
        }));

        getAIMessage(userInput).then((res) => {
            setCurrentMessage((prevState) => ({
                ...prevState,
                AIMessage: res,
                AITimestamp: timestamp
            }));
        }).catch((e) => {
            console.log(e);
            setCurrentMessage((prevState) => ({
                ...prevState,
                AIMessage: Errors.error6,
                AITimestamp: timestamp
            }));
        });
        setInputValue('');
    };

    const handleKeyPress = (event) => {
        if (event.key === 'Enter' && inputValue.trim() !== '') {
            handleSendClick();
        }
    };

    useEffect(() => {
        if (currentMessage.AIMessage) {
            setAllChatMessages((prevMessages) => {
                const updatedMessages = [...prevMessages];
                if (updatedMessages.length > 0) {
                    updatedMessages[updatedMessages.length - 1] = currentMessage;
                }
                console.log("updatedMessages:::", updatedMessages);
                return updatedMessages;
            });
        } else if (currentMessage.userMessage) {
            console.log("current message::", currentMessage);
            setAllChatMessages((prevMessages) => [...prevMessages, currentMessage]);
        }
    }, [currentMessage]);

    return (
        <div className={styles.chatMainContainer}
             style={{
                 backgroundColor: theme.theme === Themes.dark ? Colors.blackBackground : "#d0e4f2",
                 color: theme.theme === Themes.dark ? Colors.whiteFont : "#000000"
             }}
        >
            <div className={styles.chatHeader}>
                <img src={Images.aiSupport} alt="Chat Assistant Logo" style={{width: 40, height: 40}}/>
                <h1>{Constants.Playground}</h1>
            </div>
            <div style={{height: "100%", overflow: "auto"}}>
                {allChatMessages.map((conversation, index) => (
                    <ChatBox key={index} conversation={conversation}/>
                ))}
            </div>
            <ChatBottomBar
                inputValue={inputValue}
                handleSendClick={handleSendClick}
                handleKeyPress={handleKeyPress}
                setInputValue={setInputValue}
            />
        </div>
    );
};
/**
 * Component for the user input box
 * @param props
 * @returns {Element}
 * @constructor
 */
const ChatBottomBar = (props) => {
    const theme = useContext(ThemeContext);
   const {inputValue, handleSendClick, handleKeyPress, setInputValue}=props;
    return (
        <div className={styles.chatBottomBar}>
            <input
                style={{
                    backgroundColor: theme.theme === Themes.dark ? Colors.blackPopupBackground : "#FFFFFF",
                    color: theme.theme === Themes.dark ? Colors.whiteFont : "#000000"
                }}
                type="text"
                placeholder="Enter a prompt here..."
                className={styles.inputField}
                value={inputValue}
                onChange={(e) => setInputValue(e.target.value)}
                onKeyPress={handleKeyPress}
            />
            <div
                onClick={handleSendClick}
                className={`sendButton ${inputValue.trim() === '' ? 'disabled' : ''}`}
                style={inputValue.trim() === '' ? { cursor: 'not-allowed', opacity: 0.5 } : {}}
            >
                <img alt="Send Icon" src={Images.sendIcon} className={styles.sendIcon} />
                <i className="fas fa-paper-plane" aria-hidden="true"></i>
            </div>
        </div>
    );
};

export default Chat;