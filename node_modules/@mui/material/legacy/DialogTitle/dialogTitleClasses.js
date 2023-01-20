import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getDialogTitleUtilityClass(slot) {
  return generateUtilityClass('MuiDialogTitle', slot);
}
var dialogTitleClasses = generateUtilityClasses('MuiDialogTitle', ['root']);
export default dialogTitleClasses;