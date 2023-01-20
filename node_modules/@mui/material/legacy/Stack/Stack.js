import _objectWithoutProperties from "@babel/runtime/helpers/esm/objectWithoutProperties";
import _defineProperty from "@babel/runtime/helpers/esm/defineProperty";
import _typeof from "@babel/runtime/helpers/esm/typeof";
import _extends from "@babel/runtime/helpers/esm/extends";
import * as React from 'react';
import PropTypes from 'prop-types';
import { createUnarySpacing, getValue, handleBreakpoints, mergeBreakpointsInOrder, unstable_extendSxProp as extendSxProp, unstable_resolveBreakpointValues as resolveBreakpointValues } from '@mui/system';
import { deepmerge } from '@mui/utils';
import styled from '../styles/styled';
import useThemeProps from '../styles/useThemeProps';

/**
 * Return an array with the separator React element interspersed between
 * each React node of the input children.
 *
 * > joinChildren([1,2,3], 0)
 * [1,0,2,0,3]
 */
import { jsx as _jsx } from "react/jsx-runtime";
function joinChildren(children, separator) {
  var childrenArray = React.Children.toArray(children).filter(Boolean);
  return childrenArray.reduce(function (output, child, index) {
    output.push(child);
    if (index < childrenArray.length - 1) {
      output.push( /*#__PURE__*/React.cloneElement(separator, {
        key: "separator-".concat(index)
      }));
    }
    return output;
  }, []);
}
var getSideFromDirection = function getSideFromDirection(direction) {
  return {
    row: 'Left',
    'row-reverse': 'Right',
    column: 'Top',
    'column-reverse': 'Bottom'
  }[direction];
};
export var style = function style(_ref) {
  var ownerState = _ref.ownerState,
    theme = _ref.theme;
  var styles = _extends({
    display: 'flex',
    flexDirection: 'column'
  }, handleBreakpoints({
    theme: theme
  }, resolveBreakpointValues({
    values: ownerState.direction,
    breakpoints: theme.breakpoints.values
  }), function (propValue) {
    return {
      flexDirection: propValue
    };
  }));
  if (ownerState.spacing) {
    var transformer = createUnarySpacing(theme);
    var base = Object.keys(theme.breakpoints.values).reduce(function (acc, breakpoint) {
      if (_typeof(ownerState.spacing) === 'object' && ownerState.spacing[breakpoint] != null || _typeof(ownerState.direction) === 'object' && ownerState.direction[breakpoint] != null) {
        acc[breakpoint] = true;
      }
      return acc;
    }, {});
    var directionValues = resolveBreakpointValues({
      values: ownerState.direction,
      base: base
    });
    var spacingValues = resolveBreakpointValues({
      values: ownerState.spacing,
      base: base
    });
    if (_typeof(directionValues) === 'object') {
      Object.keys(directionValues).forEach(function (breakpoint, index, breakpoints) {
        var directionValue = directionValues[breakpoint];
        if (!directionValue) {
          var previousDirectionValue = index > 0 ? directionValues[breakpoints[index - 1]] : 'column';
          directionValues[breakpoint] = previousDirectionValue;
        }
      });
    }
    var styleFromPropValue = function styleFromPropValue(propValue, breakpoint) {
      return {
        '& > :not(style) + :not(style)': _defineProperty({
          margin: 0
        }, "margin".concat(getSideFromDirection(breakpoint ? directionValues[breakpoint] : ownerState.direction)), getValue(transformer, propValue))
      };
    };
    styles = deepmerge(styles, handleBreakpoints({
      theme: theme
    }, spacingValues, styleFromPropValue));
  }
  styles = mergeBreakpointsInOrder(theme.breakpoints, styles);
  return styles;
};
var StackRoot = styled('div', {
  name: 'MuiStack',
  slot: 'Root',
  overridesResolver: function overridesResolver(props, styles) {
    return [styles.root];
  }
})(style);
var Stack = /*#__PURE__*/React.forwardRef(function Stack(inProps, ref) {
  var themeProps = useThemeProps({
    props: inProps,
    name: 'MuiStack'
  });
  var props = extendSxProp(themeProps);
  var _props$component = props.component,
    component = _props$component === void 0 ? 'div' : _props$component,
    _props$direction = props.direction,
    direction = _props$direction === void 0 ? 'column' : _props$direction,
    _props$spacing = props.spacing,
    spacing = _props$spacing === void 0 ? 0 : _props$spacing,
    divider = props.divider,
    children = props.children,
    other = _objectWithoutProperties(props, ["component", "direction", "spacing", "divider", "children"]);
  var ownerState = {
    direction: direction,
    spacing: spacing
  };
  return /*#__PURE__*/_jsx(StackRoot, _extends({
    as: component,
    ownerState: ownerState,
    ref: ref
  }, other, {
    children: divider ? joinChildren(children, divider) : children
  }));
});
process.env.NODE_ENV !== "production" ? Stack.propTypes /* remove-proptypes */ = {
  // ----------------------------- Warning --------------------------------
  // | These PropTypes are generated from the TypeScript type definitions |
  // |     To update them edit the d.ts file and run "yarn proptypes"     |
  // ----------------------------------------------------------------------
  /**
   * The content of the component.
   */
  children: PropTypes.node,
  /**
   * The component used for the root node.
   * Either a string to use a HTML element or a component.
   */
  component: PropTypes.elementType,
  /**
   * Defines the `flex-direction` style property.
   * It is applied for all screen sizes.
   * @default 'column'
   */
  direction: PropTypes.oneOfType([PropTypes.oneOf(['column-reverse', 'column', 'row-reverse', 'row']), PropTypes.arrayOf(PropTypes.oneOf(['column-reverse', 'column', 'row-reverse', 'row'])), PropTypes.object]),
  /**
   * Add an element between each child.
   */
  divider: PropTypes.node,
  /**
   * Defines the space between immediate children.
   * @default 0
   */
  spacing: PropTypes.oneOfType([PropTypes.arrayOf(PropTypes.oneOfType([PropTypes.number, PropTypes.string])), PropTypes.number, PropTypes.object, PropTypes.string]),
  /**
   * The system prop, which allows defining system overrides as well as additional CSS styles.
   */
  sx: PropTypes.oneOfType([PropTypes.arrayOf(PropTypes.oneOfType([PropTypes.func, PropTypes.object, PropTypes.bool])), PropTypes.func, PropTypes.object])
} : void 0;
export default Stack;