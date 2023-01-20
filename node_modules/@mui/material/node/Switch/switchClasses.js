"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getSwitchUtilityClass = getSwitchUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getSwitchUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiSwitch', slot);
}
const switchClasses = (0, _utils.unstable_generateUtilityClasses)('MuiSwitch', ['root', 'edgeStart', 'edgeEnd', 'switchBase', 'colorPrimary', 'colorSecondary', 'sizeSmall', 'sizeMedium', 'checked', 'disabled', 'input', 'thumb', 'track']);
var _default = switchClasses;
exports.default = _default;