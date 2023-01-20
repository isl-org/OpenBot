"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getDialogTitleUtilityClass = getDialogTitleUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getDialogTitleUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiDialogTitle', slot);
}
const dialogTitleClasses = (0, _utils.unstable_generateUtilityClasses)('MuiDialogTitle', ['root']);
var _default = dialogTitleClasses;
exports.default = _default;