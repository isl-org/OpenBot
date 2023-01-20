"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getInputAdornmentUtilityClass = getInputAdornmentUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getInputAdornmentUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiInputAdornment', slot);
}
const inputAdornmentClasses = (0, _utils.unstable_generateUtilityClasses)('MuiInputAdornment', ['root', 'filled', 'standard', 'outlined', 'positionStart', 'positionEnd', 'disablePointerEvents', 'hiddenLabel', 'sizeSmall']);
var _default = inputAdornmentClasses;
exports.default = _default;