"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getListItemButtonUtilityClass = getListItemButtonUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getListItemButtonUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiListItemButton', slot);
}
const listItemButtonClasses = (0, _utils.unstable_generateUtilityClasses)('MuiListItemButton', ['root', 'focusVisible', 'dense', 'alignItemsFlexStart', 'disabled', 'divider', 'gutters', 'selected']);
var _default = listItemButtonClasses;
exports.default = _default;