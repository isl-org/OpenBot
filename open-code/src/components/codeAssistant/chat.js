import React, {useEffect, useState} from 'react';
import styles from "./chat.module.css";
import ChatBox from '../chatBox/messagebox';
import {getAIMessage} from "../../services/chatAssistant";

const responses = {
    "hello": "Hello! How can I assist you today?",
    "hi": "Hi! What's on your mind?",
    "how are you": "I'm doing great, thanks! How about you?",
};

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

    const handleSendClick = async () => {
        const userInput = inputValue.toLowerCase();
        setCurrentMessage((prevState) => ({
            ...prevState,
            userMessage: userInput,
            AIMessage: "",
            id: allChatMessages.length + 1,
            AITimestamp: new Date().toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'}),
        }));

        getAIMessage(userInput).then((res) => {
            setCurrentMessage((prevState) => ({
                ...prevState,
                AIMessage: res,
                AITimestamp: new Date().toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'})
            }));
        }).catch((e)=>{
            console.log(e);
            setCurrentMessage((prevState) => ({
                ...prevState,
                AIMessage: "Error occuted",
                AITimestamp: new Date().toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'})
            }));
        })

        // Clear the input field
        setInputValue('');
    };

    const handleKeyPress = (event) => {
        if (event.key === 'Enter') {
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

                // Return the new state
                return updatedMessages;
            });
        } else if (currentMessage.userMessage)
            setAllChatMessages((prevMessages) => [...prevMessages, currentMessage])
    }, [currentMessage])

    console.log(allChatMessages)
    return (
        <div className={styles.chatMainContainer}>
            <div title={"chat-header"} className={styles.chatHeader}>
                <h1>Chat Assistant</h1>
            </div>
            <div style={{height: "100%", overflow: "auto"}} title={"chat-box"}>
                {allChatMessages.map((conversation, index) => (
                    <ChatBox key={index} conversation={conversation}/>
                ))}
            </div>
            <div title={"chat-bottom-bar"} className={styles.chatBottomBar}>
                <input
                    type="text"
                    placeholder="Type a message..."
                    className={styles.inputField}
                    value={inputValue}
                    onChange={(e) => setInputValue(e.target.value)}
                    onKeyPress={handleKeyPress}
                />
                <button className={styles.sendButton} onClick={handleSendClick}>
                    <i className="fas fa-paper-plane" aria-hidden="true"></i>
                    Send
                </button>
            </div>
        </div>
    );
};

export default Chat;