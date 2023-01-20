import * as React from 'react';
import { UseMenuItemParameters } from './useMenuItem.types';
export default function useMenuItem(props: UseMenuItemParameters): {
    getRootProps: (other?: Record<string, any>) => {
        role: string;
        'aria-disabled'?: (boolean | "false" | "true") | undefined;
        disabled?: boolean | undefined;
        tabIndex?: number | undefined;
        type?: "button" | "reset" | "submit" | undefined;
        onBlur: React.FocusEventHandler<Element>;
        onFocus: React.FocusEventHandler<Element>;
        onKeyDown: React.KeyboardEventHandler<Element>;
        onKeyUp: React.KeyboardEventHandler<Element>;
        onMouseDown: React.MouseEventHandler<Element>;
        onMouseLeave: React.MouseEventHandler<Element>;
        ref: React.Ref<any>;
    };
    disabled: boolean;
    focusVisible: boolean;
} | {
    getRootProps: (other?: Record<string, any>) => {
        tabIndex: any;
        id: any;
        role: string;
        'aria-disabled'?: (boolean | "false" | "true") | undefined;
        disabled?: boolean | undefined;
        type?: "button" | "reset" | "submit" | undefined;
        onBlur: React.FocusEventHandler<Element>;
        onFocus: React.FocusEventHandler<Element>;
        onKeyDown: React.KeyboardEventHandler<Element>;
        onKeyUp: React.KeyboardEventHandler<Element>;
        onMouseDown: React.MouseEventHandler<Element>;
        onMouseLeave: React.MouseEventHandler<Element>;
        ref: React.Ref<any>;
    };
    disabled: boolean;
    focusVisible: boolean;
};
