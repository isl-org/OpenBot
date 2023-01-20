import React from 'react';
import {LeftSectionStyles as useStyles} from "./styles";


function LeftSection(props) {
    const classes = useStyles();
    return (
        <div className={classes.Main}>
            <div className={classes.IconContent}>
                {
                    props.content.map((data) => (
                        <div className={!data.selected ? classes.Items : classes.ItemsSelected}>
                            <img alt="Icon" className={classes.Icon} src={data.Icon}/>
                            <div className={classes.Content}>{data.title}</div>
                        </div>
                    ))
                }
            </div>
        </div>
    );
}

export default LeftSection;