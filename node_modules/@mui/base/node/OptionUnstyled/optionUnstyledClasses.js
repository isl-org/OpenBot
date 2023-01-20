"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getOptionUnstyledUtilityClass = getOptionUnstyledUtilityClass;
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
var _generateUtilityClasses = _interopRequireDefault(require("../generateUtilityClasses"));
function getOptionUnstyledUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiOption', slot);
}
const optionUnstyledClasses = (0, _generateUtilityClasses.default)('MuiOption', ['root', 'disabled', 'selected', 'highlighted']);
var _default = optionUnstyledClasses;
exports.default = _default;