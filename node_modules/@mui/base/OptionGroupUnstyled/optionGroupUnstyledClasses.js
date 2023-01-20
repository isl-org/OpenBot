import generateUtilityClass from '../generateUtilityClass';
import generateUtilityClasses from '../generateUtilityClasses';
export function getOptionGroupUnstyledUtilityClass(slot) {
  return generateUtilityClass('MuiOptionGroup', slot);
}
const optionGroupUnstyledClasses = generateUtilityClasses('MuiOptionGroup', ['root', 'label', 'list']);
export default optionGroupUnstyledClasses;