"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getSwitchUnstyledUtilityClass = getSwitchUnstyledUtilityClass;
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
var _generateUtilityClasses = _interopRequireDefault(require("../generateUtilityClasses"));
function getSwitchUnstyledUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiSwitch', slot);
}
const switchUnstyledClasses = (0, _generateUtilityClasses.default)('MuiSwitch', ['root', 'input', 'track', 'thumb', 'checked', 'disabled', 'focusVisible', 'readOnly']);
var _default = switchUnstyledClasses;
exports.default = _default;