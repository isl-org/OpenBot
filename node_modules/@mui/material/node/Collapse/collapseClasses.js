"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getCollapseUtilityClass = getCollapseUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getCollapseUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiCollapse', slot);
}
const collapseClasses = (0, _utils.unstable_generateUtilityClasses)('MuiCollapse', ['root', 'horizontal', 'vertical', 'entered', 'hidden', 'wrapper', 'wrapperInner']);
var _default = collapseClasses;
exports.default = _default;