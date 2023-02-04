import './App.css';
import {RouterComponent} from "./components/router/router";
import StoreProvider from './context/context';
import {createContext, useState} from "react";

export const ThemeContext = createContext(null);

function App() {
    const [theme, setTheme] = useState("light");


    const toggleTheme = () => {
        setTheme((curr) => (curr === "light" ? "dark" : "light"));
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
