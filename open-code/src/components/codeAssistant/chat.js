import React, {useContext, useEffect, useRef, useState} from 'react';
import styles from "./chat.module.css";
import ChatBox from '../chatBox/messagebox';
import {getAIMessage} from "../../services/chatAssistant";
import {Images} from "../../utils/images.js";
import {Themes, Errors, ChatConstants} from '../../utils/constants.js';
import {ThemeContext} from "../../App";
import {colors as Colors} from "../../utils/color";
import {StoreContext} from "../../context/context";
import {extractXmlFromResponse} from "../blockly/imageConverter";

const Chat = (props) => {
    const theme = useContext(ThemeContext);
    const {workspace} = useContext(StoreContext);
    const [inputValue, setInputValue] = useState('');
    const [loader, setLoader] = useState(false);
    const [isTyping, setIsTyping] = useState(false);
    const timestamp = new Date().toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'});
    const abortControllerRef = useRef(null);
    const chatContainerRef = useRef(null);
    const [allChatMessages, setAllChatMessages] = useState([{
        userMessage: "",
        AIMessage: ChatConstants.Message,
        id: 1,
        userTimestamp: "",
        AITimestamp: new Date().toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'}),
        paused: false
    },]);
    const [currentMessage, setCurrentMessage] = useState({
        userMessage: "",
        AIMessage: "",
        id: 2,
        userTimestamp: new Date().toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'}),
        AITimestamp: "",
        paused: false
    });
    const handleSendClick = async () => {
        const userInput = inputValue.trim().toLowerCase();
        if (userInput === '') {
            return;
        }
        setIsTyping(true);
        setCurrentMessage((prevState) => ({
            ...prevState,
            userMessage: userInput,
            userTimestamp: timestamp,
            AIMessage: "",
            id: allChatMessages.length + 1,
            AITimestamp: "",
            paused: false
        }));
        abortControllerRef.current = new AbortController();

        getAIMessage(userInput, abortControllerRef.current.signal).then((res) => {
            console.log("res::", res);
            if (res !== undefined) {
                extractXmlFromResponse(res, workspace).then((image) => {
                    setCurrentMessage((prevState) => ({
                        ...prevState, AIMessage: res, AITimestamp: timestamp,
                    }));
                })
                    .catch((e) => {
                        console.log("Error in creating block png-->", e);
                        setCurrentMessage((prevState) => ({
                            ...prevState, AIMessage: Errors.error8, AITimestamp: timestamp
                        }));
                    })
            } else {
                setCurrentMessage((prevState) => ({
                    ...prevState, AIMessage: Errors.error7, AITimestamp: timestamp
                }));
            }
        }).catch((e) => {
            console.log(e);
            setCurrentMessage((prevState) => ({
                ...prevState, AIMessage: Errors.error6, AITimestamp: timestamp
            }));
        });

        setInputValue('');
    };

    const handlePauseClick = (id) => {
        if (abortControllerRef.current) {
            abortControllerRef.current.abort();
        }
        setIsTyping(false);
        if (currentMessage.AIMessage !== "") {
            setCurrentMessage((prevState) => ({
                ...prevState, paused: true
            }));
        }
    };

    useEffect(() => {
        if (currentMessage.AIMessage) {
            setAllChatMessages((prevMessages) => {
                const updatedMessages = [...prevMessages];
                if (updatedMessages.length > 0) {
                    updatedMessages[updatedMessages.length - 1] = currentMessage;
                }
                return updatedMessages;
            });
        } else if (currentMessage.userMessage) {
            setAllChatMessages((prevMessages) => [...prevMessages, currentMessage]);
        }
    }, [currentMessage]);

    useEffect(() => {
        if (chatContainerRef.current) {
            setTimeout(() => {
                chatContainerRef.current.scrollTop = chatContainerRef.current.scrollHeight;
            }, 100);
        }
    }, [allChatMessages, currentMessage.AIMessage]);

    return (<div className={styles.chatMainContainer}
                 style={{
                     backgroundColor: theme.theme === Themes.dark ? Colors.blackBackground : "#d0e4f5",
                     color: theme.theme === Themes.dark ? Colors.whiteFont : "#000000"
                 }}
        >
            <div className={styles.chatHeader}>
                <img src={Images.aiSupport} alt="Chat Assistant Logo" style={{width: 40, height: 40}}/>
                <h1>{ChatConstants.Playground}</h1>
            </div>
            <div ref={chatContainerRef} style={{height: "100%", overflow: "auto"}}>
                {allChatMessages.map((conversation, index) => (<ChatBox
                        key={index}
                        conversation={conversation}
                        handlePauseClick={handlePauseClick}
                        setIsTyping={setIsTyping}
                        allChatMessages={allChatMessages}
                        setLoader={setLoader}
                        loader={loader}
                        chatContainerRef={chatContainerRef}

                    />))}
            </div>
            <ChatBottomBar
                inputValue={inputValue}
                handleSendClick={handleSendClick}
                setInputValue={setInputValue}
                isTyping={isTyping}
                handlePauseClick={() => handlePauseClick(currentMessage.id)}

            />
        </div>);
};

const ChatBottomBar = ({inputValue, handleSendClick, setInputValue, isTyping, handlePauseClick}) => {
    const theme = useContext(ThemeContext);

    useEffect(() => {
        console.log("isTyping:", isTyping);
    }, [isTyping]);

    return (<div className={styles.chatBottomBar}>
            <input
                style={{
                    backgroundColor: theme.theme === Themes.dark ? Colors.blackPopupBackground : "#FFFFFF",
                    color: theme.theme === Themes.dark ? Colors.whiteFont : "#000000"
                }}
                type="text"
                placeholder="Enter a prompt here..."
                className={styles.inputField}
                value={inputValue}
                onChange={(e) => setInputValue(e.target.value)}
                onKeyPress={(e) => {
                    if (e.key === 'Enter') {
                        handleSendClick();
                    }
                }}
                disabled={isTyping}
            />
            <div
                onClick={isTyping ? handlePauseClick : handleSendClick}
                className={`sendButton ${inputValue.trim() === '' ? 'disabled' : ''} ${isTyping ? '' : ''}`}
                style={inputValue.trim() === '' ? {cursor: 'not-allowed', opacity: 0.5} : {}}
            >
                <img
                    alt={isTyping ? "Pause Icon" : "Send Icon"}
                    src={isTyping ? Images.pause : Images.sendIcon}
                    className={styles.sendIcon}
                />
            </div>
        </div>);
};

export default Chat;
