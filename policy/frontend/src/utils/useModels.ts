import {useRpc} from 'src/utils/useRpc';

export interface Model {
    name: string;
}

const defaultValue: Model[] = [];

export function useModels() {
    return useRpc(defaultValue, 'getModels')
}
