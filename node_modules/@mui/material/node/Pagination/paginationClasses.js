"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getPaginationUtilityClass = getPaginationUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getPaginationUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiPagination', slot);
}
const paginationClasses = (0, _utils.unstable_generateUtilityClasses)('MuiPagination', ['root', 'ul', 'outlined', 'text']);
var _default = paginationClasses;
exports.default = _default;