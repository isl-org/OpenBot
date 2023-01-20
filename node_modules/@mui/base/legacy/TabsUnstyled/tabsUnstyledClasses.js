import generateUtilityClass from '../generateUtilityClass';
import generateUtilityClasses from '../generateUtilityClasses';
export function getTabsUnstyledUtilityClass(slot) {
  return generateUtilityClass('MuiTabs', slot);
}
var tabsUnstyledClasses = generateUtilityClasses('MuiTabs', ['root', 'horizontal', 'vertical']);
export default tabsUnstyledClasses;