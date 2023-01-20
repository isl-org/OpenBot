"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getTableFooterUtilityClass = getTableFooterUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getTableFooterUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiTableFooter', slot);
}
const tableFooterClasses = (0, _utils.unstable_generateUtilityClasses)('MuiTableFooter', ['root']);
var _default = tableFooterClasses;
exports.default = _default;