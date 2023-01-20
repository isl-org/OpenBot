"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getSpeedDialIconUtilityClass = getSpeedDialIconUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getSpeedDialIconUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiSpeedDialIcon', slot);
}
const speedDialIconClasses = (0, _utils.unstable_generateUtilityClasses)('MuiSpeedDialIcon', ['root', 'icon', 'iconOpen', 'iconWithOpenIconOpen', 'openIcon', 'openIconOpen']);
var _default = speedDialIconClasses;
exports.default = _default;