"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getImageListItemBarUtilityClass = getImageListItemBarUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getImageListItemBarUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiImageListItemBar', slot);
}
const imageListItemBarClasses = (0, _utils.unstable_generateUtilityClasses)('MuiImageListItemBar', ['root', 'positionBottom', 'positionTop', 'positionBelow', 'titleWrap', 'titleWrapBottom', 'titleWrapTop', 'titleWrapBelow', 'titleWrapActionPosLeft', 'titleWrapActionPosRight', 'title', 'subtitle', 'actionIcon', 'actionIconActionPosLeft', 'actionIconActionPosRight']);
var _default = imageListItemBarClasses;
exports.default = _default;