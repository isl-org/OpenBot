"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getInputUnstyledUtilityClass = getInputUnstyledUtilityClass;
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
var _generateUtilityClasses = _interopRequireDefault(require("../generateUtilityClasses"));
function getInputUnstyledUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiInput', slot);
}
const inputUnstyledClasses = (0, _generateUtilityClasses.default)('MuiInput', ['root', 'formControl', 'focused', 'disabled', 'error', 'multiline', 'input', 'inputMultiline', 'inputTypeSearch', 'adornedStart', 'adornedEnd']);
var _default = inputUnstyledClasses;
exports.default = _default;