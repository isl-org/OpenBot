"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getStepperUtilityClass = getStepperUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getStepperUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiStepper', slot);
}
const stepperClasses = (0, _utils.unstable_generateUtilityClasses)('MuiStepper', ['root', 'horizontal', 'vertical', 'alternativeLabel']);
var _default = stepperClasses;
exports.default = _default;