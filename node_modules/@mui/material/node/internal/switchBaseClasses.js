"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getSwitchBaseUtilityClass = getSwitchBaseUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getSwitchBaseUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('PrivateSwitchBase', slot);
}
const switchBaseClasses = (0, _utils.unstable_generateUtilityClasses)('PrivateSwitchBase', ['root', 'checked', 'disabled', 'input', 'edgeStart', 'edgeEnd']);
var _default = switchBaseClasses;
exports.default = _default;