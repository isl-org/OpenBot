"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getSkeletonUtilityClass = getSkeletonUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getSkeletonUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiSkeleton', slot);
}
const skeletonClasses = (0, _utils.unstable_generateUtilityClasses)('MuiSkeleton', ['root', 'text', 'rectangular', 'rounded', 'circular', 'pulse', 'wave', 'withChildren', 'fitContent', 'heightAuto']);
var _default = skeletonClasses;
exports.default = _default;