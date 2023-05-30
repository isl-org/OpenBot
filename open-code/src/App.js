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
    let onPageLoad = localStorage.getItem("theme") || ""
    const [theme, setTheme] = useState(onPageLoad);
    const [internetOn, setInternetOn] = useState(window.navigator.onLine);
    const [user, setUser] = useState();

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
    }, [])

    //session time out function.
    useEffect(() => {
        let timeoutId;
        const unsubscribe = auth.onAuthStateChanged((user) => {
            if (user) {
                // User is signed in.
                user.getIdTokenResult()
                    .then((idTokenResult) => {
                        const expirationTime = idTokenResult?.expirationTime;
                        const sessionTimeoutMs = new Date(expirationTime).getTime() - Date.now();
                        if (timeoutId) {
                            clearTimeout(timeoutId);
                        }
                        timeoutId = setTimeout(async () => {
                            alert('Your session has expired. You have been signed out.')
                            await googleSignOut().then();
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
    }, []);


    //check if user prefer theme is saved or not if not then saved it to system theme
    if (!localStorage.getItem("theme")) {
        const darkThemeMq = window.matchMedia("(prefers-color-scheme: dark)"); //check system theme
        //set system prefer theme in localstorage
        localStorage.setItem("theme", darkThemeMq.matches ? Themes.dark : Themes.light);
    }

    //if theme is dark and when click on change theme then setTheme light and vice-versa.
    const toggleTheme = () => {
        if (theme === "light") {
            setTheme("dark");
            localStorage.setItem("theme", Themes.dark)
            document.body.classList.replace("light", "dark"); // for background theme
        } else {
            setTheme("light");
            localStorage.setItem("theme", Themes.light)
            document.body.classList.replace("dark", "light"); //for background theme
        }
    };

    return (
        <ThemeContext.Provider value={{theme, toggleTheme}}>
            <StoreProvider isOnline={internetOn} user={user} setUser={setUser}>
                <div id={theme}>
                    <RouterComponent/>
                </div>
                <ToastContainer autoClose={5000}/>
            </StoreProvider>
        </ThemeContext.Provider>
    );
}

export default App;

