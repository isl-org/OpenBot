import generateUtilityClasses from '../generateUtilityClasses';
import generateUtilityClass from '../generateUtilityClass';
export function getBadgeUnstyledUtilityClass(slot) {
  return generateUtilityClass('MuiBadge', slot);
}
var badgeUnstyledClasses = generateUtilityClasses('MuiBadge', ['root', 'badge', 'invisible']);
export default badgeUnstyledClasses;