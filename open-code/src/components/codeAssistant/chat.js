import React, {useContext, useEffect, useRef, useState} from 'react';
import styles from "./chat.module.css";
import ChatBox from '../chatBox/messagebox';
import {getAIMessage} from "../../services/chatAssistant";
import {Images} from "../../utils/images.js";
import {Themes, Errors, ChatConstants} from '../../utils/constants.js';
import {ThemeContext} from "../../App";
import {colors as Colors} from "../../utils/color";
import {StoreContext} from "../../context/context";
import {addBlocksToWorkspace} from "../blockly/imageConverter";
import {getCurrentProject} from "../../services/workspace";
import {cleanAndFormatResponse, handler} from "../../utils/handler";

/**
 * Chat component handles user interactions and displays chat interface.
 * @param props
 * @returns {Element}
 * @constructor
 */
const Chat = ({drawer}) => {
    const theme = useContext(ThemeContext);
    const {workspace, setDrawer} = useContext(StoreContext);
    const [inputValue, setInputValue] = useState('');
    const [loader, setLoader] = useState(false);
    const [isTyping, setIsTyping] = useState(false);
    const timestamp = new Date().toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'});
    const abortControllerRef = useRef(null);
    const chatContainerRef = useRef(null);
    const [codeBufferLoader, setCodeBufferLoader] = useState(false);
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

    //Handles user click on send button
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
        let messageBuffer = '';

        //Handles incoming message chunks from the api on streaming.
        const onMessage = (chunk) => {

            messageBuffer += chunk;


            if (messageBuffer.includes('$$RESPONSE$$')) {
                setCodeBufferLoader(true);
            } else {
                setCodeBufferLoader(false);
            }
            if ((messageBuffer.includes('$$CONTENT$$":"')) && chunk !== '":"') {
                setIsTyping(true);
                //to stop displaying xml
                //|| messageBuffer.includes('BLOCKLY_XML_CODE":"') || "','"
                if (messageBuffer.includes('","')) {
                    if ("$$RESPONSE$$") {
                        messageBuffer = '';
                        setIsTyping(false);
                    }
                    //setCodeBufferLoader(true);
                } else {
                    // setCodeBufferLoader(false);
                    setCurrentMessage((prevState) => ({
                        ...prevState, AIMessage: prevState.AIMessage + chunk, AITimestamp: timestamp,
                    }));
                }
            }
        };

        // To add the blocks to the current workspace
        getAIMessage(userInput, getCurrentProject().xmlValue, abortControllerRef.current.signal, onMessage).then((res) => {
            if (res !== undefined) {
                let finalMessage = handler(res);
                if (finalMessage !== undefined) {
                    setCodeBufferLoader(false);
                    setCurrentMessage((prevState) => ({
                        ...prevState, AIMessage: finalMessage, AITimestamp: timestamp,
                    }));
                } else {
                    setCodeBufferLoader(false);
                }
                addBlocksToWorkspace(res, workspace)
                    .catch((e) => {
                        console.log("Error in creating block png-->", e);
                        setCurrentMessage((prevState) => ({
                            ...prevState, AIMessage: finalMessage + "\n" + Errors.error8, AITimestamp: timestamp,
                        }));
                    })
            } else {
                setCurrentMessage((prevState) => ({
                    ...prevState, AIMessage: Errors.error7, AITimestamp: timestamp
                }));
            }
        }).catch((e) => {
            setCodeBufferLoader(false);
            console.log(e);
        });

        setInputValue('');
    };

    //function to pause the content
    const handlePauseClick = () => {
        if (abortControllerRef.current) {
            abortControllerRef.current.abort();
        }
        setIsTyping(false);
        const finalMessage = cleanAndFormatResponse(currentMessage.AIMessage.replace(/\n+/g, ' ').trim());

        setCurrentMessage((prevState) => ({
            ...prevState, AIMessage: finalMessage, AITimestamp: timestamp
        }));
        if (loader && allChatMessages.length === currentMessage.id) {
            setCurrentMessage((prevState) => ({
                ...prevState, AIMessage: Errors.error7, AITimestamp: timestamp
            }));
        }
    };

    //Effect to update allChatMessages when currentMessage changes
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


    // Effect to scroll chat container to bottom when messages or AI message changes
    useEffect(() => {
        if (chatContainerRef.current && chatContainerRef.current.scrollHeight !== null) {

            chatContainerRef.current.scrollTop = chatContainerRef.current.scrollHeight;


        }
    }, [allChatMessages, currentMessage.AIMessage]);

    // Renders chat interface
    return (<div className={styles.chatMainContainer}
                 style={{
                     backgroundColor: theme.theme === Themes.dark ? Colors.blackBackground : "#d0e4f5",
                     color: theme.theme === Themes.dark ? Colors.whiteFont : "#000000"

                 }}
    >
        {drawer ? <div
            style={{
                backgroundColor: theme.theme === Themes.dark ? Colors.blackPopupBackground : "#FFFFFF",
                color: theme.theme === Themes.dark ? Colors.whiteFont : "#000000",
            }}
        >
            <div className={styles.chatHeader}>
                <img src={theme.theme === Themes.dark ? Images.aiSupportWhite : Images.aiSupport}
                     alt="Chat Assistant Logo"
                     style={{
                         width: theme.theme === Themes.dark ? "25px" : "30px"
                     }}/>
                <h1>{ChatConstants.Playground}</h1>
                <img onClick={() => setDrawer(false)} alt={"cross icon"} className={styles.crossIcon}
                     src={theme === Themes.dark ? Images.darkCrossIcon : Images.lightCrossIcon}/>
            </div>
        </div> : ""}
        <div ref={chatContainerRef} style={{height: "100%", overflow: "auto"}}>
            {allChatMessages.map((conversation, index) => (<ChatBox
                key={index}
                conversation={conversation}
                handlePauseClick={handlePauseClick}
                setIsTyping={setIsTyping}
                allChatMessages={allChatMessages}
                setLoader={setLoader}
                setCodeBufferLoader={setCodeBufferLoader}
                codeBufferLoader={codeBufferLoader}
                loader={loader}
                chatContainerRef={chatContainerRef}

            />))}
        </div>
        {drawer ? <ChatBottomBar
            inputValue={inputValue}
            handleSendClick={handleSendClick}
            setInputValue={setInputValue}
            isTyping={isTyping}
            handlePauseClick={() => handlePauseClick(currentMessage.id)}
        /> : ""}
    </div>);
};

/**
 * Component for rendering input field and send/pause button
 * @param inputValue
 * @param handleSendClick
 * @param setInputValue
 * @param isTyping
 * @param handlePauseClick
 * @returns {Element}
 * @constructor
 */
const ChatBottomBar = ({inputValue, handleSendClick, setInputValue, isTyping, handlePauseClick}) => {
    const theme = useContext(ThemeContext);

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
// Exports Chat component as default
export default Chat;
