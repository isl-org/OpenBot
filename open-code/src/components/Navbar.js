import React from 'react';
import "./Navbar.css"

export function Navbar(){
        return (
            <nav className="nav">
                <a href="/" className="title-icon" ></a>
                <a href="/" className="nav-title">OpenCode</a>
                <a href="/" className="info-icon" ></a>
                <a href="/" className="moon-icon" ></a>
                <a href="/" className="line-icon" ></a>
                <div>
                <div className="signIn-icon"><a href="/" ></a></div>
                </div>
                <a href="/" className="sigIn-name">Sign in</a>

            </nav>
        );
    }

