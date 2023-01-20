import { Breakpoints, Breakpoint } from '../createTheme/createBreakpoints';
import { Spacing } from '../createTheme/createSpacing';
import { ResponsiveStyleValue } from '../styleFunctionSx';
import { GridDirection, GridOwnerState } from './GridProps';
interface Props {
    theme: {
        breakpoints: Breakpoints;
        spacing?: Spacing;
    };
    ownerState: GridOwnerState & {
        parentDisableEqualOverflow?: boolean;
    };
}
interface Iterator<T> {
    (appendStyle: (responsiveStyles: Record<string, any>, style: object) => void, value: T): void;
}
export declare const filterBreakpointKeys: (breakpointsKeys: Breakpoint[], responsiveKeys: string[]) => Breakpoint[];
export declare const traverseBreakpoints: <T = unknown>(breakpoints: Breakpoints, responsive: Record<string, any> | T | T[] | undefined, iterator: Iterator<T>) => void;
export declare const generateGridSizeStyles: ({ theme, ownerState }: Props) => {};
export declare const generateGridOffsetStyles: ({ theme, ownerState }: Props) => {};
export declare const generateGridColumnsStyles: ({ theme, ownerState }: Props) => {};
export declare const generateGridRowSpacingStyles: ({ theme, ownerState }: Props) => {};
export declare const generateGridColumnSpacingStyles: ({ theme, ownerState }: Props) => {};
export declare const generateGridDirectionStyles: ({ theme, ownerState }: Props) => {};
export declare const generateGridStyles: ({ ownerState }: Props) => {};
export declare const generateSizeClassNames: (gridSize: GridOwnerState['gridSize']) => string[];
export declare const generateSpacingClassNames: (spacing: GridOwnerState['spacing'], smallestBreakpoint?: string) => string[];
export declare const generateDirectionClasses: (direction: ResponsiveStyleValue<GridDirection> | undefined) => string[];
export {};
