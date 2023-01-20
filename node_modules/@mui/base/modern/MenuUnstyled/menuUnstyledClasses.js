import generateUtilityClass from '../generateUtilityClass';
import generateUtilityClasses from '../generateUtilityClasses';
export function getMenuUnstyledUtilityClass(slot) {
  return generateUtilityClass('MuiMenu', slot);
}
const menuUnstyledClasses = generateUtilityClasses('MuiMenu', ['root', 'listbox', 'expanded']);
export default menuUnstyledClasses;