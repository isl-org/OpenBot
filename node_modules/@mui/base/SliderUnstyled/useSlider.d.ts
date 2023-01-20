import { Mark, UseSliderHiddenInputProps, UseSliderParameters, UseSliderRootSlotProps, UseSliderThumbSlotProps } from './useSlider.types';
import { EventHandlers } from '../utils';
export declare function valueToPercent(value: number, min: number, max: number): number;
export declare const Identity: (x: any) => any;
export default function useSlider(parameters: UseSliderParameters): {
    active: number;
    axis: "horizontal" | "vertical" | "horizontal-reverse";
    axisProps: {
        horizontal: {
            offset: (percent: number) => {
                left: string;
            };
            leap: (percent: number) => {
                width: string;
            };
        };
        'horizontal-reverse': {
            offset: (percent: number) => {
                right: string;
            };
            leap: (percent: number) => {
                width: string;
            };
        };
        vertical: {
            offset: (percent: number) => {
                bottom: string;
            };
            leap: (percent: number) => {
                height: string;
            };
        };
    };
    dragging: boolean;
    focusedThumbIndex: number;
    getHiddenInputProps: <TOther extends EventHandlers = {}>(otherHandlers?: TOther) => UseSliderHiddenInputProps<TOther>;
    getRootProps: <TOther_1 extends EventHandlers = {}>(otherHandlers?: TOther_1) => UseSliderRootSlotProps<TOther_1>;
    getThumbProps: <TOther_2 extends EventHandlers = {}>(otherHandlers?: TOther_2) => UseSliderThumbSlotProps<TOther_2>;
    marks: Mark[];
    open: number;
    range: boolean;
    trackLeap: number;
    trackOffset: number;
    values: number[];
};
