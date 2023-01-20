import _extends from "@babel/runtime/helpers/esm/extends";
import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
import { inputBaseClasses } from '../InputBase';
export function getOutlinedInputUtilityClass(slot) {
  return generateUtilityClass('MuiOutlinedInput', slot);
}
const outlinedInputClasses = _extends({}, inputBaseClasses, generateUtilityClasses('MuiOutlinedInput', ['root', 'notchedOutline', 'input']));
export default outlinedInputClasses;