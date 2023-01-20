import generateUtilityClass from '../generateUtilityClass';
import generateUtilityClasses from '../generateUtilityClasses';
export function getMenuItemUnstyledUtilityClass(slot) {
  return generateUtilityClass('MuiMenuItem', slot);
}
const menuItemUnstyledClasses = generateUtilityClasses('MuiMenuItem', ['root', 'disabled', 'focusVisible']);
export default menuItemUnstyledClasses;