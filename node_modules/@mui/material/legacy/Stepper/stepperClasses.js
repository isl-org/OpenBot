import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getStepperUtilityClass(slot) {
  return generateUtilityClass('MuiStepper', slot);
}
var stepperClasses = generateUtilityClasses('MuiStepper', ['root', 'horizontal', 'vertical', 'alternativeLabel']);
export default stepperClasses;