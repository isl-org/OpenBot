import generateUtilityClass from '../generateUtilityClass';
import generateUtilityClasses from '../generateUtilityClasses';
export function getSelectUnstyledUtilityClass(slot) {
  return generateUtilityClass('MuiSelect', slot);
}
var selectUnstyledClasses = generateUtilityClasses('MuiSelect', ['root', 'button', 'listbox', 'popper', 'active', 'expanded', 'disabled', 'focusVisible']);
export default selectUnstyledClasses;