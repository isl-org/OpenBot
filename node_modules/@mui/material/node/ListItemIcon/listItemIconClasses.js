"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getListItemIconUtilityClass = getListItemIconUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getListItemIconUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiListItemIcon', slot);
}
const listItemIconClasses = (0, _utils.unstable_generateUtilityClasses)('MuiListItemIcon', ['root', 'alignItemsFlexStart']);
var _default = listItemIconClasses;
exports.default = _default;