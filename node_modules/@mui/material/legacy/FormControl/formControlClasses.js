import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getFormControlUtilityClasses(slot) {
  return generateUtilityClass('MuiFormControl', slot);
}
var formControlClasses = generateUtilityClasses('MuiFormControl', ['root', 'marginNone', 'marginNormal', 'marginDense', 'fullWidth', 'disabled']);
export default formControlClasses;