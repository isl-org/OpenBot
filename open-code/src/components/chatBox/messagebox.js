import React, { useEffect, useState, useContext } from 'react';
import styles from './messageBox.module.css';
import { ThemeContext } from "../../App";
import { Themes } from "../../utils/constants";
import { colors as Colors } from "../../utils/color";
import { Images } from "../../utils/images";
import ReactMarkdown from 'react-markdown';

/**
 * Main ChatBox component that displays user and assistant messages
 * @param conversation
 * @returns {React.JSX.Element}
 * @constructor
 */
const ChatBox = ({ conversation }) => {
    const theme = useContext(ThemeContext);
    return (
        <div className={styles.chatBubble} style={{
            color: theme.theme === Themes.dark ? Colors.whiteFont : "#FFFFFF"
        }}>
            {!conversation.userMessage && <img src={Images.openBotLogo} width={"20%"} alt={"openBot"} />}
            {conversation.userMessage &&
                <UserMessage timestamp={conversation.userTimestamp} message={conversation.userMessage} />}
            <AssistantResponse timestamp={conversation.AITimestamp} message={conversation.AIMessage}
                               image={conversation.blockImage} />
        </div>
    );
};

/**
 * UserMessage component renders a user's message with a timestamp.
 * @param timestamp
 * @param message
 * @returns {React.JSX.Element}
 * @constructor
 */
const UserMessage = ({ timestamp, message }) => (
    <div className={styles.userMessage} title={timestamp}>
        <div>{message}</div>
        <div className={styles.userTimestamp}>{timestamp}</div>
    </div>
);

/**
 * Component to display assistant's response with typewriter effect and timestamp
 * @param timestamp
 * @param message
 * @param image
 * @returns {React.JSX.Element}
 * @constructor
 */
const AssistantResponse = ({ timestamp, message, image }) => {
    const [displayedMessage, setDisplayedMessage] = useState('');
    const [loader, setLoader] = useState(false);
    const theme = useContext(ThemeContext);

    useEffect(() => {
        setLoader(message === '');

        if (message !== '') {
            let index = 0;
            const interval = setInterval(() => {
                if (index <= message.length) {
                    setDisplayedMessage(message.slice(0, index));
                    index++;
                } else {
                    clearInterval(interval);
                }
            }, 50); // Adjust typing speed as needed
            return () => clearInterval(interval);
        }
    }, [message]);

    return (
        <div className={styles.responseBox}
             title={timestamp}
             style={{
                 backgroundColor: theme.theme === Themes.dark ? Colors.blackPopupBackground : "#FFFFFF",
                 color: theme.theme === Themes.dark ? Colors.whiteFont : "#000000",
             }}>
            {loader ? (
                <div className={`${styles.loader} ${theme.theme === Themes.dark ? styles.whiteLoader : styles.loader}`}>
                </div>
            ) : (
                <div className={styles.responseContent}
                     style={{
                         backgroundColor: theme.theme === Themes.dark ? Colors.blackPopupBackground : "#FFFFFF",
                         color: theme.theme === Themes.dark ? Colors.whiteFont : "#000000",
                     }}>
                    <ReactMarkdown>{displayedMessage}</ReactMarkdown>
                    <div className={styles.timestamp}>{timestamp}</div>
                </div>
            )}
        </div>
    );
};

export default ChatBox;
