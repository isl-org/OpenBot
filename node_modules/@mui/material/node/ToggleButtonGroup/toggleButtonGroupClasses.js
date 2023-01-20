"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getToggleButtonGroupUtilityClass = getToggleButtonGroupUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getToggleButtonGroupUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiToggleButtonGroup', slot);
}
const toggleButtonGroupClasses = (0, _utils.unstable_generateUtilityClasses)('MuiToggleButtonGroup', ['root', 'selected', 'vertical', 'disabled', 'grouped', 'groupedHorizontal', 'groupedVertical']);
var _default = toggleButtonGroupClasses;
exports.default = _default;