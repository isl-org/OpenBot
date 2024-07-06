import React, {useEffect, useState, useContext, useRef} from 'react';
import styles from './messageBox.module.css';
import {ThemeContext} from "../../App";
import {ChatConstants, Themes} from "../../utils/constants";
import {colors as Colors} from "../../utils/color";
import ReactMarkdown from 'react-markdown';


/**
 * Main ChatBox component that displays user and assistant messages
 * @returns {React.JSX.Element}
 * @constructor
 * @param props
 */
const ChatBox = (props) => {
    const { conversation, handlePauseClick, setTyping, setLoader, loader, allChatMessages } = props;
    const theme = useContext(ThemeContext);
    return (
        <div className={styles.chatBubble} style={{
            color: theme.theme === Themes.dark ? Colors.whiteFont : "#FFFFFF"
        }}>
            {!conversation.userMessage &&
                <iframe height="250" style={{border: "none"}}
                        src={ChatConstants.videoURl}>
                </iframe>}
            {conversation.userMessage &&
                <UserMessage timestamp={conversation.userTimestamp} message={conversation.userMessage}/>}
            <AssistantResponse
                timestamp={conversation.AITimestamp}
                message={conversation.AIMessage}
                image={conversation.blockImage}
                paused={conversation.paused} // Pass paused state
                handlePauseClick={() => handlePauseClick(conversation.id)} // Pass handlePauseClick
                setTyping={setTyping}
                allChatMessages={allChatMessages}
                id={conversation.id}
                setLoader={setLoader}
                loader={loader}
            />
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
const UserMessage = ({timestamp, message}) => (
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
 * @param paused
 * @param setLoader
 * @param loader
 * @param allChatMessages
 * @param id
 * @param handlePauseClick
 * @param setTyping
 * @returns {React.JSX.Element}
 * @constructor
 */
const AssistantResponse = ({ timestamp, message, image, paused, setTyping, setLoader, loader, allChatMessages, id, handlePauseClick }) => {
    const [displayedMessage, setDisplayedMessage] = useState('');
    const theme = useContext(ThemeContext);
    const chatContainerRef = useRef(null); // Add a ref to the chat container

    useEffect(() => {
        setLoader(message === '');

        if (message !== '') {
            let index = 0;
            const interval = setInterval(() => {
                if (!paused && index <= message.length) {
                    setDisplayedMessage(message.slice(0, index));
                    index++;
                } else if (paused) {
                    clearInterval(interval); // Stop the typewriter when paused
                }
                if (index > message.length) {
                    setTyping(false);
                    clearInterval(interval); // Stop interval when message is fully displayed
                }
            }, 10);
            return () => clearInterval(interval);
        }
    }, [message, paused]);

    useEffect(() => {
        if (chatContainerRef.current) {
            chatContainerRef.current.scrollTop = chatContainerRef.current.scrollHeight; // Auto-scroll to bottom
        }
    }, [displayedMessage]); // Run this effect whenever displayedMessage changes
    return (
        <div className={styles.responseBox}
             title={timestamp}
             style={{
                 backgroundColor: theme.theme === Themes.dark ? Colors.blackPopupBackground : "#FFFFFF",
                 color: theme.theme === Themes.dark ? Colors.whiteFont : "#000000",
             }}>
            {loader && allChatMessages.length === id ? (
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


