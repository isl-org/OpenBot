"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getToolbarUtilityClass = getToolbarUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getToolbarUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiToolbar', slot);
}
const toolbarClasses = (0, _utils.unstable_generateUtilityClasses)('MuiToolbar', ['root', 'gutters', 'regular', 'dense']);
var _default = toolbarClasses;
exports.default = _default;