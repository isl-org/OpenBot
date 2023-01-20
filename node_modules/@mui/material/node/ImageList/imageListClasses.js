"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getImageListUtilityClass = getImageListUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getImageListUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiImageList', slot);
}
const imageListClasses = (0, _utils.unstable_generateUtilityClasses)('MuiImageList', ['root', 'masonry', 'quilted', 'standard', 'woven']);
var _default = imageListClasses;
exports.default = _default;