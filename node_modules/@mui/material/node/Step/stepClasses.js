"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getStepUtilityClass = getStepUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getStepUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiStep', slot);
}
const stepClasses = (0, _utils.unstable_generateUtilityClasses)('MuiStep', ['root', 'horizontal', 'vertical', 'alternativeLabel', 'completed']);
var _default = stepClasses;
exports.default = _default;