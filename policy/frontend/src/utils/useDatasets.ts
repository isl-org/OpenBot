import {useEffect} from 'react';
import {useRpc} from 'src/utils/useRpc';
import {subscribe} from 'src/utils/ws';

export interface Session {
    name: string;
    path: string;
    seconds: number;
    ctrl: string[][];
    error?: string;
}

export interface Dataset {
    name: string;
    path: string;
    sessions: Session[];
}

const defaultValue = {
    train: [{
        name: 'loading...',
        path: '',
        sessions: [] as Session[],
    }] as Dataset[],
    test: [{
        name: 'loading...',
        path: '',
        sessions: [] as Session[],
    }] as Dataset[],
};

export function useDatasets() {
    const {error, pending, value, reload} = useRpc(defaultValue, 'getDatasets');
    useEffect(() => subscribe('dataset', reload), [reload])
    return {error, pending, value, reload};
}

const emptyList = [] as Session[];

export function useDataset(path: string) {
    const {error, pending, value, reload} = useRpc(emptyList, 'listDir', {path});
    useEffect(() => subscribe('session', reload), [reload])
    return {error, pending, value, reload};
}
