import generateUtilityClass from '../generateUtilityClass';
import generateUtilityClasses from '../generateUtilityClasses';
export function getTabsUnstyledUtilityClass(slot) {
  return generateUtilityClass('MuiTabs', slot);
}
const tabsUnstyledClasses = generateUtilityClasses('MuiTabs', ['root', 'horizontal', 'vertical']);
export default tabsUnstyledClasses;