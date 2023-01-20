
import {Navbar} from "../../components/Navbar";
import React, {useEffect, useState} from "react";
import {screens} from "../../utils/constants";
import NewProject from "../../components/Home/MyProjects/NewProject";
import SavedProjects from "../../components/Home/MyProjects/SavedProjects";
    function Home() {
        const [currentScroll, setCurrentScroll] = useState(0);
        useEffect(() => {
            const intervalId = setInterval(() => {
                setCurrentScroll((currentScroll + 1) % screens.length);
            }, 3000);
            return () => clearInterval(intervalId);
        }, [currentScroll]);

        return <div>
            {screens.map(screen => screen.key === currentScroll && screen.component)}
            <Navbar/>
            <NewProject/>
        </div>

    }

export default Home;