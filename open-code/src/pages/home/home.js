
import {Navbar} from "../../components/Navbar";
import React, {useEffect, useState} from "react";
import {screens} from "../../utils/constants";
import NewProject from "../../components/Home/MyProjects/NewProject";
import SavedProjects from "../../components/Home/MyProjects/SavedProjects";
import Carousel from 'react-material-ui-carousel'
import {SavedProjectsStyles as useStyles} from "../../components/Home/MyProjects/styles";
    function Home() {
        const classes = useStyles();
        return <div>
            <Navbar/>
            <HomeCarousel/>
            <NewProject/>
            {/*<SavedProjects/>*/}
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
