"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getCircularProgressUtilityClass = getCircularProgressUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getCircularProgressUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiCircularProgress', slot);
}
const circularProgressClasses = (0, _utils.unstable_generateUtilityClasses)('MuiCircularProgress', ['root', 'determinate', 'indeterminate', 'colorPrimary', 'colorSecondary', 'svg', 'circle', 'circleDeterminate', 'circleIndeterminate', 'circleDisableShrink']);
var _default = circularProgressClasses;
exports.default = _default;