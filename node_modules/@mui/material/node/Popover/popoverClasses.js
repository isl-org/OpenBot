"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getPopoverUtilityClass = getPopoverUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getPopoverUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiPopover', slot);
}
const popoverClasses = (0, _utils.unstable_generateUtilityClasses)('MuiPopover', ['root', 'paper']);
var _default = popoverClasses;
exports.default = _default;