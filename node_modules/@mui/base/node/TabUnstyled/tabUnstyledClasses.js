"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getTabUnstyledUtilityClass = getTabUnstyledUtilityClass;
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
var _generateUtilityClasses = _interopRequireDefault(require("../generateUtilityClasses"));
function getTabUnstyledUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiTab', slot);
}
const tabUnstyledClasses = (0, _generateUtilityClasses.default)('MuiTab', ['root', 'selected', 'disabled']);
var _default = tabUnstyledClasses;
exports.default = _default;