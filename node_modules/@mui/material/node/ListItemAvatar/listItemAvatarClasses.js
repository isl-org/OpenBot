"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getListItemAvatarUtilityClass = getListItemAvatarUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getListItemAvatarUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiListItemAvatar', slot);
}
const listItemAvatarClasses = (0, _utils.unstable_generateUtilityClasses)('MuiListItemAvatar', ['root', 'alignItemsFlexStart']);
var _default = listItemAvatarClasses;
exports.default = _default;