import React, {createContext, useState} from 'react'

export const StoreContext = createContext(null)

export default ({children}) => {
    const [projectName, setProjectName] = useState("Project1");
    const [drawer,setDrawer] = useState(false);
    const [logOut, setLogOut] = useState(false);
    const [code,setCode] = useState("");
    const [generate,setGenerateCode] = useState(false);
    const [userData,setUserData] = useState(null);
    const store = {
        projectName, setProjectName,
        drawer,setDrawer,
        logOut,setLogOut,
        code,setCode,
        generate, setGenerateCode,
        userData,setUserData
    }
    return <StoreContext.Provider value={store}>{children}</StoreContext.Provider>
}
