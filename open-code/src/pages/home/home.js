import {Navbar} from "../../components/navBar/Navbar";
import React from "react";
import {HomeCarousel} from "../../components/Home/carousel/carousel";
import {NewProject} from "../../components/Home/MyProjects/NewProject";
import CookiesComponent from "../../components/Home/Cookies/Cookies";

function Home() {
    return (
        <div>
            <Navbar/>
            <HomeCarousel/>
            <NewProject/>
            <CookiesComponent/>
        </div>
    )
}

export default Home;
