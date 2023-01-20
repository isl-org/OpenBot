"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getAccordionUtilityClass = getAccordionUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getAccordionUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiAccordion', slot);
}
const accordionClasses = (0, _utils.unstable_generateUtilityClasses)('MuiAccordion', ['root', 'rounded', 'expanded', 'disabled', 'gutters', 'region']);
var _default = accordionClasses;
exports.default = _default;