import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getStepConnectorUtilityClass(slot) {
  return generateUtilityClass('MuiStepConnector', slot);
}
var stepConnectorClasses = generateUtilityClasses('MuiStepConnector', ['root', 'horizontal', 'vertical', 'alternativeLabel', 'active', 'completed', 'disabled', 'line', 'lineHorizontal', 'lineVertical']);
export default stepConnectorClasses;