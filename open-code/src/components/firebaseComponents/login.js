import React from 'react';
import {auth, provider,signInWithGoogle} from '../../firebase_setup/firebase';

const Login = () => {

    return (
        <div>
            <button className="button" onClick={signInWithGoogle}><i className="fab fa-google"></i>Sign in with google</button>
        </div>
    )
}

export default Login;
