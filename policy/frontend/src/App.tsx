import {Route, Switch} from 'react-router-dom';
import {Container, Content, Header, Nav, Navbar} from 'rsuite';
import 'rsuite/dist/styles/rsuite-default.css';
import {SessionListPage} from 'src/pages/SessionListPage';
import './App.css';
import {ConnectionAlert} from './components/ConnectionAlert';
import {HomePage} from './pages/HomePage';
import {ModelsPage} from './pages/ModelsPage';
import {TrainPage} from './pages/TrainPage';

function App() {
    return (
        <Container className="App">
            <Header>
                <Navbar appearance="inverse">
                    <Navbar.Header>
                        <a href="#/" className="navbar-brand logo">OpenBot Driving Policy Trainer</a>
                    </Navbar.Header>
                    <Navbar.Body>
                        <Nav>
                            <Nav.Item href="#/">Datasets</Nav.Item>
                            <Nav.Item href="#/models">Models</Nav.Item>
                            <Nav.Item href="#/uploaded">Uploaded</Nav.Item>
                            <Nav.Item href="#/train">Train</Nav.Item>
                        </Nav>
                    </Navbar.Body>
                </Navbar>
            </Header>
            <Content>
                <Switch>
                    <Route path="/models">
                        <ModelsPage />
                    </Route>
                    <Route path="/uploaded">
                        <SessionListPage />
                    </Route>
                    <Route path="/train_data/:dataset">
                        <SessionListPage />
                    </Route>
                    <Route path="/test_data/:dataset">
                        <SessionListPage />
                    </Route>
                    <Route path="/train">
                        <TrainPage />
                    </Route>
                    <Route>
                        <HomePage />
                    </Route>
                </Switch>
            </Content>
            <ConnectionAlert/>
        </Container>
    );
}

export default App;
