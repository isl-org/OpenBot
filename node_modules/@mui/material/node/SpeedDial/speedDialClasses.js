"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getSpeedDialUtilityClass = getSpeedDialUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getSpeedDialUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiSpeedDial', slot);
}
const speedDialClasses = (0, _utils.unstable_generateUtilityClasses)('MuiSpeedDial', ['root', 'fab', 'directionUp', 'directionDown', 'directionLeft', 'directionRight', 'actions', 'actionsClosed']);
var _default = speedDialClasses;
exports.default = _default;