import {useEffect, useState} from 'react';
import {useToggle} from './useToggle';

export function useFetch<T>(defaultValue: T, input: RequestInfo, init?: RequestInit) {
    const [error, setError] = useState();
    const [pending, setPending] = useState(true);
    const [value, setValue] = useState(defaultValue);
    const [reloadValue, reload] = useToggle(false);
    useEffect(() => {
        setPending(true);
        let active = true;
        console.log('useFetch', input, init);
        fetch(input, init)
            .then(data => data.json())
            .then(data => {
                // simulate latency
                setTimeout(() => {
                    if (active) {
                        setPending(false);
                        setValue(data);
                        setError(undefined);
                    }
                }, 250)
            })
            .catch(err => {
                if (active) {
                    console.error(err);
                    setPending(false);
                    setError(err);
                }
            });
        return () => {
            active = false;
        };
    }, [input, init, reloadValue]);
    return {error, pending, value, reload};
}
