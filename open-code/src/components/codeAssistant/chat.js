import React, {useState} from 'react';
import styles from "./chat.module.css";
import ChatBox from '../chatBox/messagebox';

const responses = {
    "hello": "Hello! How can I assist you today?",
    "hi": "Hi! What's on your mind?",
    "how are you": "I'm doing great, thanks! How about you?",

};

const Chat = () => {
    const [inputValue, setInputValue] = useState('');
    const [conversations, setConversations] = useState({
        userMessage: "Hello World!",
        AIMessage: "I am a message!",
        id: 0,
        timestamp: new Date().getTime(),
    });
    const [allChatMessages, setAllChatMessages] = useState([]);
    const handleSendClick = () => {
        const userInput = inputValue.toLowerCase();
        const responseText = responses[userInput] || "I didn't understand that. Can you please rephrase?";
        const timestamp = new Date().toLocaleTimeString();

        console.log("userInput:::", userInput)

        // setConversations((prevState) => ({
        //     ...prevState,
        //     userMessage: userInput,
        //     AIMessage: "Buffering..",
        //     id: allChatMessages.length + 1,
        //     timestamp: new Date().getTime()
        // }))

        console.log("conversation:::",conversations)

        setAllChatMessages((prevState) => ([...prevState, conversations]))

        console.log("all messages::", allChatMessages)
        setInputValue('');
    };

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