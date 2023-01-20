import generateUtilityClasses from '../generateUtilityClasses';
import generateUtilityClass from '../generateUtilityClass';
export function getBadgeUnstyledUtilityClass(slot) {
  return generateUtilityClass('MuiBadge', slot);
}
const badgeUnstyledClasses = generateUtilityClasses('MuiBadge', ['root', 'badge', 'invisible']);
export default badgeUnstyledClasses;