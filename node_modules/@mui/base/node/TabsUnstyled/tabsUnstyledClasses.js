"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getTabsUnstyledUtilityClass = getTabsUnstyledUtilityClass;
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
var _generateUtilityClasses = _interopRequireDefault(require("../generateUtilityClasses"));
function getTabsUnstyledUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiTabs', slot);
}
const tabsUnstyledClasses = (0, _generateUtilityClasses.default)('MuiTabs', ['root', 'horizontal', 'vertical']);
var _default = tabsUnstyledClasses;
exports.default = _default;