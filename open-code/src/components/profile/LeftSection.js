import React from 'react';
import {LeftSectionStyles} from "./styles";
import {Content} from "../../utils/constants";


function LeftSection(props) {
    const {content, setTab, tab, setLogOut,logOut} = props
    return (
        <div style={LeftSectionStyles.Main}>
            <div style={LeftSectionStyles.IconContent}>
                {
                    content.map((data) => (
                        <div style={(data.title === tab) ? LeftSectionStyles.ItemsSelected : LeftSectionStyles.Items}
                             onClick={() => {
                                 (data.title === Content[3].title) ? setLogOut(!logOut) : setTab(data.title)
                             }}>
                            <img alt="Icon" style={LeftSectionStyles.Icon} src={data.Icon}/>
                            <div style={LeftSectionStyles.Content}>{data.title}</div>
                        </div>
                    ))
                }
            </div>
        </div>
    );
}

export default LeftSection;