import './App.css';
import {RouterComponent} from "./components/router/router";
import StoreProvider from './context/context';
import {createContext, useEffect, useState} from "react";
import {Constants, Themes} from "./utils/constants";
import {useLocation} from "react-router-dom";
import styles from "./components/homeComponents/carousel/carousel.module.css";
import {Images} from "./utils/images";

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

    const time=new Date().getTime();
     // useEffect(()=>{
     //     checkAccessTokenValidity(time)
     // },[time])
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
        window.location.reload()
        if (theme === Themes.light) {
            setTheme(Themes.dark)
            localStorage.setItem("theme", Themes.dark)
        } else {
            setTheme(Themes.light)
            localStorage.setItem("theme", Themes.light)
        }
    }
    return (

        <ThemeContext.Provider value={{theme, toggleTheme}}>
            <StoreProvider>
                <div id={theme}>
                    {isLoading ?
                        <LoadingComponent/> :
                        <RouterComponent/>
                    }
                </div>
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
