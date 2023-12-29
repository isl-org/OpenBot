import {differenceInMinutes} from 'date-fns';
import NoSleep from 'nosleep.js';
import {useEffect} from 'react';
import {Button, Panel, Progress} from 'rsuite';
import {ButtonBar} from 'src/components/ButtonBar';
import {GridView} from 'src/components/GridView';
import {HyperparametersForm} from 'src/components/HyperparametersForm';
import {Plot} from 'src/components/Plot';
import {ProgressState, useProgress} from 'src/utils/useProgress';
import {jsonRpc} from 'src/utils/ws';

const noSleep = new NoSleep();

export function TrainPage() {
    const [state, clear] = useProgress();

    return (
        <div className="trainPage">
            <h3>Model training</h3>
            {!state.status ? (
                <Panel shaded header="Hyperparameters">
                    <HyperparametersForm/>
                </Panel>
            ): (
                <TrainProgress state={state} clear={clear}/>
            )}
        </div>
    );
}

function TrainProgress({state, clear}: { state: ProgressState, clear: () => any }) {
    const active = state.status === 'active';
    useEffect(() => {
        if (active) {
            noSleep.enable();
        } else {
            noSleep.disable();
        }
    }, [active]);
    const now = new Date();
    const end = predictEndDate(state, now);
    return <>
        <Panel shaded header="Progress">
            <p>Current step: {state.message}</p>
            <div>
                Current epoch:
                <Progress.Line percent={state.epoch} status={state.status}/>
            </div>
            <div>
                Training:
                <Progress.Line percent={state.percent} status={state.status}/>
            </div>
            <div>Full time: {differenceInMinutes(end, state.startTime)} minutes</div>
            <div>Elapsed time: {differenceInMinutes(now, state.startTime)} minutes</div>
            <div>Remaining time: {differenceInMinutes(end, now)} minutes</div>
            <ButtonBar>
                {active ? (
                    <Button onClick={() => jsonRpc('stop')}>Stop</Button>
                ): (
                    <Button onClick={clear}>Clear</Button>
                )}
            </ButtonBar>
        </Panel>
        <GridView>
            <Plot logs={state.logs} metric="mean_absolute_error"/>
            <Plot logs={state.logs} metric="direction_metric"/>
            <Plot logs={state.logs} metric="angle_metric"/>
            <Plot logs={state.logs} metric="loss"/>
        </GridView>
        {state.error && (
            <Panel shaded header="Error">
                {state.error}
            </Panel>
        )}
        {!!state.rnd && (
            <Panel bodyFill shaded header="Train preview" collapsible defaultExpanded>
                <img alt="preview thumbnails" src={`/models/train_preview.png?rnd=${state.rnd}`}/>
            </Panel>
        )}
        {!!state.model && (
            <Panel bodyFill shaded header="Model preview" collapsible defaultExpanded>
                <img alt="preview thumbnails" src={`/models/${state.model}/model.png?rnd=${state.rnd}`}/>
            </Panel>
        )}
        {state.status === 'success' && (
            <Panel bodyFill shaded header="Test preview" collapsible defaultExpanded>
                <img alt="preview thumbnails" src={`/models/${state.model}/logs/test_preview.png?rnd=${state.rnd}`}/>
            </Panel>
        )}
    </>
}

function predictEndDate(state: ProgressState, now: Date) {
    const start = state.startTime.getTime();
    const elapsed = now.getTime() - start;
    const fullTime = elapsed / state.percent * 100;

    return new Date(start + fullTime);
}
