import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getSpeedDialIconUtilityClass(slot) {
  return generateUtilityClass('MuiSpeedDialIcon', slot);
}
var speedDialIconClasses = generateUtilityClasses('MuiSpeedDialIcon', ['root', 'icon', 'iconOpen', 'iconWithOpenIconOpen', 'openIcon', 'openIconOpen']);
export default speedDialIconClasses;