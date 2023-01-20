import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getToolbarUtilityClass(slot) {
  return generateUtilityClass('MuiToolbar', slot);
}
var toolbarClasses = generateUtilityClasses('MuiToolbar', ['root', 'gutters', 'regular', 'dense']);
export default toolbarClasses;