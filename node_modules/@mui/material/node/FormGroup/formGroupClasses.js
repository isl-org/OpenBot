"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getFormGroupUtilityClass = getFormGroupUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getFormGroupUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiFormGroup', slot);
}
const formGroupClasses = (0, _utils.unstable_generateUtilityClasses)('MuiFormGroup', ['root', 'row', 'error']);
var _default = formGroupClasses;
exports.default = _default;