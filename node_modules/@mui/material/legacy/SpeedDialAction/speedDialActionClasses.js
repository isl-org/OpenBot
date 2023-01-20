import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getSpeedDialActionUtilityClass(slot) {
  return generateUtilityClass('MuiSpeedDialAction', slot);
}
var speedDialActionClasses = generateUtilityClasses('MuiSpeedDialAction', ['fab', 'fabClosed', 'staticTooltip', 'staticTooltipClosed', 'staticTooltipLabel', 'tooltipPlacementLeft', 'tooltipPlacementRight']);
export default speedDialActionClasses;