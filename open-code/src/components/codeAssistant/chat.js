import React, { useEffect, useState } from 'react';
import styles from "./chat.module.css";
import ChatBox from '../chatBox/messagebox';
import { getAIMessage } from "../../services/chatAssistant";
import { Images } from "../../utils/images.js";

const Chat = () => {
    const [inputValue, setInputValue] = useState('');
    const [allChatMessages, setAllChatMessages] = useState([]);
    const [currentMessage, setCurrentMessage] = useState({
        userMessage: "",
        AIMessage: "",
        id: 1,
        userTimestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
        AITimestamp: "",
    });

    const handleSendClick = async () => {
        const userInput = inputValue.trim().toLowerCase();
        if (userInput === '') return;
        setCurrentMessage((prevState) => ({
            ...prevState,
            userMessage: userInput,
            AIMessage: "",
            id: allChatMessages.length + 1,
            AITimestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
        }));

        getAIMessage(userInput).then((res) => {
            setCurrentMessage((prevState) => ({
                ...prevState,
                AIMessage: res,
                AITimestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
            }));
        }).catch((e) => {
            console.log(e);
            setCurrentMessage((prevState) => ({
                ...prevState,
                AIMessage: "Error occurred",
                AITimestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
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

                return updatedMessages;
            });
        } else if (currentMessage.userMessage)
            setAllChatMessages((prevMessages) => [...prevMessages, currentMessage]);
    }, [currentMessage]);

    return (
        <div className={styles.chatMainContainer}>
            <div title="chat-header" className={styles.chatHeader}>
                <h1>Chat Assistant</h1>
            </div>
            <div style={{height: "100%", overflow: "auto"}} title="chat-box">
                {allChatMessages.map((conversation, index) => (
                    <ChatBox key={index} conversation={conversation}/>
                ))}
            </div>
            <div title="chat-bottom-bar" className={styles.chatBottomBar}>
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
