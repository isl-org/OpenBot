"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = getThemeProps;
var _utils = require("@mui/utils");
function getThemeProps(params) {
  const {
    theme,
    name,
    props
  } = params;
  if (!theme || !theme.components || !theme.components[name] || !theme.components[name].defaultProps) {
    return props;
  }
  return (0, _utils.internal_resolveProps)(theme.components[name].defaultProps, props);
}