"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getDialogContentUtilityClass = getDialogContentUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getDialogContentUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiDialogContent', slot);
}
const dialogContentClasses = (0, _utils.unstable_generateUtilityClasses)('MuiDialogContent', ['root', 'dividers']);
var _default = dialogContentClasses;
exports.default = _default;