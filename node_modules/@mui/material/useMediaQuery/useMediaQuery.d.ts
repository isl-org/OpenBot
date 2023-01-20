/**
 * @deprecated Not used internally. Use `MediaQueryListEvent` from lib.dom.d.ts instead.
 */
export interface MuiMediaQueryListEvent {
    matches: boolean;
}
/**
 * @deprecated Not used internally. Use `MediaQueryList` from lib.dom.d.ts instead.
 */
export interface MuiMediaQueryList {
    matches: boolean;
    addListener: (listener: MuiMediaQueryListListener) => void;
    removeListener: (listener: MuiMediaQueryListListener) => void;
}
/**
 * @deprecated Not used internally. Use `(event: MediaQueryListEvent) => void` instead.
 */
export type MuiMediaQueryListListener = (event: MuiMediaQueryListEvent) => void;
export interface Options {
    defaultMatches?: boolean;
    matchMedia?: typeof window.matchMedia;
    /**
     * This option is kept for backwards compatibility and has no longer any effect.
     * It's previous behavior is now handled automatically.
     */
    noSsr?: boolean;
    ssrMatchMedia?: (query: string) => {
        matches: boolean;
    };
}
export default function useMediaQuery<Theme = unknown>(queryInput: string | ((theme: Theme) => string), options?: Options): boolean;
