
import {Navbar} from "../../components/navBar/Navbar";
import React from "react";
import {screens} from "../../utils/constants";
import NewProject from "../../components/home/MyProjects/NewProject";
import Carousel from 'react-material-ui-carousel'


function Home() {
    return <div>
        <Navbar/>
        <HomeCarousel/>
        <NewProject/>

    </div>

}

const  HomeCarousel = ()=>{
    return (
        <Carousel duration={1000} interval={3000} swipe={true} dir = 'rtl' animation={"slide"}  stopAutoPlayOnHover={false} indicatorIconButtonProps={{
            style: {
                padding: '8px',
                color: '#C7EBFF',
                marginTop : '8rem'

            }
        }}
                  activeIndicatorIconButtonProps={{
                      style: {
                          color : '#0071C5'
                      }
                  }}
        >
            {
                screens.map(screen =>screen.component)
            }
        </Carousel>
    )
}

export default Home;
