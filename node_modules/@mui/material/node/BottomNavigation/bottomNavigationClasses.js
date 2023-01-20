"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getBottomNavigationUtilityClass = getBottomNavigationUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getBottomNavigationUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiBottomNavigation', slot);
}
const bottomNavigationClasses = (0, _utils.unstable_generateUtilityClasses)('MuiBottomNavigation', ['root']);
var _default = bottomNavigationClasses;
exports.default = _default;