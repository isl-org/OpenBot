"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getButtonBaseUtilityClass = getButtonBaseUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getButtonBaseUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiButtonBase', slot);
}
const buttonBaseClasses = (0, _utils.unstable_generateUtilityClasses)('MuiButtonBase', ['root', 'disabled', 'focusVisible']);
var _default = buttonBaseClasses;
exports.default = _default;