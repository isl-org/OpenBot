"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getStepConnectorUtilityClass = getStepConnectorUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getStepConnectorUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiStepConnector', slot);
}
const stepConnectorClasses = (0, _utils.unstable_generateUtilityClasses)('MuiStepConnector', ['root', 'horizontal', 'vertical', 'alternativeLabel', 'active', 'completed', 'disabled', 'line', 'lineHorizontal', 'lineVertical']);
var _default = stepConnectorClasses;
exports.default = _default;