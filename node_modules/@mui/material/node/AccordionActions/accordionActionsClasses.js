"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getAccordionActionsUtilityClass = getAccordionActionsUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getAccordionActionsUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiAccordionActions', slot);
}
const accordionActionsClasses = (0, _utils.unstable_generateUtilityClasses)('MuiAccordionActions', ['root', 'spacing']);
var _default = accordionActionsClasses;
exports.default = _default;