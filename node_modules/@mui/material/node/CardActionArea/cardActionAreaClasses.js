"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getCardActionAreaUtilityClass = getCardActionAreaUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getCardActionAreaUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiCardActionArea', slot);
}
const cardActionAreaClasses = (0, _utils.unstable_generateUtilityClasses)('MuiCardActionArea', ['root', 'focusVisible', 'focusHighlight']);
var _default = cardActionAreaClasses;
exports.default = _default;