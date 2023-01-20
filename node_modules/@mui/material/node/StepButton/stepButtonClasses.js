"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getStepButtonUtilityClass = getStepButtonUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getStepButtonUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiStepButton', slot);
}
const stepButtonClasses = (0, _utils.unstable_generateUtilityClasses)('MuiStepButton', ['root', 'horizontal', 'vertical', 'touchRipple']);
var _default = stepButtonClasses;
exports.default = _default;