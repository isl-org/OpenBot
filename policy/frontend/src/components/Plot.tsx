import {ChartData, ChartOptions} from 'chart.js';
import {Line} from 'react-chartjs-2';
import {Panel} from 'rsuite';

const colors = ['blue', 'red'];

const options: ChartOptions = {
    scales: {
        xAxes: [{
            scaleLabel: {
                display: true,
                labelString: 'Epoch',
            },
        }],
    },
};

export function Plot(props: {logs: any[], metric: string}) {
    return (
        <Panel bodyFill shaded>
            <Line
                data={logToData(props.logs, [props.metric, `val_${props.metric}`])}
                options={options}
            />
        </Panel>
    );
}

function logToData(logs: any[], metrics: string[]): ChartData {
    return {
        labels: Array(Math.max(logs.length, 10)).fill(0).map((v, i) => `${i}`),
        datasets: metrics.map((metric, i) => ({
            borderColor: colors[i],
            borderWidth: 2,
            fill: false,
            label: metric,
            data: logs.map(l => l[metric]),
        })),
    };
}
