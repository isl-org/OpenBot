"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getMenuItemUnstyledUtilityClass = getMenuItemUnstyledUtilityClass;
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
var _generateUtilityClasses = _interopRequireDefault(require("../generateUtilityClasses"));
function getMenuItemUnstyledUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiMenuItem', slot);
}
const menuItemUnstyledClasses = (0, _generateUtilityClasses.default)('MuiMenuItem', ['root', 'disabled', 'focusVisible']);
var _default = menuItemUnstyledClasses;
exports.default = _default;