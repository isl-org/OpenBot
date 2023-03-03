import {BrowserRouter, Route, Routes, useLocation} from 'react-router-dom'
import Home from "../../pages/home";
import Playground from "../../pages/playground";
import {useEffect, useState} from "react";
import {createBrowserHistory} from "history";

export const RouterComponent = () => {
    return (

            <Routes>
                <Route exact path="/" element={<Home/>}/>
                <Route exact path="/playground" element={<Playground/>}/>
                )}/>
            </Routes>
    )
}
