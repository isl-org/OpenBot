import React from 'react';
import {NewProjectStyles} from "./styles";
import Triangle from "../../../assets/Triangle.png";
import NewProjectButton from "./NewProjectButton";

export const NewProject =(props)=> {
    return (
        <div style={NewProjectStyles.Main}>
            <div style={NewProjectStyles.Heading}>My Projects</div>
            <div style={NewProjectStyles.ButtonsMessage}>
                <NewProjectButton/>
                <div style={NewProjectStyles.MessageIcon}>
                    <img alt="Triangle" style={NewProjectStyles.TriangleIcon} src={Triangle}/>
                    <div style={NewProjectStyles.Message}>Click to create a new project.</div>
                </div>
            </div>

        </div>
    );
}

