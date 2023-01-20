import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getSpeedDialUtilityClass(slot) {
  return generateUtilityClass('MuiSpeedDial', slot);
}
const speedDialClasses = generateUtilityClasses('MuiSpeedDial', ['root', 'fab', 'directionUp', 'directionDown', 'directionLeft', 'directionRight', 'actions', 'actionsClosed']);
export default speedDialClasses;