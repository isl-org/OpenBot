import {useReducer} from 'react';

export function useToggle(def = false) {
    return useReducer(toggle, def);
}

function toggle(val: boolean) {
    return !val;
}
