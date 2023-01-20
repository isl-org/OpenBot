import _extends from "@babel/runtime/helpers/esm/extends";
import { unstable_generateUtilityClasses as generateUtilityClasses } from '@mui/utils';
import generateUtilityClass from '../generateUtilityClass';
import { inputBaseClasses } from '../InputBase';
export function getFilledInputUtilityClass(slot) {
  return generateUtilityClass('MuiFilledInput', slot);
}
var filledInputClasses = _extends({}, inputBaseClasses, generateUtilityClasses('MuiFilledInput', ['root', 'underline', 'input']));
export default filledInputClasses;