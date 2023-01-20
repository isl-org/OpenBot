"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getImageListItemUtilityClass = getImageListItemUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getImageListItemUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiImageListItem', slot);
}
const imageListItemClasses = (0, _utils.unstable_generateUtilityClasses)('MuiImageListItem', ['root', 'img', 'standard', 'woven', 'masonry', 'quilted']);
var _default = imageListItemClasses;
exports.default = _default;