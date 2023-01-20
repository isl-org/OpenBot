"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getStepIconUtilityClass = getStepIconUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getStepIconUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiStepIcon', slot);
}
const stepIconClasses = (0, _utils.unstable_generateUtilityClasses)('MuiStepIcon', ['root', 'active', 'completed', 'error', 'text']);
var _default = stepIconClasses;
exports.default = _default;