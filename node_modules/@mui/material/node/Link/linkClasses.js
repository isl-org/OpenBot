"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getLinkUtilityClass = getLinkUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getLinkUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiLink', slot);
}
const linkClasses = (0, _utils.unstable_generateUtilityClasses)('MuiLink', ['root', 'underlineNone', 'underlineHover', 'underlineAlways', 'button', 'focusVisible']);
var _default = linkClasses;
exports.default = _default;