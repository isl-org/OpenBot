import {Route, Routes} from 'react-router-dom'
import Home from "../../pages/home";
import Playground from "../../pages/playground";


/**
 * Router to maintain different paths of the application
 * @returns {JSX.Element}
 * @constructor
 */
export const RouterComponent = () => {
    return (
        <Routes>
            <Route exact path="/" element={<Home/>}/>
            <Route exact path="/playground" element={<Playground/>}/>
            )}/>
        </Routes>
    )
}
