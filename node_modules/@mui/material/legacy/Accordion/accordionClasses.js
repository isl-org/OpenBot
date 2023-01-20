import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getAccordionUtilityClass(slot) {
  return generateUtilityClass('MuiAccordion', slot);
}
var accordionClasses = generateUtilityClasses('MuiAccordion', ['root', 'rounded', 'expanded', 'disabled', 'gutters', 'region']);
export default accordionClasses;