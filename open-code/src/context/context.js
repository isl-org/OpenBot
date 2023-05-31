import React, {createContext, useState} from 'react'
import {getCurrentProject} from "../services/workspace";
import {Constants, localStorageKeys} from "../utils/constants";

export const StoreContext = createContext(null)

export default ({children, isOnline, user, setUser}) => {
    let savedProjectName = null
    let savedProjectId = null
    let savedProjectXml = null
    let savedFileId = null
    let savedFolderId = null

    if (localStorage.getItem(localStorageKeys.currentProject)) {
        savedProjectName = getCurrentProject().projectName
        savedProjectId = getCurrentProject().id
        savedProjectXml = getCurrentProject().xmlValue
        savedFileId = getCurrentProject()?.fileId
        savedFolderId = getCurrentProject().folderId
    }

    const [projectName, setProjectName] = useState(savedProjectName ? savedProjectName : undefined);
    const [drawer, setDrawer] = useState(false);
    const [logOut, setLogOut] = useState(false);
    const [code, setCode] = useState({});
    const [generate, setGenerateCode] = useState(false);
    const [currentProjectXml, setCurrentProjectXml] = useState(savedProjectXml);
    const [fileId, setFileId] = useState(savedFileId);
    const [folderId, setFolderId] = useState(savedFolderId);
    const [category, setCategory] = useState("");
    const [workspace, setWorkspace] = useState();
    const [isError, setIsError] = useState(false);

    const store = {
        projectName, setProjectName,
        drawer, setDrawer,
        logOut, setLogOut,
        category, setCategory,
        code, setCode,
        generate, setGenerateCode,
        currentProjectXml, setCurrentProjectXml,
        fileId, setFileId,
        folderId, setFolderId,
        user, setUser,
        workspace, setWorkspace,
        isError, setIsError, isOnline
    }
    return <StoreContext.Provider value={store}>{children}</StoreContext.Provider>
}
