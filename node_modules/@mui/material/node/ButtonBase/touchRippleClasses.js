"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getTouchRippleUtilityClass = getTouchRippleUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getTouchRippleUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiTouchRipple', slot);
}
const touchRippleClasses = (0, _utils.unstable_generateUtilityClasses)('MuiTouchRipple', ['root', 'ripple', 'rippleVisible', 'ripplePulsate', 'child', 'childLeaving', 'childPulsate']);
var _default = touchRippleClasses;
exports.default = _default;