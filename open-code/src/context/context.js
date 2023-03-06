import React, {createContext, useState} from 'react'
import {getCurrentProject} from "../services/workspace";
import {localStorageKeys} from "../utils/constants";

export const StoreContext = createContext(null)

export default ({children}) => {
    let savedProjectName = null
    let savedProjectId = null
    let savedProjectXml = null
    if (localStorage.getItem(localStorageKeys.currentProject)) {
        savedProjectName = getCurrentProject().projectName
        savedProjectId = getCurrentProject().id
        savedProjectXml = getCurrentProject().xmlValue
    }
    const [projectName, setProjectName] = useState(savedProjectName ? savedProjectName : undefined);
    const [drawer, setDrawer] = useState(false);
    const [logOut, setLogOut] = useState(false);
    const [code, setCode] = useState("");
    const [generate, setGenerateCode] = useState(false);
    const [currentProjectXml, setCurrentProjectXml] = useState(savedProjectXml);
    const [currentProjectId, setCurrentProjectId] = useState(savedProjectId);
    const store = {
        projectName, setProjectName,
        drawer, setDrawer,
        logOut, setLogOut,
        code, setCode,
        generate, setGenerateCode,
        currentProjectXml, setCurrentProjectXml,
        currentProjectId, setCurrentProjectId,
    }
    return <StoreContext.Provider value={store}>{children}</StoreContext.Provider>
}
