"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getAlertTitleUtilityClass = getAlertTitleUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getAlertTitleUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiAlertTitle', slot);
}
const alertTitleClasses = (0, _utils.unstable_generateUtilityClasses)('MuiAlertTitle', ['root']);
var _default = alertTitleClasses;
exports.default = _default;