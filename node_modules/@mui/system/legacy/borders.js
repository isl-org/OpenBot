import responsivePropType from './responsivePropType';
import style from './style';
import compose from './compose';
import { createUnaryUnit, getValue } from './spacing';
import { handleBreakpoints } from './breakpoints';
export function borderTransform(value) {
  if (typeof value !== 'number') {
    return value;
  }
  return "".concat(value, "px solid");
}
export var border = style({
  prop: 'border',
  themeKey: 'borders',
  transform: borderTransform
});
export var borderTop = style({
  prop: 'borderTop',
  themeKey: 'borders',
  transform: borderTransform
});
export var borderRight = style({
  prop: 'borderRight',
  themeKey: 'borders',
  transform: borderTransform
});
export var borderBottom = style({
  prop: 'borderBottom',
  themeKey: 'borders',
  transform: borderTransform
});
export var borderLeft = style({
  prop: 'borderLeft',
  themeKey: 'borders',
  transform: borderTransform
});
export var borderColor = style({
  prop: 'borderColor',
  themeKey: 'palette'
});
export var borderTopColor = style({
  prop: 'borderTopColor',
  themeKey: 'palette'
});
export var borderRightColor = style({
  prop: 'borderRightColor',
  themeKey: 'palette'
});
export var borderBottomColor = style({
  prop: 'borderBottomColor',
  themeKey: 'palette'
});
export var borderLeftColor = style({
  prop: 'borderLeftColor',
  themeKey: 'palette'
});

// false positive
// eslint-disable-next-line react/function-component-definition
export var borderRadius = function borderRadius(props) {
  if (props.borderRadius !== undefined && props.borderRadius !== null) {
    var transformer = createUnaryUnit(props.theme, 'shape.borderRadius', 4, 'borderRadius');
    var styleFromPropValue = function styleFromPropValue(propValue) {
      return {
        borderRadius: getValue(transformer, propValue)
      };
    };
    return handleBreakpoints(props, props.borderRadius, styleFromPropValue);
  }
  return null;
};
borderRadius.propTypes = process.env.NODE_ENV !== 'production' ? {
  borderRadius: responsivePropType
} : {};
borderRadius.filterProps = ['borderRadius'];
var borders = compose(border, borderTop, borderRight, borderBottom, borderLeft, borderColor, borderTopColor, borderRightColor, borderBottomColor, borderLeftColor, borderRadius);
export default borders;