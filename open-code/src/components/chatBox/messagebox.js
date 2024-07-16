import React, { useEffect, useState, useContext } from 'react';
import styles from './messageBox.module.css';
import mstyles from './markDown.module.css';
import { ThemeContext } from '../../App';
import { ChatConstants, Themes } from '../../utils/constants';
import { colors as Colors } from '../../utils/color';
import ReactMarkdown from 'react-markdown';
import remarkGfm from 'remark-gfm';

/**
 * ChatBox component renders a chat bubble containing user and assistant messages.
 * @param props
 * @returns {Element}
 * @constructor
 */
const ChatBox = (props) => {
    const {
        conversation, handlePauseClick, setIsTyping, setLoader, loader, allChatMessages, chatContainerRef,
    } = props;
    const theme = useContext(ThemeContext);
    return (
        <div
            className={styles.chatBubble}
            style={{
                color: theme.theme === Themes.dark? Colors.whiteFont : '#FFFFFF',
            }}
        >
            {!conversation.userMessage && (
                <iframe
                    height='250'
                    style={{ border: 'none' }}
                    src={ChatConstants.videoURl}
                ></iframe>
            )}
            {conversation.userMessage && (
                <UserMessage
                    timestamp={conversation.userTimestamp}
                    message={conversation.userMessage}
                />
            )}
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
                chatContainerRef={chatContainerRef}
            />
        </div>
    );
};

/**
 * UserMessage component renders a user message with timestamp.
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
 * AssistantResponse component renders assistant's response with typewriter effect.
 * Manages display of response content, loader, and scrolling behavior.
 * @param timestamp
 * @param message
 * @param paused
 * @param setIsTyping
 * @param setLoader
 * @param loader
 * @param allChatMessages
 * @param id
 * @param chatContainerRef
 * @returns {Element}
 * @constructor
 */
const AssistantResponse = ({
                               timestamp,
                               message,
                               paused,
                               setIsTyping,
                               setLoader,
                               loader,
                               allChatMessages,
                               id,
                               chatContainerRef,
                           }) => {
    const [displayedMessage, setDisplayedMessage] = useState('');
    const [showXml, setShowXml] = useState(false);
    const theme = useContext(ThemeContext);

    useEffect(() => {
        setLoader(message === '');

        if (message!== '') {
            let index = 0;

            const regex = /<xml xmlns="https:\/\/developers.google.com\/blockly\/xml">[\s\S]*?<\/xml>/;
            const cleanMessage = message.replace(regex, '');
            if (allChatMessages.length === id) {
                const interval = setInterval(() => {
                    if (paused) {
                        clearInterval(interval);
                    } else if (index <= cleanMessage.length) {
                        setDisplayedMessage(cleanMessage.slice(0, index));
                        index++;
                    }
                    if (index > cleanMessage.length) {
                        setIsTyping(false);
                        clearInterval(interval);
                    }
                }, 10);
                return () => clearInterval(interval);
            }
        }
    }, [message, paused]);

    useEffect(() => {
        if (chatContainerRef.current) {
            if (chatContainerRef.current.scrollHeight!== null) {
                chatContainerRef.current.scrollTop = chatContainerRef.current.scrollHeight;
            }
        }
    }, [displayedMessage, chatContainerRef]);
    return (
        <div
            className={styles.responseBox}
            title={timestamp}
            style={{
                backgroundColor: theme.theme === Themes.dark? Colors.blackPopupBackground : '#FFFFFF',
                color: theme.theme === Themes.dark? Colors.whiteFont : '#000000',
            }}
        >
            {loader && allChatMessages.length === id? (
                <div
                    className={`${styles.loader} ${theme.theme === Themes.dark? styles.whiteLoader : styles.loader}`}
                ></div>
            ) : (
                <div
                    className={styles.responseContent}
                    style={{
                        backgroundColor: theme.theme === Themes.dark? Colors.blackPopupBackground : '#FFFFFF',
                        color: theme.theme === Themes.dark? Colors.whiteFont : '#000000',
                    }}
                >
                    <ReactMarkdown
                        remarkPlugins={[remarkGfm]}
                        components={{
                            h1: ({node,...props}) => <h1
                                className={`${mstyles.heading1} ${theme.theme === Themes.dark? mstyles.darkTheme : ''}`} {...props} />,
                            h2: ({node,...props}) => <h2
                                className={`${mstyles.heading2} ${theme.theme === Themes.dark? mstyles.darkTheme : ''}`} {...props} />,
                            h3: ({node,...props}) => <h3
                                className={`${mstyles.heading3} ${theme.theme === Themes.dark? mstyles.darkTheme : ''}`} {...props} />,
                            p: ({node,...props}) => <p
                                className={`${mstyles.paragraph} ${theme.theme === Themes.dark? mstyles.darkTheme : ''}`} {...props} />,
                            strong: ({node,...props}) => <strong
                                className={`${mstyles.strong} ${theme.theme === Themes.dark? mstyles.darkTheme : ''}`} {...props} />,
                            em: ({node,...props}) => <em
                                className={`${mstyles.em} ${theme.theme === Themes.dark? mstyles.darkTheme : ''}`} {...props} />,
                            ul: ({node,...props}) => <ul
                                className={`${mstyles.list} ${theme.theme === Themes.dark? mstyles.darkTheme : ''}`} {...props} />,
                            ol: ({node,...props}) => <ol
                                className={`${mstyles.orderedList} ${theme.theme === Themes.dark? mstyles.darkTheme : ''}`} {...props} />,
                            li: ({node,...props}) => <li
                                className={`${mstyles.listItem} ${theme.theme === Themes.dark? mstyles.darkTheme : ''}`} {...props} />,
                            blockquote: ({node,...props}) => <blockquote
                                className={`${mstyles.blockquote} ${theme.theme === Themes.dark? mstyles.darkTheme : ''}`} {...props} />,
                            code: ({node,...props}) => <code
                                className={`${mstyles.code} ${theme.theme === Themes.dark? mstyles.darkTheme : ''}`} {...props} />,
                        }}
                    >
                        {displayedMessage}
                    </ReactMarkdown>
                    <div className={styles.timestamp}>{timestamp}</div>
                </div>
            )}
        </div>
    );
};

export default ChatBox;