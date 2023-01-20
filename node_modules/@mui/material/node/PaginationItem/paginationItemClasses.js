"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getPaginationItemUtilityClass = getPaginationItemUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getPaginationItemUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiPaginationItem', slot);
}
const paginationItemClasses = (0, _utils.unstable_generateUtilityClasses)('MuiPaginationItem', ['root', 'page', 'sizeSmall', 'sizeLarge', 'text', 'textPrimary', 'textSecondary', 'outlined', 'outlinedPrimary', 'outlinedSecondary', 'rounded', 'ellipsis', 'firstLast', 'previousNext', 'focusVisible', 'disabled', 'selected', 'icon']);
var _default = paginationItemClasses;
exports.default = _default;