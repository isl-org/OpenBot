import './App.css';
import {RouterComponent} from "./components/router/router";
import StoreProvider from './context/context';
import {createContext, useEffect, useState} from "react";
import {localStorageKeys, Themes} from "./utils/constants";
import {auth, googleSignOut} from "./services/firebase";
import {ToastContainer} from "react-toastify";

export const ThemeContext = createContext(null);


/**
 * Main function where the application Code is started
 * @returns {JSX.Element}
 * @constructor
 */
function App() {
    let onPageLoad = localStorage.getItem(localStorageKeys.theme) || ""
    const [theme, setTheme] = useState(onPageLoad);
    const [internetOn, setInternetOn] = useState(window.navigator.onLine);
    const [user, setUser] = useState();
    const [isSessionExpireModal, setIsSessionExpireModal] = useState(false);
    const [isSessionExpire, setIsSessionExpire] = useState(false);
    const [isTimeoutId, setTimeoutId] = useState(false);
    let isAndroid = /Android/i.test(navigator.userAgent);

    useEffect(() => {
        window.addEventListener('online', handleOnline);
        window.addEventListener('offline', handleOffline);

        return () => {
            window.removeEventListener('online', handleOnline);
            window.removeEventListener('offline', handleOffline);
        };
    }, []);

    const handleOnline = () => setInternetOn(true);
    const handleOffline = () => setInternetOn(false);

    useEffect(() => {
        //to change body theme of the site
        let darkElement = document.body;
        theme === Themes.dark ? darkElement.classList.add("dark-mode") : darkElement.classList.remove("dark-mode");
    }, [theme]);

    useEffect(() => {
        if (isAndroid) {
            auth.getRedirectResult().then(async function (result) {
                if (result.credential) {
                    localStorage.setItem(localStorageKeys.accessToken, result.credential.accessToken);
                    localStorage.setItem("isSigIn", "true");
                    setUser({
                        photoURL: result.user?.photoURL,
                        displayName: result.user?.displayName,
                        email: result.user?.email,
                    });
                }
            });
        }
    }, [])

    /**
     * function to get cookie from storage
     * @param cname
     * @returns {string}
     */
    function getCookie(cname) {
        let name = cname + "=";
        let decodedCookie = decodeURIComponent(document.cookie);
        let splitParams = decodedCookie.split(';');
        for (let i = 0; i < splitParams.length; i++) {
            let cookieName = splitParams[i];
            while (cookieName.charAt(0) === ' ') {
                cookieName = cookieName.substring(1);
            }
            if (cookieName.indexOf(name) === 0) {
                return cookieName.substring(name.length, cookieName.length);
            }
        }
        return "";
    }

    /**
     * function to delete a cookie
     * @param name
     */
    const delete_cookie = function (name) {
        document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
    };

    //handled single sign-on from dashboard
    useEffect(() => {
        let cookie = getCookie("user");
        if (cookie) {
            let result = cookie
            localStorage.setItem("isSigIn", "true");
            auth.signInWithCustomToken(result).then((res) => {
                delete_cookie("user");
            })
                .catch((error) => {
                    console.log("error::", error);
                })
        }
        let tokenCookie = getCookie("accessToken");
        if (tokenCookie) {
            localStorage.setItem(localStorageKeys.accessToken, tokenCookie);
            delete_cookie("accessToken");
        }
    }, []);

    //session time out function.
    useEffect(() => {
        let timeoutId;
        const unsubscribe = auth.onAuthStateChanged((user) => {
            if (user) {
                if (isTimeoutId) {
                    clearTimeout(timeoutId)
                }
                // User is signed in.
                user.getIdTokenResult()
                    .then((idTokenResult) => {
                        const expirationTime = idTokenResult?.expirationTime;
                        const sessionTimeoutMs = new Date(expirationTime).getTime() - Date.now();
                        if (timeoutId) {
                            clearTimeout(timeoutId);
                        }
                        timeoutId = setTimeout(async () => {
                            setIsSessionExpire(true);
                        }, sessionTimeoutMs);
                    })
                    .catch((error) => {
                        console.error(error);
                    });
            } else {
                // User is signed out
                clearTimeout(timeoutId);
            }
        });

        return () => {
            unsubscribe();
            clearTimeout(timeoutId);
        }
    }, [isTimeoutId]);

    useEffect(() => {
        if (isSessionExpire) {
            alert('Your session has expired. You have been signed out.');
            googleSignOut().then();
            // setIsSessionExpireModal(true);
            setIsSessionExpire(false);
        }
    }, [isSessionExpire])


    //check if user prefer theme is saved or not if not then saved it to system theme
    if (!localStorage.getItem(localStorageKeys.theme)) {
        const darkThemeMq = window.matchMedia("(prefers-color-scheme: dark)"); //check system theme
        //set system prefer theme in localstorage
        localStorage.setItem(localStorageKeys.theme, darkThemeMq.matches ? Themes.dark : Themes.light);
    }

    //if theme is dark and when click on change theme then setTheme light and vice-versa.
    const toggleTheme = () => {
        if (theme === Themes.light) {
            setTheme(Themes.dark);
            localStorage.setItem("theme", Themes.dark)
            document.body.classList.replace(Themes.light, Themes.dark); // for background theme
        } else {
            setTheme(Themes.light);
            localStorage.setItem("theme", Themes.light)
            document.body.classList.replace(Themes.dark, Themes.light); //for background theme
        }
    };

    return (
        <ThemeContext.Provider value={{theme, toggleTheme}}>
            <StoreProvider isOnline={internetOn} isSessionExpireModal={isSessionExpireModal}
                           setIsSessionExpireModal={setIsSessionExpireModal} user={user} setUser={setUser}
                           setIsSessionExpire={setIsSessionExpire} isTimeoutId={isTimeoutId}
                           setTimeoutId={setTimeoutId}>
                <div id={theme}>
                    <RouterComponent/>
                </div>
                <ToastContainer autoClose={5000}/>
            </StoreProvider>
        </ThemeContext.Provider>
    );
}

export default App;

