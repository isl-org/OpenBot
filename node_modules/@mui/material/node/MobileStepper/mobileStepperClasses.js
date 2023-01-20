"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getMobileStepperUtilityClass = getMobileStepperUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getMobileStepperUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiMobileStepper', slot);
}
const mobileStepperClasses = (0, _utils.unstable_generateUtilityClasses)('MuiMobileStepper', ['root', 'positionBottom', 'positionTop', 'positionStatic', 'dots', 'dot', 'dotActive', 'progress']);
var _default = mobileStepperClasses;
exports.default = _default;