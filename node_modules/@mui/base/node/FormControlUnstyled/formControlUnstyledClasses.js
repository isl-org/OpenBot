"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getFormControlUnstyledUtilityClass = getFormControlUnstyledUtilityClass;
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
var _generateUtilityClasses = _interopRequireDefault(require("../generateUtilityClasses"));
function getFormControlUnstyledUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiFormControl', slot);
}
const formControlUnstyledClasses = (0, _generateUtilityClasses.default)('MuiFormControl', ['root', 'disabled', 'error', 'filled', 'focused', 'required']);
var _default = formControlUnstyledClasses;
exports.default = _default;