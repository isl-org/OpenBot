import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getToggleButtonGroupUtilityClass(slot) {
  return generateUtilityClass('MuiToggleButtonGroup', slot);
}
const toggleButtonGroupClasses = generateUtilityClasses('MuiToggleButtonGroup', ['root', 'selected', 'vertical', 'disabled', 'grouped', 'groupedHorizontal', 'groupedVertical']);
export default toggleButtonGroupClasses;