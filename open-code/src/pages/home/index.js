import {Header} from "../../components/navBar/header";
import React, {useEffect} from "react";
import {HomeCarousel} from "../../components/homeComponents/carousel/carousel";
import {NewProject} from "../../components/homeComponents/myProjects/newProject";
import CookiesComponent from "../../components/homeComponents/cookies/cookies";
import {updateLocalProjects, filterProjects} from "../../services/workspace";

/**
 * Home component is the main homepage displayed to the user when they open the application
 * @returns {JSX.Element}
 * @constructor
 */
function Home() {
    useEffect(() => {
        updateLocalProjects()
        filterProjects()
    }, [])
    return (
        <div style={{height: '100vh'}}>
            <Header/>
            <HomeCarousel/>
            <NewProject/>
            <CookiesComponent/>
        </div>
    )
}
export default Home;
