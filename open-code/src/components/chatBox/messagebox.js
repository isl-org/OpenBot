import React from 'react';
import styles from './messageBox.module.css';

/**
 * Main ChatBox component that displays user and assistant messages
 * @param conversation
 * @returns {React.JSX.Element}
 * @constructor
 */
const ChatBox = ({conversation}) => (
    <div className={styles.chatBubble}>
        <UserMessage timestamp={conversation.userTimestamp} message={conversation.userMessage}
        />
        <AssistantResponse timestamp={conversation.AITimestamp} message={conversation.AIMessage}
        />
    </div>
);
/**
 * UserMessage component renders a user's message with a timestamp.
 * @param timestamp
 * @param message
 * @returns {React.JSX.Element}
 * @constructor
 */
const UserMessage = ({timestamp, message}) => (
    <div className={styles.userMessage} title={timestamp}>
        <p>
            <span className={styles.userName}></span>
            {message}
        </p>
    </div>
);
/**
 *
 * Component to display assistant's response with timestamp
 * @returns {Element}
 * @constructor
 * @param props
 */
const AssistantResponse = (props) => {
    const {timestamp, message, conversation} = props;
    const parsedMessage = parseResponseMessage(message);

    return (
        <div className={styles.responseBox} title={timestamp}>
            <div className={styles.responseContent}>
                {parsedMessage}
            </div>
        </div>
    );
};
/**
 *
 * Parses a response message string and converts it into an array of JSX elements.
 * Handles different formatting rules such as headers, list items, code blocks, and bold text.
 * @param message
 * @returns {*[]}
 */
const parseResponseMessage = (message) => {
    const lines = message.split('\n');
    const parsedLines = [];

    lines.forEach((line, index) => {
        const leadingWhitespace = line.match(/^(\s+)/);
        const indent = leadingWhitespace ? leadingWhitespace[1].length : 0;

        if (line.startsWith('###')) {
            parsedLines.push(
                <h3 key={index} style={{paddingLeft: `${indent * 16}px`}}>
                    {line.replace('###', '')}
                </h3>
            );
        } else if (line.startsWith('-')) {
            parsedLines.push(
                <li key={index} style={{paddingLeft: `${indent * 16}px`}}>
                    {line.replace('-', '')}
                </li>
            );
        } else if (line.includes('```')) {
            const code = line.replace('```', '');
            parsedLines.push(
                <code key={index} style={{paddingLeft: `${indent * 16}px`}}>
                    {code}
                </code>
            );
        } else if (line.includes('**') || line.includes('***')) {
            parsedLines.push(
                <p
                    key={index}
                    style={{
                        fontFamily: 'Gilroy-Bold',
                        // fontWeight: 'bold',
                        paddingLeft: `${indent * 16}px`,
                    }}
                >
                    {line.replace(/(?:\*\*|\*)/g, '')}
                </p>
            );
        } else {
            parsedLines.push(
                <p key={index} style={{paddingLeft: `${indent * 16}px`}}>
                    {line}
                </p>
            );
        }
    });

    return parsedLines;
};


export default ChatBox;