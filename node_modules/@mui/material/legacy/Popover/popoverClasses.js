import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getPopoverUtilityClass(slot) {
  return generateUtilityClass('MuiPopover', slot);
}
var popoverClasses = generateUtilityClasses('MuiPopover', ['root', 'paper']);
export default popoverClasses;