"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getSelectUtilityClasses = getSelectUtilityClasses;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getSelectUtilityClasses(slot) {
  return (0, _generateUtilityClass.default)('MuiSelect', slot);
}
const selectClasses = (0, _utils.unstable_generateUtilityClasses)('MuiSelect', ['select', 'multiple', 'filled', 'outlined', 'standard', 'disabled', 'focused', 'icon', 'iconOpen', 'iconFilled', 'iconOutlined', 'iconStandard', 'nativeInput']);
var _default = selectClasses;
exports.default = _default;