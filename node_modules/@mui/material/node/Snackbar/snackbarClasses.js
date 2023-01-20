"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getSnackbarUtilityClass = getSnackbarUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getSnackbarUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiSnackbar', slot);
}
const snackbarClasses = (0, _utils.unstable_generateUtilityClasses)('MuiSnackbar', ['root', 'anchorOriginTopCenter', 'anchorOriginBottomCenter', 'anchorOriginTopRight', 'anchorOriginBottomRight', 'anchorOriginTopLeft', 'anchorOriginBottomLeft']);
var _default = snackbarClasses;
exports.default = _default;