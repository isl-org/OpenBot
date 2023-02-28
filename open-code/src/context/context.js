import React, {createContext, useState} from 'react'
export const StoreContext = createContext(null)

export default ({children}) => {
    let savedProjectName = null
    if (localStorage.getItem("CurrentProject")) {
        savedProjectName = Object.keys(JSON.parse(localStorage.getItem("CurrentProject"))).toString()
    }
    const [projectName, setProjectName] = useState(savedProjectName ? savedProjectName : undefined);
    const [drawer, setDrawer] = useState(false);
    const [logOut, setLogOut] = useState(false);
    const [code, setCode] = useState("");
    const [generate, setGenerateCode] = useState(false);
    const store = {
        projectName, setProjectName,
        drawer, setDrawer,
        logOut, setLogOut,
        code, setCode,
        generate, setGenerateCode,
    }
    return <StoreContext.Provider value={store}>{children}</StoreContext.Provider>
}

