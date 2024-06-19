import React from 'react';
import styles from "./chat.module.css";

const Chat = (params) => {
    return (
        <div className={styles.chatMainContainer}>
            <div title={"chat-header"} className={styles.chatHeader}>
                <h1>Chat Assistant</h1>
            </div>
            <div style={{backgroundColor:"tomato" , height:"100%"}} title={"chat-box"}>sxasCSC</div>
            <div title={"chat-bottom-bar"} className={styles.chatBottomBar}>
                <input type="text" placeholder="Type a message..." className={styles.inputField} />
                <button className={styles.sendButton}>
                    <i className="fas fa-paper-plane" aria-hidden="true"></i>
                    Send
                </button>
            </div>
        </div>
    );
};

export default Chat;