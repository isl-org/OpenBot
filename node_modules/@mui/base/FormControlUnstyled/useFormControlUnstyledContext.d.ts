import * as React from 'react';
export default function useFormControlUnstyledContext(): {
    disabled?: boolean | undefined;
    required?: boolean | undefined;
    value?: unknown;
    onChange?: React.ChangeEventHandler<import("./FormControlUnstyled.types").NativeFormControlElement> | undefined;
    error?: boolean | undefined;
    filled: boolean;
    focused: boolean;
    onBlur: () => void;
    onFocus: () => void;
} | undefined;
