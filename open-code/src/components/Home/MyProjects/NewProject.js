import React from 'react';
import {NewProjectStyles as useStyles} from "./styles";
import Triangle from "../../../assets/Triangle.png";
import NewProjectButton from "./NewProjectButton";

function NewProject(props) {
    const classes = useStyles();
    return (
        <div className={classes.Main}>
            <div className={classes.Heading}>My Projects</div>
            <div className={classes.ButtonsMessage}>
                <NewProjectButton/>
                <div className={classes.MessageIcon}>
                    <img alt="Triangle" className={classes.TriangleIcon} src={Triangle}/>
                    <div className={classes.Message}>Click to create a new project.</div>
                </div>
            </div>

        </div>
    );
}

export default NewProject;