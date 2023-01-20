import generateUtilityClass from '../generateUtilityClass';
import generateUtilityClasses from '../generateUtilityClasses';
export function getTabPanelUnstyledUtilityClass(slot) {
  return generateUtilityClass('MuiTabPanel', slot);
}
const tabPanelUnstyledClasses = generateUtilityClasses('MuiTabPanel', ['root', 'hidden']);
export default tabPanelUnstyledClasses;