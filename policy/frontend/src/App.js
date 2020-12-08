import {Link, Route, Switch} from 'react-router-dom';
import {Container, Content, Header, Nav, Navbar} from 'rsuite';
import 'rsuite/dist/styles/rsuite-default.css';
import './App.css';
import {TrainPage} from './pages/TrainPage';
import {UploadedPage} from './pages/UploadedPage';

function App() {
    return (
        <Container className="App">
            <Header>
                <Navbar appearance="inverse">
                    <Navbar.Header>
                        <a href="#/" className="navbar-brand logo">Openbot Driving Policy Trainer</a>
                    </Navbar.Header>
                    <Navbar.Body>
                        <Nav>
                            <Nav.Item href="#/uploaded">Uploaded</Nav.Item>
                            <Nav.Item href="#/datasets">Datasets</Nav.Item>
                            <Nav.Item href="#/train">Train</Nav.Item>
                        </Nav>
                    </Navbar.Body>
                </Navbar>
            </Header>
            <Content>
                <Switch>
                    <Route path="/uploaded">
                        <UploadedPage />
                    </Route>
                    <Route path="/train">
                        <TrainPage />
                    </Route>
                    <Route>
                        Home
                    </Route>
                </Switch>
            </Content>
        </Container>
    );
}

export default App;
