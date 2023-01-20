"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getTableContainerUtilityClass = getTableContainerUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getTableContainerUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiTableContainer', slot);
}
const tableContainerClasses = (0, _utils.unstable_generateUtilityClasses)('MuiTableContainer', ['root']);
var _default = tableContainerClasses;
exports.default = _default;