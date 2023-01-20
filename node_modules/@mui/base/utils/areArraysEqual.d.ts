type ItemComparer<T> = (a: T, b: T) => boolean;
export default function areArraysEqual<T>(array1: T[], array2: T[], itemComparer?: ItemComparer<T>): boolean;
export {};
