import {Header} from "../../components/navBar/header";
import React from "react";
import {HomeCarousel} from "../../components/homeComponents/carousel/carousel";
import {NewProject} from "../../components/homeComponents/myProjects/newProject";
import CookiesComponent from "../../components/homeComponents/cookies/cookies";

function Home() {
    return (
        <div>
            <Header/>
            <HomeCarousel/>
            <NewProject/>
            <CookiesComponent/>
        </div>
    )
}

export default Home;
