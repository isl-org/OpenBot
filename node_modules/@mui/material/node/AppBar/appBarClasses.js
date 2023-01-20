"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getAppBarUtilityClass = getAppBarUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getAppBarUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiAppBar', slot);
}
const appBarClasses = (0, _utils.unstable_generateUtilityClasses)('MuiAppBar', ['root', 'positionFixed', 'positionAbsolute', 'positionSticky', 'positionStatic', 'positionRelative', 'colorDefault', 'colorPrimary', 'colorSecondary', 'colorInherit', 'colorTransparent']);
var _default = appBarClasses;
exports.default = _default;