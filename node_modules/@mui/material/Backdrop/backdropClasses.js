import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
export function getBackdropUtilityClass(slot) {
  return generateUtilityClass('MuiBackdrop', slot);
}
const backdropClasses = generateUtilityClasses('MuiBackdrop', ['root', 'invisible']);
export default backdropClasses;