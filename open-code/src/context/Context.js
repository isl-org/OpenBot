import React, {useState} from 'react'

export const StoreContext = React.createContext(null)

export default ({children}) => {
    const [projectName, setProjectName] = useState("Project1");
    const [drawer,setDrawer] = useState(false);
    const [workspaceWidth,setWorkspaceWidth] = useState(100);

    const store = {
        projectName, setProjectName,
        drawer,setDrawer,
        workspaceWidth,setWorkspaceWidth

    }

    return <StoreContext.Provider value={store}>{children}</StoreContext.Provider>
}
