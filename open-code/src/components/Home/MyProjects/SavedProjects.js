import React from 'react';
import {SavedProjectsStyles as useStyles} from "./styles";
import Card from "./Card";
import NewProjectButton from "./NewProjectButton";

function SavedProjects(props) {
    const classes = useStyles();
    return (
        <div className={classes.Main}>
            <div className={classes.Heading}>My Projects</div>
            <div className={classes.CardMain}>
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