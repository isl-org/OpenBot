"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getOptionGroupUnstyledUtilityClass = getOptionGroupUnstyledUtilityClass;
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
var _generateUtilityClasses = _interopRequireDefault(require("../generateUtilityClasses"));
function getOptionGroupUnstyledUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiOptionGroup', slot);
}
const optionGroupUnstyledClasses = (0, _generateUtilityClasses.default)('MuiOptionGroup', ['root', 'label', 'list']);
var _default = optionGroupUnstyledClasses;
exports.default = _default;