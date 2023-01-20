"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getListUtilityClass = getListUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getListUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiList', slot);
}
const listClasses = (0, _utils.unstable_generateUtilityClasses)('MuiList', ['root', 'padding', 'dense', 'subheader']);
var _default = listClasses;
exports.default = _default;