import {Navbar} from "../../components/navBar/Navbar";
import React from "react";
import {HomeCarousel} from "../../components/home/carousel/carousel";
import {NewProject} from "../../components/home/MyProjects/NewProject";
import CookiesComponent from "../../components/home/Cookies/Cookies";

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
