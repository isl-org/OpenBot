"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getListItemTextUtilityClass = getListItemTextUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getListItemTextUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiListItemText', slot);
}
const listItemTextClasses = (0, _utils.unstable_generateUtilityClasses)('MuiListItemText', ['root', 'multiline', 'dense', 'inset', 'primary', 'secondary']);
var _default = listItemTextClasses;
exports.default = _default;