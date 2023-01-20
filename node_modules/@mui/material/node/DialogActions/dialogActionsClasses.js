"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getDialogActionsUtilityClass = getDialogActionsUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getDialogActionsUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiDialogActions', slot);
}
const dialogActionsClasses = (0, _utils.unstable_generateUtilityClasses)('MuiDialogActions', ['root', 'spacing']);
var _default = dialogActionsClasses;
exports.default = _default;