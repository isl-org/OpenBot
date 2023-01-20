import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getCardUtilityClass(slot) {
  return generateUtilityClass('MuiCard', slot);
}
var cardClasses = generateUtilityClasses('MuiCard', ['root']);
export default cardClasses;