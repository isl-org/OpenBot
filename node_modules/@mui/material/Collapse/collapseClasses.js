import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getCollapseUtilityClass(slot) {
  return generateUtilityClass('MuiCollapse', slot);
}
const collapseClasses = generateUtilityClasses('MuiCollapse', ['root', 'horizontal', 'vertical', 'entered', 'hidden', 'wrapper', 'wrapperInner']);
export default collapseClasses;