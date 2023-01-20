"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getCardMediaUtilityClass = getCardMediaUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getCardMediaUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiCardMedia', slot);
}
const cardMediaClasses = (0, _utils.unstable_generateUtilityClasses)('MuiCardMedia', ['root', 'media', 'img']);
var _default = cardMediaClasses;
exports.default = _default;