import {Navbar} from "../../components/navBar/Navbar";
import React from "react";
import Cookies from "../../components/home/Cookies/Cookies";
import {HomeCarousel} from "../../components/home/carousel/carousel";
import {NewProject} from "../../components/home/MyProjects/NewProject";

function Home() {
    return (
        <div>
            <Navbar/>
            <HomeCarousel/>
            <NewProject/>
            <Cookies/>
        </div>
    )
}

export default Home;
