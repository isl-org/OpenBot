"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getTableHeadUtilityClass = getTableHeadUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getTableHeadUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiTableHead', slot);
}
const tableHeadClasses = (0, _utils.unstable_generateUtilityClasses)('MuiTableHead', ['root']);
var _default = tableHeadClasses;
exports.default = _default;