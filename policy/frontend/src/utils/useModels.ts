import {Hyperparametes} from 'src/utils/useProgress';
import {useRpc} from 'src/utils/useRpc';

export interface Model {
    name: string;
    path: string;
    params: Hyperparametes;
    logs: any[];
}

const defaultValue: Model | null = null;

export function useModels() {
    return useRpc([] as string[], 'getModels')
}

export function useModel(name: string) {
    return useRpc(defaultValue, 'getModelInfo', name)
}
