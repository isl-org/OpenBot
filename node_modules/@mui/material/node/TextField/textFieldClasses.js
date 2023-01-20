"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getTextFieldUtilityClass = getTextFieldUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getTextFieldUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiTextField', slot);
}
const textFieldClasses = (0, _utils.unstable_generateUtilityClasses)('MuiTextField', ['root']);
var _default = textFieldClasses;
exports.default = _default;