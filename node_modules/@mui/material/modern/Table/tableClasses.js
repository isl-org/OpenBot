import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getTableUtilityClass(slot) {
  return generateUtilityClass('MuiTable', slot);
}
const tableClasses = generateUtilityClasses('MuiTable', ['root', 'stickyHeader']);
export default tableClasses;