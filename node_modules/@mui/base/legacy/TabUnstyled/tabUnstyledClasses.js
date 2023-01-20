import generateUtilityClass from '../generateUtilityClass';
import generateUtilityClasses from '../generateUtilityClasses';
export function getTabUnstyledUtilityClass(slot) {
  return generateUtilityClass('MuiTab', slot);
}
var tabUnstyledClasses = generateUtilityClasses('MuiTab', ['root', 'selected', 'disabled']);
export default tabUnstyledClasses;