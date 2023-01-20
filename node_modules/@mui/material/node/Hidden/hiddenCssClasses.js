"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getHiddenCssUtilityClass = getHiddenCssUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getHiddenCssUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('PrivateHiddenCss', slot);
}
const hiddenCssClasses = (0, _utils.unstable_generateUtilityClasses)('PrivateHiddenCss', ['root', 'xlDown', 'xlUp', 'onlyXl', 'lgDown', 'lgUp', 'onlyLg', 'mdDown', 'mdUp', 'onlyMd', 'smDown', 'smUp', 'onlySm', 'xsDown', 'xsUp', 'onlyXs']);
var _default = hiddenCssClasses;
exports.default = _default;