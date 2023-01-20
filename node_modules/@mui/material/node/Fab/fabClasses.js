"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getFabUtilityClass = getFabUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getFabUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiFab', slot);
}
const fabClasses = (0, _utils.unstable_generateUtilityClasses)('MuiFab', ['root', 'primary', 'secondary', 'extended', 'circular', 'focusVisible', 'disabled', 'colorInherit', 'sizeSmall', 'sizeMedium', 'sizeLarge', 'info', 'error', 'warning', 'success']);
var _default = fabClasses;
exports.default = _default;