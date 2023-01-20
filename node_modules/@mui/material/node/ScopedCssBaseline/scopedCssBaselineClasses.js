"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getScopedCssBaselineUtilityClass = getScopedCssBaselineUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getScopedCssBaselineUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiScopedCssBaseline', slot);
}
const scopedCssBaselineClasses = (0, _utils.unstable_generateUtilityClasses)('MuiScopedCssBaseline', ['root']);
var _default = scopedCssBaselineClasses;
exports.default = _default;