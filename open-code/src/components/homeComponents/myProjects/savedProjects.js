import React from 'react';
import {SavedProjectsStyles} from "./styles";
import Card from "./card";
import NewProjectButton from "./newProjectButton";

function SavedProjects(props) {
    return (
        <div style={SavedProjectsStyles.Main}>
            <div style={SavedProjectsStyles.Heading}>My Projects</div>
            <div style={SavedProjectsStyles.CardMain}>
                <NewProjectButton/>
                <Card/>
                <Card/>
                <Card/>
                <Card/>
                <Card/>
                <Card/>
            </div>
        </div>
    );
}


export default SavedProjects;