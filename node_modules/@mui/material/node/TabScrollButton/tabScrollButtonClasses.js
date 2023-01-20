"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getTabScrollButtonUtilityClass = getTabScrollButtonUtilityClass;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getTabScrollButtonUtilityClass(slot) {
  return (0, _generateUtilityClass.default)('MuiTabScrollButton', slot);
}
const tabScrollButtonClasses = (0, _utils.unstable_generateUtilityClasses)('MuiTabScrollButton', ['root', 'vertical', 'horizontal', 'disabled']);
var _default = tabScrollButtonClasses;
exports.default = _default;