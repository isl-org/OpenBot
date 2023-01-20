import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'
import Home from "../../pages/home/home";
export const RouterComponent = ()=>{
    return(
        <Router>

            <Routes>
                <Route exact path="/" element={<Home/>}/>
            </Routes>
        </Router>
    )
}
