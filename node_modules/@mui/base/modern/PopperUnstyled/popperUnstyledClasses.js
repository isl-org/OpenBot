import generateUtilityClass from '../generateUtilityClass';
import generateUtilityClasses from '../generateUtilityClasses';
export function getPopperUnstyledUtilityClass(slot) {
  return generateUtilityClass('MuiPopperUnstyled', slot);
}
const popperUnstyledClasses = generateUtilityClasses('MuiPopperUnstyled', ['root']);
export default popperUnstyledClasses;