import React from 'react';
import styles from './box.module.css';

const ChatBox = (props) => {

    const {conversation} = props;

    let message = {}
    return (
        <div className={styles.chatBubble}>
            <div className={styles.userMessage}>
                <p>
                    <span>{conversation.timestamp}</span>
                    <span style={{color: "blue"}}>You: {conversation.userMessage}</span>
                </p>
            </div>
            <div className={styles.assistantResponse}>
                <p>
                    <span>{conversation.timestamp}</span>
                    <span style={{color: "green"}}>Assistant: {conversation.AIMessage}</span>
                </p>
            </div>
        </div>
    );
};

export default ChatBox;