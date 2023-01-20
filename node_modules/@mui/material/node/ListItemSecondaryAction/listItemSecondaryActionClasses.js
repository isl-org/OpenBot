"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getListItemSecondaryActionClassesUtilityClass = getListItemSecondaryActionClassesUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getListItemSecondaryActionClassesUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiListItemSecondaryAction', slot);
}
const listItemSecondaryActionClasses = (0, _utils.unstable_generateUtilityClasses)('MuiListItemSecondaryAction', ['root', 'disableGutters']);
var _default = listItemSecondaryActionClasses;
exports.default = _default;