"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getTableBodyUtilityClass = getTableBodyUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getTableBodyUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiTableBody', slot);
}
const tableBodyClasses = (0, _utils.unstable_generateUtilityClasses)('MuiTableBody', ['root']);
var _default = tableBodyClasses;
exports.default = _default;