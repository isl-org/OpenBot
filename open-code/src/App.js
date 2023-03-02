import './App.css';
import {RouterComponent} from "./components/router/router";
import StoreProvider from './context/context';
import {createContext, useState} from "react";
import {Themes} from "./utils/constants";

export const ThemeContext = createContext(null);

function App() {
    if (localStorage.getItem("theme") == null) {
        localStorage.setItem("theme", Themes.light);
    }

    const darkThemeMq = window.matchMedia("(prefers-color-scheme: dark)");
    let onPageLoad = localStorage.getItem("theme") || darkThemeMq.matches ? Themes.dark : Themes.light;
    const [theme, setTheme] = useState(onPageLoad);

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
                    <RouterComponent/>
                </div>
            </StoreProvider>
        </ThemeContext.Provider>
    );
}

export default App;
