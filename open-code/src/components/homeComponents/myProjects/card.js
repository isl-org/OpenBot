import React from 'react';
import {SavedProjectsStyles} from "./styles";

function Card(props) {

    return (
        <div style={SavedProjectsStyles.Content}>
            <div style={SavedProjectsStyles.Card}>
                <div style={SavedProjectsStyles.CardHeadingIcon}>
                    <div style={SavedProjectsStyles.CardHeading}>Blink Led</div>
                    {/*<div className={classes.Icon}>Icon</div>*/}
                </div>
                <div style={SavedProjectsStyles.Date}>Jan 4, 2023</div>
            </div>
        </div>
    );
}

export default Card;
