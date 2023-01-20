"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getTableUtilityClass = getTableUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getTableUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiTable', slot);
}
const tableClasses = (0, _utils.unstable_generateUtilityClasses)('MuiTable', ['root', 'stickyHeader']);
var _default = tableClasses;
exports.default = _default;