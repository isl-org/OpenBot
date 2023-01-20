"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getCardHeaderUtilityClass = getCardHeaderUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getCardHeaderUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiCardHeader', slot);
}
const cardHeaderClasses = (0, _utils.unstable_generateUtilityClasses)('MuiCardHeader', ['root', 'avatar', 'action', 'content', 'title', 'subheader']);
var _default = cardHeaderClasses;
exports.default = _default;