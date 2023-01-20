"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getFilledInputUtilityClass = getFilledInputUtilityClass;
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
var _InputBase = require("../InputBase");
function getFilledInputUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiFilledInput', slot);
}
const filledInputClasses = (0, _extends2.default)({}, _InputBase.inputBaseClasses, (0, _utils.unstable_generateUtilityClasses)('MuiFilledInput', ['root', 'underline', 'input']));
var _default = filledInputClasses;
exports.default = _default;