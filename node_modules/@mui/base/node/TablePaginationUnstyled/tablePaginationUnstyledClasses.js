"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getTablePaginationUnstyledUtilityClass = getTablePaginationUnstyledUtilityClass;
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
var _generateUtilityClasses = _interopRequireDefault(require("../generateUtilityClasses"));
function getTablePaginationUnstyledUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiTablePagination', slot);
}
const tablePaginationUnstyledClasses = (0, _generateUtilityClasses.default)('MuiTablePagination', ['root', 'toolbar', 'spacer', 'selectLabel', 'selectRoot', 'select', 'selectIcon', 'input', 'menuItem', 'displayedRows', 'actions']);
var _default = tablePaginationUnstyledClasses;
exports.default = _default;