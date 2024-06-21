import React from 'react';
import styles from './box.module.css';

const UserMessage = ({ timestamp, message }) => (
    <div className={styles.userMessage}>
        <p>
            <span>{timestamp}</span>
            <span className={styles.userName}>You: {message}</span>
        </p>
    </div>
);

const AssistantResponse = ({ timestamp, message }) => (
    <div className={styles.assistantResponse}>
        <p>
            <span>{timestamp}</span>
            <span className={styles.assistantName}>Assistant: {message}</span>
        </p>
    </div>
);

const ChatBox = ({ conversation }) => (
    <div className={styles.chatBubble}>
        <UserMessage timestamp={conversation.userTimestamp} message={conversation.userMessage} />
        <AssistantResponse timestamp={conversation.AITimestamp} message={conversation.AIMessage} />
    </div>
);

export default ChatBox;