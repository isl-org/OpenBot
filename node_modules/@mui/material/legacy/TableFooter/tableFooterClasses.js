import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getTableFooterUtilityClass(slot) {
  return generateUtilityClass('MuiTableFooter', slot);
}
var tableFooterClasses = generateUtilityClasses('MuiTableFooter', ['root']);
export default tableFooterClasses;