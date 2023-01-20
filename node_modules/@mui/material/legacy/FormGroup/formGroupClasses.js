import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getFormGroupUtilityClass(slot) {
  return generateUtilityClass('MuiFormGroup', slot);
}
var formGroupClasses = generateUtilityClasses('MuiFormGroup', ['root', 'row', 'error']);
export default formGroupClasses;