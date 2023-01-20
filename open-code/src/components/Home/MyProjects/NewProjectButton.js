import React from 'react';
import {NewProjectStyles as useStyles} from "./styles";

function NewProjectButton(props) {
    const classes = useStyles();
    return (
        <div className={classes.Content}>
            <div className={classes.Button}>
                <div className={classes.AddIconImage}>
                    <div className={classes.plus}>+</div>
                </div>
            </div>
        </div>

    );
}

export default NewProjectButton;