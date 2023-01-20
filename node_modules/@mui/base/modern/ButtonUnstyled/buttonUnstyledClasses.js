import generateUtilityClass from '../generateUtilityClass';
import generateUtilityClasses from '../generateUtilityClasses';
export function getButtonUnstyledUtilityClass(slot) {
  return generateUtilityClass('MuiButton', slot);
}
const buttonUnstyledClasses = generateUtilityClasses('MuiButton', ['root', 'active', 'disabled', 'focusVisible']);
export default buttonUnstyledClasses;