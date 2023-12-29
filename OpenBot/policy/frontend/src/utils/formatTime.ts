export function formatTime(seconds: number) {
    if (seconds < 120) {
        return `${seconds} seconds`;
    }
    const minutes = Math.round(seconds / 6) / 10;
    return `${minutes} minutes`
}
