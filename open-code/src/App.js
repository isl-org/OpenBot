import './App.css';
import {RouterComponent} from "./components/router/router";
import StoreProvider from './context/context';
import {createContext, useEffect, useState} from "react";
import Mainpage from "./components/firebaseComponents/main";
import Login from "./components/firebaseComponents/login";
import firebase from "./firebase_setup/firebase";

export const ThemeContext = createContext(null);

function App() {
    if(localStorage.getItem("theme")  == null){
        localStorage.setItem("theme", "light");
    }
    let onPageLoad = localStorage.getItem("theme") || "";
    const [theme, setTheme] = useState(onPageLoad);
    const toggleTheme = () => {
        let theme = localStorage.getItem("theme");
        if (theme === "light") {
            setTheme("dark")
            localStorage.setItem("theme", "dark")
        } else if (theme === "dark") {
            setTheme("light")
            localStorage.setItem("theme", "light")
        }
    }

    const [user, setUser] = useState(null);

    useEffect(() => {
        firebase.auth().onAuthStateChanged(user => {
            setUser(user);
        })
    }, [])

    console.log(user);
    return (

        <ThemeContext.Provider value={{theme, toggleTheme}}>
            <StoreProvider>
                <div id={theme}>
                    <RouterComponent/>
                   {/*<Login/>*/}
                </div>
            </StoreProvider>
        </ThemeContext.Provider>
    );
}

export default App;
