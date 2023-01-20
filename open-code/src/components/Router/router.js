import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'
import Home from "../../pages/home/home";
import Profile from "../../pages/Profile/Profile";
export const RouterComponent = ()=>{
    return(
        <Router>
            <Routes>
                <Route exact path="/" element={<Home/>}/>
                <Route exact path="/profile" element={<Profile/>}/>
            </Routes>
        </Router>
    )
}
