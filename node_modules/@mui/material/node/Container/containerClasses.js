"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getContainerUtilityClass = getContainerUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getContainerUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiContainer', slot);
}
const containerClasses = (0, _utils.unstable_generateUtilityClasses)('MuiContainer', ['root', 'disableGutters', 'fixed', 'maxWidthXs', 'maxWidthSm', 'maxWidthMd', 'maxWidthLg', 'maxWidthXl']);
var _default = containerClasses;
exports.default = _default;