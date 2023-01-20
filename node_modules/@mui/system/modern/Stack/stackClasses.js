import { unstable_generateUtilityClass as generateUtilityClass, unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
export function getStackUtilityClass(slot) {
  return generateUtilityClass('MuiStack', slot);
}
const stackClasses = generateUtilityClasses('MuiStack', ['root']);
export default stackClasses;