import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getStepContentUtilityClass(slot) {
  return generateUtilityClass('MuiStepContent', slot);
}
var stepContentClasses = generateUtilityClasses('MuiStepContent', ['root', 'last', 'transition']);
export default stepContentClasses;