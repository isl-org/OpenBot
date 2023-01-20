"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getBackdropUtilityClass = getBackdropUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getBackdropUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiBackdrop', slot);
}
const backdropClasses = (0, _utils.unstable_generateUtilityClasses)('MuiBackdrop', ['root', 'invisible']);
var _default = backdropClasses;
exports.default = _default;