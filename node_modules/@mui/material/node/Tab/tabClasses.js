"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getTabUtilityClass = getTabUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getTabUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiTab', slot);
}
const tabClasses = (0, _utils.unstable_generateUtilityClasses)('MuiTab', ['root', 'labelIcon', 'textColorInherit', 'textColorPrimary', 'textColorSecondary', 'selected', 'disabled', 'fullWidth', 'wrapped', 'iconWrapper']);
var _default = tabClasses;
exports.default = _default;