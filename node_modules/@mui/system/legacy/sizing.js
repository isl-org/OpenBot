import style from './style';
import compose from './compose';
import { handleBreakpoints, values as breakpointsValues } from './breakpoints';
export function sizingTransform(value) {
  return value <= 1 && value !== 0 ? "".concat(value * 100, "%") : value;
}
export var width = style({
  prop: 'width',
  transform: sizingTransform
});
export var maxWidth = function maxWidth(props) {
  if (props.maxWidth !== undefined && props.maxWidth !== null) {
    var styleFromPropValue = function styleFromPropValue(propValue) {
      var _props$theme, _props$theme$breakpoi, _props$theme$breakpoi2;
      var breakpoint = ((_props$theme = props.theme) == null ? void 0 : (_props$theme$breakpoi = _props$theme.breakpoints) == null ? void 0 : (_props$theme$breakpoi2 = _props$theme$breakpoi.values) == null ? void 0 : _props$theme$breakpoi2[propValue]) || breakpointsValues[propValue];
      return {
        maxWidth: breakpoint || sizingTransform(propValue)
      };
    };
    return handleBreakpoints(props, props.maxWidth, styleFromPropValue);
  }
  return null;
};
maxWidth.filterProps = ['maxWidth'];
export var minWidth = style({
  prop: 'minWidth',
  transform: sizingTransform
});
export var height = style({
  prop: 'height',
  transform: sizingTransform
});
export var maxHeight = style({
  prop: 'maxHeight',
  transform: sizingTransform
});
export var minHeight = style({
  prop: 'minHeight',
  transform: sizingTransform
});
export var sizeWidth = style({
  prop: 'size',
  cssProperty: 'width',
  transform: sizingTransform
});
export var sizeHeight = style({
  prop: 'size',
  cssProperty: 'height',
  transform: sizingTransform
});
export var boxSizing = style({
  prop: 'boxSizing'
});
var sizing = compose(width, maxWidth, minWidth, height, maxHeight, minHeight, boxSizing);
export default sizing;