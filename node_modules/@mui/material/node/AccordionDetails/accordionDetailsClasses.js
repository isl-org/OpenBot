"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getAccordionDetailsUtilityClass = getAccordionDetailsUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getAccordionDetailsUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiAccordionDetails', slot);
}
const accordionDetailsClasses = (0, _utils.unstable_generateUtilityClasses)('MuiAccordionDetails', ['root']);
var _default = accordionDetailsClasses;
exports.default = _default;