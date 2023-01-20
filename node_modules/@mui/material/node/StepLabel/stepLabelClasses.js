"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getStepLabelUtilityClass = getStepLabelUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getStepLabelUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiStepLabel', slot);
}
const stepLabelClasses = (0, _utils.unstable_generateUtilityClasses)('MuiStepLabel', ['root', 'horizontal', 'vertical', 'label', 'active', 'completed', 'error', 'disabled', 'iconContainer', 'alternativeLabel', 'labelContainer']);
var _default = stepLabelClasses;
exports.default = _default;