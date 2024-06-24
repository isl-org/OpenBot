import React from 'react';
import styles from './box.module.css';

/*
Main ChatBox component that displays user and assistant messages
 */
const ChatBox = ({conversation}) => (
    <div className={styles.chatBubble}>
        <UserMessage timestamp={conversation.userTimestamp} message={conversation.userMessage}/>
        <AssistantResponse timestamp={conversation.AITimestamp} message={conversation.AIMessage}/>
    </div>
);

/*
Main ChatBox component that displays user and assistant messages
 */
const UserMessage = ({timestamp, message}) => (
    <div className={styles.userMessage}>
        <p>
            <span>{timestamp}</span>
            <span className={styles.userName}>You: {message}</span>
        </p>
    </div>
);
/*
Component to display assistant's response with timestamp
 */

const AssistantResponse = ({timestamp, message}) => {
    const parsedMessage = parseResponseMessage(message);

    return (
        <div className={styles.responseBox}>
            <div className={styles.responseContent}>
                <p>
                    <span>{timestamp}</span>
                    <span className={styles.assistantName}>Assistant:</span>
                </p>
                {parsedMessage}
            </div>
        </div>
    );
};
const parseResponseMessage = (message) => {
    const lines = message.split('\n');
    const parsedLines = [];

    lines.forEach((line) => {
        if (line.startsWith('###')) {
            parsedLines.push(<h3 key={parsedLines.length}>{line.replace('###', '')}</h3>);
        } else if (line.startsWith('-')) {
            parsedLines.push(<li key={parsedLines.length}>{line.replace('-', '')}</li>);
        } else if (line.includes('```')) {
            const code = line.replace('```', '');
            parsedLines.push(<code key={parsedLines.length}>{code}</code>);
        } else if (line.includes('**') || line.includes('***')) {
            parsedLines.push(<p style={{fontWeight: "bold"}}
                                key={parsedLines.length}>{line.replace(/(?:\*\*|\*)/g, '')}</p>);
        } else {
            // Regular text
            parsedLines.push(<p key={parsedLines.length}>{line}</p>);
        }
    });

    return parsedLines;
};


export default ChatBox;