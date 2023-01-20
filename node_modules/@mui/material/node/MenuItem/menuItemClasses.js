"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getMenuItemUtilityClass = getMenuItemUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getMenuItemUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiMenuItem', slot);
}
const menuItemClasses = (0, _utils.unstable_generateUtilityClasses)('MuiMenuItem', ['root', 'focusVisible', 'dense', 'disabled', 'divider', 'gutters', 'selected']);
var _default = menuItemClasses;
exports.default = _default;