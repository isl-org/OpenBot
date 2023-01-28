import React, {useState} from 'react'

export const StoreContext = React.createContext(null)

export default ({children}) => {
    const [projectName, setProjectName] = useState("Project1");

    const store = {
        projectName, setProjectName

    }

    return <StoreContext.Provider value={store}>{children}</StoreContext.Provider>
}