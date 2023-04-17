import './App.css';
import {RouterComponent} from "./components/router/router";
import StoreProvider from './context/context';
import {createContext, useEffect, useState} from "react";
import {Constants, Themes} from "./utils/constants";
import {useLocation} from "react-router-dom";
import styles from "./components/homeComponents/carousel/carousel.module.css";
import {Images} from "./utils/images";
import {auth, googleSignOut} from "./services/firebase";
import {ToastContainer} from "react-toastify";

export const ThemeContext = createContext(null);


/**
 * Main function where the application Code is started
 * @returns {JSX.Element}
 * @constructor
 */
function App() {
    const location = useLocation();
    const [isLoading, setIsLoading] = useState(false);
    let onPageLoad = localStorage.getItem("theme") || ""
    const [theme, setTheme] = useState(onPageLoad);

    useEffect(() => {
        //to change body theme of the site
        let darkElement = document.body;
        theme === Themes.dark ? darkElement.classList.add("dark-mode") : darkElement.classList.remove("dark-mode");
    }, [theme]);


    useEffect(() => {
        let timeoutId;
        const unsubscribe = auth.onAuthStateChanged((user) => {
            if (user) {
                // User is signed in.
                user.getIdTokenResult()
                    .then((idTokenResult) => {
                        const expirationTime = idTokenResult?.expirationTime;
                        const sessionTimeoutMs = new Date(expirationTime).getTime() - Date.now() ;
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


    useEffect(() => {
        setIsLoading(true);
        setTimeout(() => {
            setIsLoading(false);
        }, Constants.LoadingTime);
    }, [location]);

    //check if user prefer theme is saved or not if not then saved it to system theme
    if (!localStorage.getItem("theme")) {
        const darkThemeMq = window.matchMedia("(prefers-color-scheme: dark)"); //check system theme
        //set system prefer theme in localstorage
        localStorage.setItem("theme", darkThemeMq.matches ? Themes.dark : Themes.light);
    }

    const toggleTheme = () => {
        if (theme === "light") {
            setTheme("dark");
            localStorage.setItem("theme", Themes.dark)
            document.body.classList.replace("light", "dark");
        } else {
            setTheme("light");
            localStorage.setItem("theme", Themes.light)
            document.body.classList.replace("dark", "light");
        }
    };

    return (

        <ThemeContext.Provider value={{theme, toggleTheme}}>
            <StoreProvider>
                <div id={theme}>
                    {isLoading ?
                        <LoadingComponent/> :
                        <RouterComponent/>
                    }
                </div>
                <ToastContainer autoClose={5000}/>
            </StoreProvider>
        </ThemeContext.Provider>
    );
}

export default App;

/**
 * component to display the loading icon on the screen
 * @returns {JSX.Element}
 * @constructor
 */
function LoadingComponent() {
    return (
        <div className={styles.loading}>
            <img alt="loadingIcon" className={styles.spin} src={Images.logo}/>
        </div>
    )
}
