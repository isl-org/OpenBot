"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getFormControlUtilityClasses = getFormControlUtilityClasses;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getFormControlUtilityClasses(slot) {
  return (0, _generateUtilityClass.default)('MuiFormControl', slot);
}
const formControlClasses = (0, _utils.unstable_generateUtilityClasses)('MuiFormControl', ['root', 'marginNone', 'marginNormal', 'marginDense', 'fullWidth', 'disabled']);
var _default = formControlClasses;
exports.default = _default;