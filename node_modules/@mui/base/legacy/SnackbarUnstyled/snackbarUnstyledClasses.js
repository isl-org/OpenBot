import generateUtilityClass from '../generateUtilityClass';
import generateUtilityClasses from '../generateUtilityClasses';
export function getSnackbarUnstyledUtilityClass(slot) {
  return generateUtilityClass('MuiSnackbar', slot);
}
var snackbarUnstyledClasses = generateUtilityClasses('MuiSnackbar', ['root']);
export default snackbarUnstyledClasses;