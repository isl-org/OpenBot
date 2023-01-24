import React from 'react';
import {LeftSectionStyles as useStyles} from "./styles";


function LeftSection(props) {
    const {content, setTab, tab} = props
    console.log(tab)
    const classes = useStyles();

    return (
        <div className={classes.Main}>
            <div className={classes.IconContent}>
                {
                    content.map((data) => (
                        <div className={(data.title === tab) ? classes.ItemsSelected : classes.Items}
                             onClick={() => setTab(data.title)}>
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