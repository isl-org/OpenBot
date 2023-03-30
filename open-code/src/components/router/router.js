import {Route, Routes} from 'react-router-dom'
import Home from "../../pages/home";
import Playground from "../../pages/playground";
import {PathName} from "../../utils/constants";

/**
 * Router to maintain different paths of the application
 * @returns {JSX.Element}
 * @constructor
 */
export const RouterComponent = () => {
    return (
        <Routes>
            <Route exact path={PathName.home} element={<Home/>}/>
            <Route exact path={PathName.playGround} element={<Playground/>}/>
        </Routes>
    )
}
