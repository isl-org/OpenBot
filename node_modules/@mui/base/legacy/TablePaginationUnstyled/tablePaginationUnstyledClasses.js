import generateUtilityClass from '../generateUtilityClass';
import generateUtilityClasses from '../generateUtilityClasses';
export function getTablePaginationUnstyledUtilityClass(slot) {
  return generateUtilityClass('MuiTablePagination', slot);
}
var tablePaginationUnstyledClasses = generateUtilityClasses('MuiTablePagination', ['root', 'toolbar', 'spacer', 'selectLabel', 'selectRoot', 'select', 'selectIcon', 'input', 'menuItem', 'displayedRows', 'actions']);
export default tablePaginationUnstyledClasses;