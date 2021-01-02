const width = 100;

export function Direction({left, right}: { left: number; right: number }) {
    if (left === undefined && right === undefined) {
        return (
            <svg viewBox="0 0 50 50" width={width}>
                <text x={10} y={25}>N/A</text>
            </svg>
        );
    }
    if (!left && !right) {
        return (
            <svg viewBox="0 0 50 50" width={width}>
                <text x={10} y={25}>stop</text>
            </svg>
        );
    }
    return (
        <svg viewBox="0 0 200 200" width={width}>
            <defs>
                <marker id="arrowhead" markerWidth="10" markerHeight="7"
                        refX="0" refY="3.5" orient="auto">
                    <polygon points="0 0, 10 3.5, 0 7"/>
                </marker>
            </defs>
            <line
                x1={width}
                x2={width + (left - right) / 2}
                y1={width + (left + right) / 4}
                y2={width - (left + right) / 4}
                stroke="#000"
                strokeWidth={3}
                markerEnd="url(#arrowhead)"
            />
        </svg>
    );
}
