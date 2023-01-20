import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getTableHeadUtilityClass(slot) {
  return generateUtilityClass('MuiTableHead', slot);
}
const tableHeadClasses = generateUtilityClasses('MuiTableHead', ['root']);
export default tableHeadClasses;