import {PlaygroundNavbarStyles} from "./styles";
import React, {useContext, useState} from "react";
import {useNavigate} from "react-router-dom";
import icon from "../../assets/images/icon/open-bot-logo.png";
import info from "../../assets/images/icon/info.png";
import moon from "../../assets/images/icon/whiteMode/white-mode-icon.png";
import line from "../../assets/images/line.png";
import downArrow from "../../assets/images/icon/down-arrow.png";
import UpArrow from "../../assets/images/icon/up-arrow.png";
import Edit from "../../assets/images/icon/edit.png";
import trash from "../../assets/images/icon/trash.png";
import styles from "./workSpace.module.css";
import {Popper} from "@mui/material";
import {StoreContext} from "../../context/context"
import DeleteModel from "../../pages/profile/deleteModel";
import {QrDrawer} from "../drower/drower";

export const WorkspaceNavbar = () => {
    const openHomepage = () => {
        let path = `/`;
        navigate(path);
    }

    const [anchorEl, setAnchorEl] = useState(null);
    const {projectName, setProjectName} = useContext(StoreContext)
    const [deleteProject, setDeleteProject] = useState(false)
    const handleClick = (event) => {
        setAnchorEl(anchorEl ? null : event.currentTarget);
    };

    const open = Boolean(anchorEl);
    const id = open ? 'simple-popper' : undefined;

    let navigate = useNavigate();

    const handleDelete = () => {
        setDeleteProject(true)
    }


    return (
        <>
            { deleteProject && <DeleteModel setDeleteProject={setDeleteProject}/>}
            <div style={PlaygroundNavbarStyles.navbarDiv}>
                <div style={PlaygroundNavbarStyles.navbarTitleDiv}>
                    <img style={{...PlaygroundNavbarStyles.mainIcon, ...PlaygroundNavbarStyles.iconMargin}} src={icon}
                         onClick={() => {
                             openHomepage()
                         }}

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
                                <div className={styles.item} onClick={handleClick}>
                                    <img alt="Icon" className={styles.icon} src={Edit}/>
                                    <div>Rename</div>
                                </div>
                                <div className={styles.item} onClick={handleDelete}>
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
                    <QrDrawer/>
                </div>
            </div> }
        </>
    );
}
