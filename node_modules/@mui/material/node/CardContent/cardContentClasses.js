"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getCardContentUtilityClass = getCardContentUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getCardContentUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiCardContent', slot);
}
const cardContentClasses = (0, _utils.unstable_generateUtilityClasses)('MuiCardContent', ['root']);
var _default = cardContentClasses;
exports.default = _default;