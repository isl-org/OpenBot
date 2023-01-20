"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getSnackbarUnstyledUtilityClass = getSnackbarUnstyledUtilityClass;
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
var _generateUtilityClasses = _interopRequireDefault(require("../generateUtilityClasses"));
function getSnackbarUnstyledUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiSnackbar', slot);
}
const snackbarUnstyledClasses = (0, _generateUtilityClasses.default)('MuiSnackbar', ['root']);
var _default = snackbarUnstyledClasses;
exports.default = _default;