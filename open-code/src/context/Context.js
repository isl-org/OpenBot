import React, {createContext, useState} from 'react'

export const StoreContext = createContext(null)

export default ({children}) => {
    const [projectName, setProjectName] = useState("Project1");
    const [drawer,setDrawer] = useState(false);
    const [workspaceWidth,setWorkspaceWidth] = useState(100);
    const [logOut, setLogOut] = useState(false);

    const store = {

        projectName, setProjectName,
        drawer,setDrawer,
        workspaceWidth,setWorkspaceWidth,
        logOut,setLogOut

    }

    return <StoreContext.Provider value={store}>{children}</StoreContext.Provider>
}
