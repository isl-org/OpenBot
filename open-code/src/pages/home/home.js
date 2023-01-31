import {Navbar} from "../../components/navBar/Navbar";
import React from "react";
import {HomeCarousel} from "../../components/home/carousel/carousel";
import {NewProject} from "../../components/home/MyProjects/NewProject";
import {Cookies} from "react-cookie-consent";

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
