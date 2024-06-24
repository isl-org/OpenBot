import {Header} from "../../components/navBar/header";
import React, {useContext, useEffect} from "react";
import {HomeCarousel} from "../../components/homeComponents/carousel/carousel";
import {NewProject} from "../../components/homeComponents/myProjects/newProject";
import CookiesComponent from "../../components/homeComponents/cookies/cookies";
import {updateLocalProjects} from "../../services/workspace";
import {ThemeContext} from "../../App";

/**
 * Home component is the main homepage displayed to the user when they open the application
 * @returns {JSX.Element}
 * @constructor
 */
function Home() {
    const {theme} = useContext(ThemeContext);
    useEffect(() => {
        //update local projects if there is any change.
        updateLocalProjects();
    }, [])
    return (
        <div style={{height: '100vh', backgroundColor: theme === "dark" ? "#202020" : "#f8f9fb"}}>
            <Header/>
            <HomeCarousel/>
            <NewProject/>
            <CookiesComponent/>
        </div>
    )
}

export default Home;
