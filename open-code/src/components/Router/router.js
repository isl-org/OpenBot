import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'
import Home from "../../pages/home/home";
import Profile from "../../pages/Profile/Profile";
// import WorkSpace from "../../pages/BlocklyWorkSpace/WorkSpace";
import Playground from "../Playground/Playground";
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
