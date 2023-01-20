"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getInputLabelUtilityClasses = getInputLabelUtilityClasses;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getInputLabelUtilityClasses(slot) {
  return (0, _generateUtilityClass.default)('MuiInputLabel', slot);
}
const inputLabelClasses = (0, _utils.unstable_generateUtilityClasses)('MuiInputLabel', ['root', 'focused', 'disabled', 'error', 'required', 'asterisk', 'formControl', 'sizeSmall', 'shrink', 'animated', 'standard', 'filled', 'outlined']);
var _default = inputLabelClasses;
exports.default = _default;