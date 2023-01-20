"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getMenuUtilityClass = getMenuUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getMenuUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiMenu', slot);
}
const menuClasses = (0, _utils.unstable_generateUtilityClasses)('MuiMenu', ['root', 'paper', 'list']);
var _default = menuClasses;
exports.default = _default;