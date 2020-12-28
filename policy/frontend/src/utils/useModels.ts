import {Hyperparametes} from 'src/utils/useProgress';
import {useRpc} from 'src/utils/useRpc';

export interface Model {
    name: string;
    path: string;
    params: Hyperparametes;
    logs: any[];
}

const defaultValue: Model[] = [];

export function useModels() {
    return useRpc(defaultValue, 'getModels')
}
