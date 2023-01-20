"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getStackUtilityClass = getStackUtilityClass;
var _utils = require("@mui/utils");
function getStackUtilityClass(slot) {
  return (0, _utils.unstable_generateUtilityClass)('MuiStack', slot);
}
const stackClasses = (0, _utils.unstable_generateUtilityClasses)('MuiStack', ['root']);
var _default = stackClasses;
exports.default = _default;