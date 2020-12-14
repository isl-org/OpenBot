import {useFetch} from 'src/utils/useFetch';

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
    train: [] as Dataset[],
    test: [] as Dataset[],
};

export function useDatasets() {
    return useFetch(defaultValue, '/datasets')
}
