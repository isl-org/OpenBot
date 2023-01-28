import {PlaygroundNavbarStyles} from "./Styles";
import React, {useContext, useState} from "react";
import { useNavigate } from "react-router-dom";
import icon from "../../assets/images/ICON.png";
import info from "../../assets/images/info.png";
import moon from "../../assets/images/moon.png";
import line from "../../assets/images/Line.png";
import downArrow from "../../assets/images/down-arrow.png";
import UpArrow from "../../assets/images/DownArrow.png";
import Edit from "../../assets/images/edit.png";
import trash from "../../assets/images/trash.png";
import styles from "./workSpace.module.css";
import {Popper} from "@mui/material";
import {StoreContext} from "../../context/Context"


export const WorkspaceNavbar = () => {
    const [anchorEl, setAnchorEl] = useState(null);
    const {projectName,setProjectName} = useContext(StoreContext)
    const handleClick = (event) => {
        setAnchorEl(anchorEl ? null : event.currentTarget);
    };

    const open = Boolean(anchorEl);
    const id = open ? 'simple-popper' : undefined;

    let navigate = useNavigate();
    const handleDeleteProject = () =>{
        let path = `/`;
        navigate(path);
    }

    return (
        <>
            <div style={PlaygroundNavbarStyles.navbarDiv}>
                <div style={PlaygroundNavbarStyles.navbarTitleDiv}>
                    <img style={{...PlaygroundNavbarStyles.mainIcon, ...PlaygroundNavbarStyles.iconMargin}} src={icon}
                         alt={icon}/>
                    <span
                        style={{...PlaygroundNavbarStyles.mainTitle, ...PlaygroundNavbarStyles.iconMargin}}>OpenCode</span>
                </div>
                {!anchorEl ?
                    <div style={PlaygroundNavbarStyles.playgroundName} onClick={handleClick}>
                    <span
                        style={{...PlaygroundNavbarStyles.mainTitle, ...PlaygroundNavbarStyles.arrowMargin}}>{projectName}</span>
                        <img src={downArrow}
                             style={{...PlaygroundNavbarStyles.infoIcon, ...PlaygroundNavbarStyles.arrowMargin}}
                             alt={icon}/>
                    </div>
                    :
                    <>
                        <div style={PlaygroundNavbarStyles.playgroundName}>
                            <input type="text" className={styles.Edit}
                                   id="userEdit"
                                   onChange={(e) => setProjectName(e.target.value)}
                                   style={{width: `${projectName.length}ch`}}
                                   value={projectName}
                            />
                            <img src={UpArrow}
                                 style={{...PlaygroundNavbarStyles.infoIcon, ...PlaygroundNavbarStyles.arrowMargin}}
                                 onClick={handleClick} alt={icon}/>
                        </div>
                        <Popper id={id} open={open} anchorEl={anchorEl}>
                            <div className={styles.option}>
                                <div className={styles.item}  onClick={handleClick} >
                                    <img alt="Icon" className={styles.icon} src={Edit}/>
                                    <div >Rename</div>
                                </div>
                                <div className={styles.item} onClick={handleDeleteProject} >
                                    <img alt="Icon" className={styles.icon} src={trash}/>
                                    <div> Delete File</div>
                                </div>
                            </div>
                        </Popper>
                    </>
                }
                <div style={PlaygroundNavbarStyles.navbarIconDiv}>
                    <img src={info} style={{...PlaygroundNavbarStyles.infoIcon, ...PlaygroundNavbarStyles.iconMargin}}
                         alt={icon}/>
                    <img src={moon} style={{...PlaygroundNavbarStyles.moonIcon, ...PlaygroundNavbarStyles.iconMargin}}
                         alt={icon}/>
                    <img src={line} style={{...PlaygroundNavbarStyles.lineIcon, ...PlaygroundNavbarStyles.iconMargin}}
                         alt={icon}/>
                    <button style={{...PlaygroundNavbarStyles.buttonIcon, ...PlaygroundNavbarStyles.iconMargin}}><span>Sign in</span>
                    </button>
                </div>
            </div>
        </>
    );
}