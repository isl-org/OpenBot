import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getFabUtilityClass(slot) {
  return generateUtilityClass('MuiFab', slot);
}
const fabClasses = generateUtilityClasses('MuiFab', ['root', 'primary', 'secondary', 'extended', 'circular', 'focusVisible', 'disabled', 'colorInherit', 'sizeSmall', 'sizeMedium', 'sizeLarge', 'info', 'error', 'warning', 'success']);
export default fabClasses;