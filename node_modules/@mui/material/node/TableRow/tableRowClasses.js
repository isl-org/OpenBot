"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getTableRowUtilityClass = getTableRowUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getTableRowUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiTableRow', slot);
}
const tableRowClasses = (0, _utils.unstable_generateUtilityClasses)('MuiTableRow', ['root', 'selected', 'hover', 'head', 'footer']);
var _default = tableRowClasses;
exports.default = _default;