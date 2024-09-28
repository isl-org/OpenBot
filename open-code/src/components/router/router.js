import {Outlet, Route, Routes, useNavigate} from 'react-router-dom'
import Home from "../../pages/home";
import Playground from "../../pages/playground";
import {PathName} from "../../utils/constants";
import {useContext} from "react";
import {ThemeContext} from "../../App";
import {Box, Container, Grid, Typography} from "@mui/material";

/**
 * Router to maintain different paths of the application
 * @returns {JSX.Element}
 * @constructor
 */
export const RouterComponent = () => {
    return (
        <Routes>
            <Route path={PathName.home} element={<Outlet/>}>
                <Route index element={<Home/>}/>
                <Route path={PathName.playGround} element={<Playground/>}/>
                <Route path="*" element={<NotFound/>}/>
            </Route>
        </Routes>
    )
}

function NotFound() {
    const {theme} = useContext(ThemeContext)
    let navigate = useNavigate();
    const openHomePage = () => {
        navigate(`/`);
    }
    return (
        <Box
            sx={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                minHeight: '100vh'
            }}
        >
            <Container maxWidth="md">
                <Grid container spacing={2}>
                    <Grid xs={6}>
                        <Typography style={{color: theme === "dark" ? "white" : "black"}} variant="h1">
                            404
                        </Typography>
                        <Typography style={{color: theme === "dark" ? "white" : "black"}} variant="h6">
                            The page you’re looking for doesn’t exist.
                        </Typography>
                        <button onClick={openHomePage}>Go to home page</button>
                    </Grid>
                    <Grid xs={6}>
                        <img
                            src="https://cdn.pixabay.com/photo/2017/03/09/12/31/error-2129569__340.jpg"
                            alt=""
                            width={500} height={250}
                        />
                    </Grid>
                </Grid>
            </Container>
        </Box>
    );
}

