"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getStepContentUtilityClass = getStepContentUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getStepContentUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiStepContent', slot);
}
const stepContentClasses = (0, _utils.unstable_generateUtilityClasses)('MuiStepContent', ['root', 'last', 'transition']);
var _default = stepContentClasses;
exports.default = _default;