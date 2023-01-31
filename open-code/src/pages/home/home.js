import {Navbar} from "../../components/navBar/Navbar";
import React from "react";
import Cookies from "../../components/Home/Cookies/Cookies";
import {HomeCarousel} from "../../components/Home/carousel/carousel";
import {NewProject} from "../../components/Home/MyProjects/NewProject";
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
