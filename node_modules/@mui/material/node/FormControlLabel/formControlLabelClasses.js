"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getFormControlLabelUtilityClasses = getFormControlLabelUtilityClasses;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getFormControlLabelUtilityClasses(slot) {
  return (0, _generateUtilityClass.default)('MuiFormControlLabel', slot);
}
const formControlLabelClasses = (0, _utils.unstable_generateUtilityClasses)('MuiFormControlLabel', ['root', 'labelPlacementStart', 'labelPlacementTop', 'labelPlacementBottom', 'disabled', 'label', 'error']);
var _default = formControlLabelClasses;
exports.default = _default;