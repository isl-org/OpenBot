import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'
import Home from "../../pages/home/home";
import Profile from "../../pages/profile/Profile";
import Playground from "../playground/Playground";
export const RouterComponent = ()=>{
    return(
        <Router>
            <Routes>
                <Route exact path="/" element={<Home/>}/>
                <Route exact path="/profile" element={<Profile/>}/>
                <Route exact path="/playground" element={<Playground/>}/>
            </Routes>
        </Router>
    )
}
