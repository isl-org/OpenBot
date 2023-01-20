"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getBottomNavigationActionUtilityClass = getBottomNavigationActionUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getBottomNavigationActionUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiBottomNavigationAction', slot);
}
const bottomNavigationActionClasses = (0, _utils.unstable_generateUtilityClasses)('MuiBottomNavigationAction', ['root', 'iconOnly', 'selected', 'label']);
var _default = bottomNavigationActionClasses;
exports.default = _default;