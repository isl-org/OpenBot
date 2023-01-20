"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
const defaultOptionStringifier = option => {
  const {
    label,
    value
  } = option;
  if (typeof label === 'string') {
    return label;
  }
  if (typeof value === 'string') {
    return value;
  }

  // Fallback string representation
  return String(option);
};
var _default = defaultOptionStringifier;
exports.default = _default;