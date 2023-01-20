import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getPaginationUtilityClass(slot) {
  return generateUtilityClass('MuiPagination', slot);
}
const paginationClasses = generateUtilityClasses('MuiPagination', ['root', 'ul', 'outlined', 'text']);
export default paginationClasses;