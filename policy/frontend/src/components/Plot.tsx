import {ChartData} from 'chart.js';
import {Line} from 'react-chartjs-2';
import {Panel} from 'rsuite';

const colors = ['blue', 'red'];

export function Plot(props: {logs: any[], metric: string}) {
    return (
        <Panel bodyFill shaded>
            <Line data={logToData(props.logs, [props.metric, `val_${props.metric}`])}/>
        </Panel>
    );
}

function logToData(logs: any[], metrics: string[]): ChartData {
    return {
        labels: Array(10).fill(0).map((v, i) => `${i + 1}`),
        datasets: metrics.map((metric, i) => ({
            borderColor: colors[i],
            borderWidth: 2,
            fill: false,
            label: metric,
            data: logs.map(l => l[metric]),
        })),
    };
}
