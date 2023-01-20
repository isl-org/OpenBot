"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getTablePaginationUtilityClass = getTablePaginationUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getTablePaginationUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiTablePagination', slot);
}
const tablePaginationClasses = (0, _utils.unstable_generateUtilityClasses)('MuiTablePagination', ['root', 'toolbar', 'spacer', 'selectLabel', 'selectRoot', 'select', 'selectIcon', 'input', 'menuItem', 'displayedRows', 'actions']);
var _default = tablePaginationClasses;
exports.default = _default;