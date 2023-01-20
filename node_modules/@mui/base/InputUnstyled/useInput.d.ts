import * as React from 'react';
import { UseInputInputSlotProps, UseInputParameters, UseInputRootSlotProps } from './useInput.types';
export default function useInput(parameters: UseInputParameters): {
    disabled: boolean;
    error: boolean;
    focused: boolean;
    formControlContext: {
        disabled?: boolean | undefined;
        required?: boolean | undefined;
        value?: unknown;
        onChange?: React.ChangeEventHandler<import("../FormControlUnstyled/FormControlUnstyled.types").NativeFormControlElement> | undefined;
        error?: boolean | undefined;
        filled: boolean;
        focused: boolean;
        onBlur: () => void;
        onFocus: () => void;
    } | undefined;
    getInputProps: <TOther extends Record<string, any> = {}>(externalProps?: TOther) => UseInputInputSlotProps<TOther>;
    getRootProps: <TOther_1 extends Record<string, any> = {}>(externalProps?: TOther_1) => UseInputRootSlotProps<TOther_1>;
    required: boolean;
    value: unknown;
};
