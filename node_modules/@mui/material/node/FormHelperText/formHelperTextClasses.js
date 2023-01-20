"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getFormHelperTextUtilityClasses = getFormHelperTextUtilityClasses;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getFormHelperTextUtilityClasses(slot) {
  return (0, _generateUtilityClass.default)('MuiFormHelperText', slot);
}
const formHelperTextClasses = (0, _utils.unstable_generateUtilityClasses)('MuiFormHelperText', ['root', 'error', 'disabled', 'sizeSmall', 'sizeMedium', 'contained', 'focused', 'filled', 'required']);
var _default = formHelperTextClasses;
exports.default = _default;