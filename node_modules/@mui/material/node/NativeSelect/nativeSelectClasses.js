"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
exports.getNativeSelectUtilityClasses = getNativeSelectUtilityClasses;
var _utils = require("@mui/utils");
var _generateUtilityClass = _interopRequireDefault(require("../generateUtilityClass"));
function getNativeSelectUtilityClasses(slot) {
  return (0, _generateUtilityClass.default)('MuiNativeSelect', slot);
}
const nativeSelectClasses = (0, _utils.unstable_generateUtilityClasses)('MuiNativeSelect', ['root', 'select', 'multiple', 'filled', 'outlined', 'standard', 'disabled', 'icon', 'iconOpen', 'iconFilled', 'iconOutlined', 'iconStandard', 'nativeInput']);
var _default = nativeSelectClasses;
exports.default = _default;