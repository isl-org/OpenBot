import generateUtilityClass from '../generateUtilityClass';
import generateUtilityClasses from '../generateUtilityClasses';
export function getTabUnstyledUtilityClass(slot) {
  return generateUtilityClass('MuiTab', slot);
}
const tabUnstyledClasses = generateUtilityClasses('MuiTab', ['root', 'selected', 'disabled']);
export default tabUnstyledClasses;