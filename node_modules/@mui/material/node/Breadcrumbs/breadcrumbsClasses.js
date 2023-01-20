"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getBreadcrumbsUtilityClass = getBreadcrumbsUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getBreadcrumbsUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiBreadcrumbs', slot);
}
const breadcrumbsClasses = (0, _utils.unstable_generateUtilityClasses)('MuiBreadcrumbs', ['root', 'ol', 'li', 'separator']);
var _default = breadcrumbsClasses;
exports.default = _default;