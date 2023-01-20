import { OverridableComponent } from '@mui/types';
import { ButtonUnstyledOwnProps, ButtonUnstyledTypeMap } from './ButtonUnstyled.types';
export interface ButtonUnstyledOwnerState extends ButtonUnstyledOwnProps {
    focusVisible: boolean;
    active: boolean;
}
/**
 * The foundation for building custom-styled buttons.
 *
 * Demos:
 *
 * - [Unstyled Button](https://mui.com/base/react-button/)
 *
 * API:
 *
 * - [ButtonUnstyled API](https://mui.com/base/api/button-unstyled/)
 */
declare const ButtonUnstyled: OverridableComponent<ButtonUnstyledTypeMap<{}, "button">>;
export default ButtonUnstyled;
