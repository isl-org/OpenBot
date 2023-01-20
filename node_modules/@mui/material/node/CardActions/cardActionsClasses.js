"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getCardActionsUtilityClass = getCardActionsUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getCardActionsUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiCardActions', slot);
}
const cardActionsClasses = (0, _utils.unstable_generateUtilityClasses)('MuiCardActions', ['root', 'spacing']);
var _default = cardActionsClasses;
exports.default = _default;