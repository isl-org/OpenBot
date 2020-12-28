import {useCallback, useEffect, useReducer} from 'react';
import {subscribe} from 'src/utils/ws';

export interface Hyperparametes {
    MODEL: string;
    TRAIN_BATCH_SIZE: number;
    TEST_BATCH_SIZE: number;
    LEARNING_RATE: number;
    NUM_EPOCHS: number;
    BATCH_NORM: boolean;
    FLIP_AUG: boolean;
    CMD_AUG: boolean;
    USE_LAST: boolean;
}

export interface ProgressState {
    status: 'success' | 'fail' | 'active' | undefined;
    epoch: number;
    percent: number;
    logs: any[];
    error?: string;
    model?: string;
    params?: Hyperparametes;
    rnd?: number;
}

const defaultState: ProgressState = {
    status: undefined,
    epoch: 0,
    percent: 0,
    logs: [],
}

export function useProgress() {
    const [state, dispatch] = useReducer(progressReducer, defaultState);

    useEffect(() => {
        return subscribe('training', dispatch);
    }, [])

    const clear = useCallback(() => {
        dispatch({event: 'clear'})
    }, [])

    return [state, clear] as const;
}

function progressReducer(state: ProgressState, msg: any): ProgressState {
    switch (msg.event) {
        case 'started':
            return {
                status: 'active',
                epoch: 0,
                percent: 0,
                logs: [],
            }
        case 'preview':
            return {
                ...state,
                rnd: Date.now(),
            };
        case 'logs':
            return {
                ...state,
                logs: [...state.logs, msg.payload],
            };
        case 'progress':
            return {
                ...state,
                status: 'active',
                epoch: Math.round(msg.payload.epoch * 100),
                percent: Math.round(msg.payload.train * 100),
            };
        case 'cancelled':
        case 'failed':
            return {
                ...state,
                status: 'fail',
                error: msg.payload,
            };
        case 'done':
            return {
                ...state,
                rnd: Date.now(),
                status: 'success',
                model: msg.payload.model,
            };
        case 'clear':
            return {
                status: undefined,
                epoch: 0,
                percent: 0,
                logs: [],
            };
    }
    return state;
}
