"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getTableSortLabelUtilityClass = getTableSortLabelUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getTableSortLabelUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiTableSortLabel', slot);
}
const tableSortLabelClasses = (0, _utils.unstable_generateUtilityClasses)('MuiTableSortLabel', ['root', 'active', 'icon', 'iconDirectionDesc', 'iconDirectionAsc']);
var _default = tableSortLabelClasses;
exports.default = _default;