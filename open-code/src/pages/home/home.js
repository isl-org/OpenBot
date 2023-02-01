import {Navbar} from "../../components/navBar/navbar";
import React from "react";
import {HomeCarousel} from "../../components/homeComponents/carousel/carousel";
import {NewProject} from "../../components/homeComponents/MyProjects/NewProject";
import CookiesComponent from "../../components/homeComponents/Cookies/Cookies";

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
