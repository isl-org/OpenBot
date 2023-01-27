
import {Navbar} from "../../components/navBar/Navbar";
import React from "react";
import {NewProject} from "../../components/home/MyProjects/NewProject";
import {HomeCarousel} from "../../components/carousel/carousel";


function Home() {
    return <div>
        <Navbar/>
        <HomeCarousel/>
        <NewProject/>
    </div>

}


export default Home;
