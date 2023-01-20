import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getRadioUtilityClass(slot) {
  return generateUtilityClass('MuiRadio', slot);
}
const radioClasses = generateUtilityClasses('MuiRadio', ['root', 'checked', 'disabled', 'colorPrimary', 'colorSecondary']);
export default radioClasses;