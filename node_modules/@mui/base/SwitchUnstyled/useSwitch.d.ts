import * as React from 'react';
import { UseSwitchInputSlotProps, UseSwitchParameters } from './useSwitch.types';
/**
 * The basic building block for creating custom switches.
 *
 * Demos:
 *
 * - [Switches](https://mui.com/components/switches/)
 */
export default function useSwitch(props: UseSwitchParameters): {
    checked: boolean;
    disabled: boolean;
    focusVisible: boolean;
    getInputProps: (otherProps?: React.HTMLAttributes<HTMLInputElement>) => UseSwitchInputSlotProps;
    readOnly: boolean;
};
