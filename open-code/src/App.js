import './App.css';
import {RouterComponent} from "./components/router/router";
import StoreProvider from './context/Context'

function App() {
    return (
        <div style={{fontStyle: 'Gilroy-Black'}}>
            <StoreProvider>
                <RouterComponent/>
            </StoreProvider>
        </div>

    );
}

export default App;
