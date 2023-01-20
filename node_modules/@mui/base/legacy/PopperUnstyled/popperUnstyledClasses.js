import generateUtilityClass from '../generateUtilityClass';
import generateUtilityClasses from '../generateUtilityClasses';
export function getPopperUnstyledUtilityClass(slot) {
  return generateUtilityClass('MuiPopperUnstyled', slot);
}
var popperUnstyledClasses = generateUtilityClasses('MuiPopperUnstyled', ['root']);
export default popperUnstyledClasses;