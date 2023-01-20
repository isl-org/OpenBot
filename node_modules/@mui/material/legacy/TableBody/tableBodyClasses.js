import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getTableBodyUtilityClass(slot) {
  return generateUtilityClass('MuiTableBody', slot);
}
var tableBodyClasses = generateUtilityClasses('MuiTableBody', ['root']);
export default tableBodyClasses;