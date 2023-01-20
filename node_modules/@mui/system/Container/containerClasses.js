"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getContainerUtilityClass = getContainerUtilityClass;
var _utils = require("@mui/utils");
function getContainerUtilityClass(slot) {
  return (0, _utils.unstable_generateUtilityClass)('MuiContainer', slot);
}
const containerClasses = (0, _utils.unstable_generateUtilityClasses)('MuiContainer', ['root', 'disableGutters', 'fixed', 'maxWidthXs', 'maxWidthSm', 'maxWidthMd', 'maxWidthLg', 'maxWidthXl']);
var _default = containerClasses;
exports.default = _default;