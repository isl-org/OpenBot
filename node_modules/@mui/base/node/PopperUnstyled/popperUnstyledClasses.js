"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getPopperUnstyledUtilityClass = getPopperUnstyledUtilityClass;
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
var _generateUtilityClasses = _interopRequireDefault(require("../generateUtilityClasses"));
function getPopperUnstyledUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiPopperUnstyled', slot);
}
const popperUnstyledClasses = (0, _generateUtilityClasses.default)('MuiPopperUnstyled', ['root']);
var _default = popperUnstyledClasses;
exports.default = _default;