import {styled} from 'goober';
import {useEffect, useRef, useState} from 'react';
import {useHotkeys} from 'react-hotkeys-hook';
import {useRouteMatch} from 'react-router-dom';
import {FlexboxGrid, Icon, IconButton, InputNumber, Loader, Message, Panel, SelectPicker, Slider} from 'rsuite';
import {Direction} from 'src/components/Direction';
import {formatTime} from 'src/utils/formatTime';
import {Session} from 'src/utils/useDatasets';
import {useModels} from 'src/utils/useModels';
import {useRpc} from 'src/utils/useRpc';
import {useToggle} from 'src/utils/useToggle';
import {jsonRpc} from 'src/utils/ws';

const defaultValue: Session | null = null;
const indicatorValues = [
    {label:'-1', value: '-1'},
    {label:'0', value: '0'},
    {label:'1', value: '1'},
];

export function SessionPage() {
    const match = useRouteMatch<any>();
    const info = useRpc(defaultValue, 'getSession', match.url);
    const session = info.value;
    return <>
        <h3>{match.params.path}</h3>
        <Panel shaded>
            {session ? <SessionComp session={session}/> : <Loader/>}
        </Panel>
    </>;

}

const PreviewCont = styled('div')`
    display: flex;
    gap: 10px;
    margin: 20px 0;
    > img {
        flex: 1;
        width: 100%;
        max-width: 600px;
        align-self: flex-start;
    }
    > div {
        vertical-align: top;
        text-align: center;
        width: 130px;
    }
`;

function SessionComp({session}: {session: Session}) {
    const max = session.ctrl.length - 1;
    const models = useModels();
    const [playing, togglePlaying] = useToggle(false);
    const [current, setCurrent] = useState(0);
    const [model, setModel] = useState(null);
    const [indicator, setIndicator] = useState(null);
    useAnimate(() => {
        if (playing) {
            setCurrent(c => c === max ? 0 : c + 1);
        }
    }, [playing, max]);
    useHotkeys('space', togglePlaying);
    useHotkeys('right', () => setCurrent(c => c === max ? 0 : c + 1), [max]);
    useHotkeys('left', () => setCurrent(c => c === 0 ? max : c - 1), [max]);

    const prediction = usePrediction(session.path, model, indicator, current);
    const [predLeft, predRight] = prediction.value[current] || [];
    const [img, left, right, ind] = session.ctrl[current];
    return (
        <>
            <FlexboxGrid>
                <FlexboxGrid.Item colspan={12}>
                    <div>Length: {formatTime(session.seconds)}</div>
                    <div>Frames: {session.ctrl.length}</div>
                    <div>Predictions: {prediction.value.length}</div>
                </FlexboxGrid.Item>
                <FlexboxGrid.Item colspan={12} style={{textAlign:'right'}}>
                    Model <SelectPicker
                        placeholder="Select a model to test"
                        data={models.value.map(m => ({label: m, value: m}))}
                        value={model}
                        onChange={setModel}
                        searchable={models.value.length > 10}
                        placement="bottomEnd"
                    />
                    <br/>
                    Indicator <SelectPicker
                        placeholder="Use recorded"
                        data={indicatorValues}
                        value={indicator}
                        onChange={setIndicator}
                        searchable={false}
                    />
                </FlexboxGrid.Item>
            </FlexboxGrid>

            <PreviewCont>
                <img src={`${session.path}/images/${img}`} alt=""/>
                <Panel header="Recorded" bordered>
                    <Direction left={+left} right={+right}/>
                    Indicator: {ind}
                </Panel>
                <Panel header="Model" bordered>
                    <Direction left={predLeft} right={predRight}/>
                    {prediction.pending > 0 && <Loader/>}
                </Panel>
            </PreviewCont>

            <FlexboxGrid>
                <IconButton icon={<Icon icon={playing ? 'pause' : 'arrow-right'}/>} onClick={togglePlaying}/>
                <Slider style={{flex: 1, margin: 14}} max={max} value={current} onChange={setCurrent}/>
                <InputNumber min={0} max={max} value={current} onChange={v => setCurrent(+v)} style={{maxWidth: 100}}/>
            </FlexboxGrid>

            <br/>
            <Message
                title="Hotkeys"
                description={
                    <div>
                        <div>Space: toggle playing</div>
                        <div>Arrow right: next frame</div>
                        <div>Arrow left: prev frame</div>
                    </div>
                }
            />
        </>
    );
}

const PREFETCH_SIZE = 50;

function usePrediction(path: string, model: string | null, indicator: string | null, current: number) {
    const [pending, setPending] = useState(0);
    const [value, setValue] = useState<any[]>([]);
    const valid = useRef<any>();
    if (!valid.current) {
        valid.current = [];
    }
    useEffect(() => {
        console.log("clear predictions");
        valid.current = [];
    }, [indicator, model, path])
    let batch = Math.floor(current / PREFETCH_SIZE);
    if (valid.current[batch]) {
        batch++;
    }
    useEffect(() => {
        if (valid.current[batch]) {
            return;
        }
        valid.current[batch] = true;
        setPending(c => c + 1);
        const start = batch * PREFETCH_SIZE;
        jsonRpc<any[]>('getPrediction', {path, model, indicator, start, end: start + PREFETCH_SIZE})
            .then(data => {
                setValue(v => {
                    const copy = v.slice();
                    copy[start] = null;
                    copy.splice(start, batch, ...data)
                    return copy;
                })
                setPending(c => c - 1);
            })
            .catch(() => {
                valid.current[batch] = false;
                setPending(c => c - 1);
            });
    }, [batch, indicator, model, path])

    return {value, pending};
}

function useAnimate(update: () => void, deps: any[]) {
    useEffect(() => {
        let active = true;
        let start = performance.now();
        const animate = () => {
            if (performance.now() - start > 25) {
                update();
                start = performance.now();
            }
            if (active) {
                requestAnimationFrame(animate);
            }
        };
        requestAnimationFrame(animate);
        return () => {
            active = false;
        };
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, deps);
}
