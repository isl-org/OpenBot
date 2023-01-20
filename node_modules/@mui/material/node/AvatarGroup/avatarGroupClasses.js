"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getAvatarGroupUtilityClass = getAvatarGroupUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getAvatarGroupUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiAvatarGroup', slot);
}
const avatarGroupClasses = (0, _utils.unstable_generateUtilityClasses)('MuiAvatarGroup', ['root', 'avatar']);
var _default = avatarGroupClasses;
exports.default = _default;