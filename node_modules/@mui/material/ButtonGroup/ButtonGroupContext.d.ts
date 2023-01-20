import * as React from 'react';
import type { ButtonGroupProps } from './ButtonGroup';
interface IButtonGroupContext {
    className?: string;
    color?: ButtonGroupProps['color'];
    disabled?: boolean;
    disableElevation?: boolean;
    disableFocusRipple?: boolean;
    disableRipple?: boolean;
    fullWidth?: boolean;
    size?: ButtonGroupProps['size'];
    variant?: ButtonGroupProps['variant'];
}
/**
 * @ignore - internal component.
 */
declare const ButtonGroupContext: React.Context<IButtonGroupContext>;
export default ButtonGroupContext;
