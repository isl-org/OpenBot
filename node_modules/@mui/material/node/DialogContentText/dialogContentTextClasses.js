"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getDialogContentTextUtilityClass = getDialogContentTextUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getDialogContentTextUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiDialogContentText', slot);
}
const dialogContentTextClasses = (0, _utils.unstable_generateUtilityClasses)('MuiDialogContentText', ['root']);
var _default = dialogContentTextClasses;
exports.default = _default;