"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = exports.colorTransformations = void 0;
var _system = require("@mui/system");
const colorTransformations = {
  primary: 'primary.main',
  textPrimary: 'text.primary',
  secondary: 'secondary.main',
  textSecondary: 'text.secondary',
  error: 'error.main'
};
exports.colorTransformations = colorTransformations;
const transformDeprecatedColors = color => {
  return colorTransformations[color] || color;
};
const getTextDecoration = ({
  theme,
  ownerState
}) => {
  const transformedColor = transformDeprecatedColors(ownerState.color);
  const color = (0, _system.getPath)(theme, `palette.${transformedColor}`, false) || ownerState.color;
  const channelColor = (0, _system.getPath)(theme, `palette.${transformedColor}Channel`);
  if ('vars' in theme && channelColor) {
    return `rgba(${channelColor} / 0.4)`;
  }
  return (0, _system.alpha)(color, 0.4);
};
var _default = getTextDecoration;
exports.default = _default;