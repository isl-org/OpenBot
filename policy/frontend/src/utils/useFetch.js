import {useEffect, useState} from 'react';

export function useFetch(defaultValue, input, init) {
    const [error, setError] = useState();
    const [pending, setPending] = useState(true);
    const [value, setValue] = useState(defaultValue);
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
    }, [input, init]);
    return {error, pending, value};
}
