"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getFormLabelUtilityClasses = getFormLabelUtilityClasses;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getFormLabelUtilityClasses(slot) {
  return (0, _generateUtilityClass.default)('MuiFormLabel', slot);
}
const formLabelClasses = (0, _utils.unstable_generateUtilityClasses)('MuiFormLabel', ['root', 'colorSecondary', 'focused', 'disabled', 'error', 'filled', 'required', 'asterisk']);
var _default = formLabelClasses;
exports.default = _default;