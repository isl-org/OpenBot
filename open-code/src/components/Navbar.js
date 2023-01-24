import React from 'react';
import "./Navbar.css"
import info from "../assets/images/info.png"
import moon from "../assets/images/moon.png"
import line from "../assets/images/Line.png"
import icon from "../assets/images/ICON.png"
export function Navbar(){
        return (
            <header className="nav-bar">
                <img className="title-icon" src={icon} alt={""}/>
                <a href="/" className="nav-title">OpenCode</a>
            <nav className="nav">
            <ul>
                <li><img src={info} alt={""}/></li>
                <li><img src={moon} alt={""}/></li>
                <li><img src={line} alt={""}/></li>
            </ul>
            </nav>
                <button><a href="/" className="sigIn-name">Sign in</a></button>
            </header>
        );
    }

