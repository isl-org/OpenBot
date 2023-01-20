"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getTableCellUtilityClass = getTableCellUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getTableCellUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiTableCell', slot);
}
const tableCellClasses = (0, _utils.unstable_generateUtilityClasses)('MuiTableCell', ['root', 'head', 'body', 'footer', 'sizeSmall', 'sizeMedium', 'paddingCheckbox', 'paddingNone', 'alignLeft', 'alignCenter', 'alignRight', 'alignJustify', 'stickyHeader']);
var _default = tableCellClasses;
exports.default = _default;