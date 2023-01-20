import generateUtilityClass from '../generateUtilityClass';
import generateUtilityClasses from '../generateUtilityClasses';
export function getTabsListUnstyledUtilityClass(slot) {
  return generateUtilityClass('MuiTabsList', slot);
}
var tabsListUnstyledClasses = generateUtilityClasses('MuiTabsList', ['root', 'horizontal', 'vertical']);
export default tabsListUnstyledClasses;