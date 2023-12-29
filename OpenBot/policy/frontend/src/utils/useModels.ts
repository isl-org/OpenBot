import {useEffect} from 'react';
import {Hyperparametes} from 'src/utils/useProgress';
import {useRpc} from 'src/utils/useRpc';
import {subscribe} from 'src/utils/ws';

export interface Model {
    name: string;
    path: string;
    params: Hyperparametes;
    logs: any[];
}
export interface ModelFile {
    name: string;
    mtime: number;
}

const defaultValue: Model | null = null;

export function useModels() {
    return useRpc([] as string[], 'getModels')
}

export function useModel(name: string) {
    return useRpc(defaultValue, 'getModelInfo', name)
}

export function useModelFiles() {
    const {error, pending, value, reload} = useRpc([] as ModelFile[], 'getModelFiles');
    useEffect(() => subscribe('modelFile', reload), [reload])
    return {error, pending, value, reload};
}
