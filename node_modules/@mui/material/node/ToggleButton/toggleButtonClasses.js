"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getToggleButtonUtilityClass = getToggleButtonUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getToggleButtonUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiToggleButton', slot);
}
const toggleButtonClasses = (0, _utils.unstable_generateUtilityClasses)('MuiToggleButton', ['root', 'disabled', 'selected', 'standard', 'primary', 'secondary', 'sizeSmall', 'sizeMedium', 'sizeLarge']);
var _default = toggleButtonClasses;
exports.default = _default;