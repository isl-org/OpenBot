import React from 'react';
import {SavedProjectsStyles as useStyles} from "./styles";

function Card(props) {
    const classes = useStyles();
    return (
        <div className={classes.Content}>
            <div className={classes.Card}>
                <div className={classes.CardHeadingIcon}>
                    <div className={classes.CardHeading}>Blink Led</div>
                    {/*<div className={classes.Icon}>Icon</div>*/}
                </div>
                <div className={classes.Date}>Jan 4, 2023</div>
            </div>
        </div>
    );
}

export default Card;
