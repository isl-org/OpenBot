import React, {useEffect, useState} from 'react';
import styles from "./chat.module.css";
import ChatBox from '../chatBox/messagebox';
import {getAIMessage} from "../../services/chatAssistant";
import {Images} from "../../utils/images.js";
import {Constants} from '../../utils/constants.js';


const Chat = () => {
    const [inputValue, setInputValue] = useState('');
    const [allChatMessages, setAllChatMessages] = useState([]);
    const [currentMessage, setCurrentMessage] = useState({
        userMessage: "",
        AIMessage: "",
        id: 1,
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
                AIMessage: "Error occurred",
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
        <div className={styles.chatMainContainer}>
            <div className={styles.chatHeader}>
                <img src={Images.AISupport} alt="Chat Assistant Logo" style={{width: 40, height: 40}}/>
                <h1>{Constants.Playground}</h1>
            </div>
            <div style={{height: "100%", overflow: "auto"}}>
                {allChatMessages.map((conversation, index) => (
                    <ChatBox key={index} conversation={conversation}/>
                ))}
            </div>
            <div className={styles.chatBottomBar}>
                <input
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
                    style={inputValue.trim() === '' ? {cursor: 'not-allowed', opacity: 0.5} : {}}
                >
                    <img alt="Send Icon" src={Images.sendIcon} className={styles.sendIcon}/>
                    <i className="fas fa-paper-plane" aria-hidden="true"></i>
                </div>
            </div>
        </div>
    );
};

export default Chat;
