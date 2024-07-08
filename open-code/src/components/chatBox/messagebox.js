import React, {useEffect, useState, useContext, useRef} from 'react';
import styles from './messageBox.module.css';
import {ThemeContext} from "../../App";
import {ChatConstants, Themes} from "../../utils/constants";
import {colors as Colors} from "../../utils/color";
import ReactMarkdown from 'react-markdown';

const ChatBox = (props) => {
    const {conversation, handlePauseClick, setIsTyping, setLoader, loader, allChatMessages} = props;
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
                paused={conversation.paused}
                handlePauseClick={() => handlePauseClick(conversation.id)}
                setIsTyping={setIsTyping}
                allChatMessages={allChatMessages}
                id={conversation.id}
                setLoader={setLoader}
                loader={loader}
            />
        </div>
    );
};

const UserMessage = ({timestamp, message}) => (
    <div className={styles.userMessage} title={timestamp}>
        <div>{message}</div>
        <div className={styles.userTimestamp}>{timestamp}</div>
    </div>
);

const AssistantResponse = ({
                               timestamp,
                               message,
                               paused,
                               setIsTyping,
                               setLoader,
                               loader,
                               allChatMessages,
                               id
                           }) => {
    const [displayedMessage, setDisplayedMessage] = useState('');
    const theme = useContext(ThemeContext);
    const chatContainerRef = useRef(null);

    useEffect(() => {
        setLoader(message === '');

        if (message !== '') {
            let index = 0;
            const interval = setInterval(() => {
                if (paused) {
                    clearInterval(interval);
                } else if (index <= message.length) {
                    setDisplayedMessage(message.slice(0, index));
                    index++;
                }
                if (index > message.length) {
                    setIsTyping(false);
                    clearInterval(interval);
                }
            }, 10);
            return () => clearInterval(interval);
        }
    }, [message, paused]);

    useEffect(() => {
        if (chatContainerRef.current) {
            chatContainerRef.current.scrollTop = chatContainerRef.current.scrollHeight;
        }
    }, [displayedMessage]);

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
