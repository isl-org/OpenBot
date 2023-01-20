"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getSpeedDialActionUtilityClass = getSpeedDialActionUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getSpeedDialActionUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiSpeedDialAction', slot);
}
const speedDialActionClasses = (0, _utils.unstable_generateUtilityClasses)('MuiSpeedDialAction', ['fab', 'fabClosed', 'staticTooltip', 'staticTooltipClosed', 'staticTooltipLabel', 'tooltipPlacementLeft', 'tooltipPlacementRight']);
var _default = speedDialActionClasses;
exports.default = _default;