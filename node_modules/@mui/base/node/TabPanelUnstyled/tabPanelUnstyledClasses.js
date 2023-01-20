"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getTabPanelUnstyledUtilityClass = getTabPanelUnstyledUtilityClass;
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
var _generateUtilityClasses = _interopRequireDefault(require("../generateUtilityClasses"));
function getTabPanelUnstyledUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiTabPanel', slot);
}
const tabPanelUnstyledClasses = (0, _generateUtilityClasses.default)('MuiTabPanel', ['root', 'hidden']);
var _default = tabPanelUnstyledClasses;
exports.default = _default;