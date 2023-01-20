"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.sizeWidth = exports.sizeHeight = exports.minWidth = exports.minHeight = exports.maxWidth = exports.maxHeight = exports.height = exports.default = exports.boxSizing = void 0;
exports.sizingTransform = sizingTransform;
exports.width = void 0;
var _style = _interopRequireDefault(require("./style"));
var _compose = _interopRequireDefault(require("./compose"));
var _breakpoints = require("./breakpoints");
function sizingTransform(value) {
  return value <= 1 && value !== 0 ? `${value * 100}%` : value;
}
const width = (0, _style.default)({
  prop: 'width',
  transform: sizingTransform
});
exports.width = width;
const maxWidth = props => {
  if (props.maxWidth !== undefined && props.maxWidth !== null) {
    const styleFromPropValue = propValue => {
      var _props$theme, _props$theme$breakpoi, _props$theme$breakpoi2;
      const breakpoint = ((_props$theme = props.theme) == null ? void 0 : (_props$theme$breakpoi = _props$theme.breakpoints) == null ? void 0 : (_props$theme$breakpoi2 = _props$theme$breakpoi.values) == null ? void 0 : _props$theme$breakpoi2[propValue]) || _breakpoints.values[propValue];
      return {
        maxWidth: breakpoint || sizingTransform(propValue)
      };
    };
    return (0, _breakpoints.handleBreakpoints)(props, props.maxWidth, styleFromPropValue);
  }
  return null;
};
exports.maxWidth = maxWidth;
maxWidth.filterProps = ['maxWidth'];
const minWidth = (0, _style.default)({
  prop: 'minWidth',
  transform: sizingTransform
});
exports.minWidth = minWidth;
const height = (0, _style.default)({
  prop: 'height',
  transform: sizingTransform
});
exports.height = height;
const maxHeight = (0, _style.default)({
  prop: 'maxHeight',
  transform: sizingTransform
});
exports.maxHeight = maxHeight;
const minHeight = (0, _style.default)({
  prop: 'minHeight',
  transform: sizingTransform
});
exports.minHeight = minHeight;
const sizeWidth = (0, _style.default)({
  prop: 'size',
  cssProperty: 'width',
  transform: sizingTransform
});
exports.sizeWidth = sizeWidth;
const sizeHeight = (0, _style.default)({
  prop: 'size',
  cssProperty: 'height',
  transform: sizingTransform
});
exports.sizeHeight = sizeHeight;
const boxSizing = (0, _style.default)({
  prop: 'boxSizing'
});
exports.boxSizing = boxSizing;
const sizing = (0, _compose.default)(width, maxWidth, minWidth, height, maxHeight, minHeight, boxSizing);
var _default = sizing;
exports.default = _default;