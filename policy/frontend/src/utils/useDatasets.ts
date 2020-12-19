import {useRpc} from 'src/utils/useRpc';

export interface Session {
    name: string;
    path: string;
    frames: number;
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
    return useRpc(defaultValue, 'getDatasets')
}
