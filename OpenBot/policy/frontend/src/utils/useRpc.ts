import {useEffect, useMemo, useState} from 'react';
import {useToggle} from './useToggle';
import {jsonRpc} from './ws';

export function useRpc<T>(defaultValue: T, method: string, params?: any) {
    const [error, setError] = useState();
    const [pending, setPending] = useState(true);
    const [value, setValue] = useState(defaultValue);
    const [reloadValue, reload] = useToggle(false);
    // eslint-disable-next-line react-hooks/exhaustive-deps
    const paramsMemo = useMemo(() => params, [JSON.stringify(params)]);
    useEffect(() => {
        setPending(true);
        let active = true;
        console.log('useRpc', method, paramsMemo);
        jsonRpc<T>(method, paramsMemo)
            .then(data => {
                if (active) {
                    setValue(data);
                    setError(undefined);
                    setPending(false);
                }
            })
            .catch(err => {
                if (active) {
                    console.error(err);
                    setError(err);
                    setPending(false);
                }
            });
        return () => {
            active = false;
        };
    }, [method, paramsMemo, reloadValue]);
    return {error, pending, value, reload};
}
