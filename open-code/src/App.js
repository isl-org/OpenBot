import './App.css';
import {RouterComponent} from "./components/router/router";
import StoreProvider from './context/context';
import {createContext, useState} from "react";

export const ThemeContext = createContext(null);

function App() {
    if (localStorage.getItem("theme") == null) {
        localStorage.setItem("theme", "light");
    }
    let onPageLoad = localStorage.getItem("theme") || "";
    const [theme, setTheme] = useState(onPageLoad);

    const toggleTheme = () => {
        if (theme === "light") {
            setTheme("dark")
            localStorage.setItem("theme", "dark")
        } else {
            setTheme("light")
            localStorage.setItem("theme", "light")
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
