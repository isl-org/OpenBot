import generateUtilityClass from '../generateUtilityClass';
import generateUtilityClasses from '../generateUtilityClasses';
export function getFormControlUnstyledUtilityClass(slot) {
  return generateUtilityClass('MuiFormControl', slot);
}
var formControlUnstyledClasses = generateUtilityClasses('MuiFormControl', ['root', 'disabled', 'error', 'filled', 'focused', 'required']);
export default formControlUnstyledClasses;