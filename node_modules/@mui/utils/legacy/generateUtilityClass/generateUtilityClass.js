import ClassNameGenerator from '../ClassNameGenerator';
var globalStateClassesMapping = {
  active: 'active',
  checked: 'checked',
  completed: 'completed',
  disabled: 'disabled',
  error: 'error',
  expanded: 'expanded',
  focused: 'focused',
  focusVisible: 'focusVisible',
  required: 'required',
  selected: 'selected'
};
export default function generateUtilityClass(componentName, slot) {
  var globalStatePrefix = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : 'Mui';
  var globalStateClass = globalStateClassesMapping[slot];
  return globalStateClass ? "".concat(globalStatePrefix, "-").concat(globalStateClass) : "".concat(ClassNameGenerator.generate(componentName), "-").concat(slot);
}