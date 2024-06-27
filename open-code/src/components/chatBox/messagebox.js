import React, {useRef, useEffect, useState, useContext} from 'react';
import styles from './messageBox.module.css';
import {ThemeContext} from "../../App";
import {Themes} from "../../utils/constants";
import {colors as Colors} from "../../utils/color";
import {Images} from "../../utils/images";

/**
 * Main ChatBox component that displays user and assistant messages
 * @param conversation
 * @returns {React.JSX.Element}
 * @constructor
 */
const ChatBox = ({conversation}) => {
    const theme = useContext(ThemeContext);
    return (
        <div className={styles.chatBubble} style={{
            backgroundColor: theme.theme === Themes.dark ? Colors.blackBackground : "#d0e4f2",
            color: theme.theme === Themes.dark ? Colors.whiteFont : "#FFFFFF"
        }}>
            {!conversation.userMessage && <img src={Images.openBotLogo} width={"20%"} alt={"openbot"}/>}
            {conversation.userMessage &&
                <UserMessage timestamp={conversation.userTimestamp} message={conversation.userMessage}/>}
            <AssistantResponse timestamp={conversation.AITimestamp} message={conversation.AIMessage}/>
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
 *
 * Component to display assistant's response with timestamp
 * @returns {Element}
 * @constructor
 * @param props
 */
const AssistantResponse = ({timestamp, message}) => {
    const [loader, setLoader] = useState(false);
    const parsedMessage = parseResponseMessage(message);
    const theme = useContext(ThemeContext)
    useEffect(() => {
        if (message === "") {
            setLoader(true);
        } else {
            setLoader(false);
        }
    }, [message]);

    return (
        <div className={styles.responseBox} title={timestamp} style={{
            backgroundColor: theme.theme === Themes.dark ? Colors.blackPopupBackground : "#FFFFFF",
            color: theme.theme === Themes.dark ? Colors.whiteFont : "#000000"
        }}>
            {loader ? (
                <div
                    className={`${styles.loader} ${theme.theme === Themes.dark ? styles.whiteLoader : styles.loader}`}
                ></div>
            ) : (
                <div className={styles.responseContent} style={{
                    backgroundColor: theme.theme === Themes.dark ? Colors.blackPopupBackground : "#FFFFFF",
                    color: theme.theme === Themes.dark ? Colors.whiteFont : "#000000"
                }}>{parsedMessage}
                    <div className={styles.timestamp}>{timestamp}</div>
                </div>
            )}
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