import _extends from "@babel/runtime/helpers/esm/extends";
import _objectWithoutPropertiesLoose from "@babel/runtime/helpers/esm/objectWithoutPropertiesLoose";
const _excluded = ["className", "columns", "container", "component", "direction", "wrap", "spacing", "rowSpacing", "columnSpacing", "disableEqualOverflow"];
import * as React from 'react';
import PropTypes from 'prop-types';
import clsx from 'clsx';
import { unstable_composeClasses as composeClasses, unstable_generateUtilityClass as generateUtilityClass } from '@mui/utils';
import systemStyled from '../styled';
import useThemePropsSystem from '../useThemeProps';
import useTheme from '../useTheme';
import { extendSxProp } from '../styleFunctionSx';
import createTheme from '../createTheme';
import { generateGridStyles, generateGridSizeStyles, generateGridColumnsStyles, generateGridColumnSpacingStyles, generateGridRowSpacingStyles, generateGridDirectionStyles, generateGridOffsetStyles, generateSizeClassNames, generateSpacingClassNames, generateDirectionClasses } from './gridGenerator';
import { jsx as _jsx } from "react/jsx-runtime";
const defaultTheme = createTheme();

// widening Theme to any so that the consumer can own the theme structure.
const defaultCreateStyledComponent = systemStyled('div', {
  name: 'MuiGrid',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
});
function useThemePropsDefault(props) {
  return useThemePropsSystem({
    props,
    name: 'MuiGrid',
    defaultTheme
  });
}
export default function createGrid(options = {}) {
  const {
    // This will allow adding custom styled fn (for example for custom sx style function)
    createStyledComponent = defaultCreateStyledComponent,
    useThemeProps = useThemePropsDefault,
    componentName = 'MuiGrid'
  } = options;
  const NestedContext = /*#__PURE__*/React.createContext(false);
  const OverflowContext = /*#__PURE__*/React.createContext(undefined);
  const useUtilityClasses = (ownerState, theme) => {
    const {
      container,
      direction,
      spacing,
      wrap,
      gridSize
    } = ownerState;
    const slots = {
      root: ['root', container && 'container', wrap !== 'wrap' && `wrap-xs-${String(wrap)}`, ...generateDirectionClasses(direction), ...generateSizeClassNames(gridSize), ...(container ? generateSpacingClassNames(spacing, theme.breakpoints.keys[0]) : [])]
    };
    return composeClasses(slots, slot => generateUtilityClass(componentName, slot), {});
  };
  const GridRoot = createStyledComponent(generateGridColumnsStyles, generateGridColumnSpacingStyles, generateGridRowSpacingStyles, generateGridSizeStyles, generateGridDirectionStyles, generateGridStyles, generateGridOffsetStyles);
  const Grid = /*#__PURE__*/React.forwardRef(function Grid(inProps, ref) {
    const theme = useTheme();
    const themeProps = useThemeProps(inProps);
    const props = extendSxProp(themeProps); // `color` type conflicts with html color attribute.
    const nested = React.useContext(NestedContext);
    const overflow = React.useContext(OverflowContext);
    const {
        className,
        columns: columnsProp = 12,
        container = false,
        component = 'div',
        direction = 'row',
        wrap = 'wrap',
        spacing: spacingProp = 0,
        rowSpacing: rowSpacingProp = spacingProp,
        columnSpacing: columnSpacingProp = spacingProp,
        disableEqualOverflow: themeDisableEqualOverflow
      } = props,
      rest = _objectWithoutPropertiesLoose(props, _excluded);
    // Because `disableEqualOverflow` can be set from the theme's defaultProps, the **nested** grid should look at the instance props instead.
    let disableEqualOverflow = themeDisableEqualOverflow;
    if (nested && themeDisableEqualOverflow !== undefined) {
      disableEqualOverflow = inProps.disableEqualOverflow;
    }
    // collect breakpoints related props because they can be customized from the theme.
    const gridSize = {};
    const gridOffset = {};
    const other = {};
    Object.entries(rest).forEach(([key, val]) => {
      if (theme.breakpoints.values[key] !== undefined) {
        gridSize[key] = val;
      } else if (theme.breakpoints.values[key.replace('Offset', '')] !== undefined) {
        gridOffset[key.replace('Offset', '')] = val;
      } else {
        other[key] = val;
      }
    });
    const columns = inProps.columns ?? (nested ? undefined : columnsProp);
    const spacing = inProps.spacing ?? (nested ? undefined : spacingProp);
    const rowSpacing = inProps.rowSpacing ?? inProps.spacing ?? (nested ? undefined : rowSpacingProp);
    const columnSpacing = inProps.columnSpacing ?? inProps.spacing ?? (nested ? undefined : columnSpacingProp);
    const ownerState = _extends({}, props, {
      nested,
      columns,
      container,
      direction,
      wrap,
      spacing,
      rowSpacing,
      columnSpacing,
      gridSize,
      gridOffset,
      disableEqualOverflow: disableEqualOverflow ?? overflow ?? false,
      // use context value if exists.
      parentDisableEqualOverflow: overflow // for nested grid
    });

    const classes = useUtilityClasses(ownerState, theme);
    let result = /*#__PURE__*/_jsx(GridRoot, _extends({
      ref: ref,
      as: component,
      ownerState: ownerState,
      className: clsx(classes.root, className)
    }, other));
    if (!nested) {
      result = /*#__PURE__*/_jsx(NestedContext.Provider, {
        value: true,
        children: result
      });
    }
    if (disableEqualOverflow !== undefined && disableEqualOverflow !== (overflow ?? false)) {
      // There are 2 possibilities that should wrap with the OverflowContext to communicate with the nested grids:
      // 1. It is the root grid with `disableEqualOverflow`.
      // 2. It is a nested grid with different `disableEqualOverflow` from the context.
      result = /*#__PURE__*/_jsx(OverflowContext.Provider, {
        value: disableEqualOverflow,
        children: result
      });
    }
    return result;
  });
  process.env.NODE_ENV !== "production" ? Grid.propTypes /* remove-proptypes */ = {
    children: PropTypes.node,
    className: PropTypes.string,
    columns: PropTypes.oneOfType([PropTypes.arrayOf(PropTypes.number), PropTypes.number, PropTypes.object]),
    columnSpacing: PropTypes.oneOfType([PropTypes.arrayOf(PropTypes.oneOfType([PropTypes.number, PropTypes.string])), PropTypes.number, PropTypes.object, PropTypes.string]),
    component: PropTypes.elementType,
    container: PropTypes.bool,
    direction: PropTypes.oneOfType([PropTypes.oneOf(['column-reverse', 'column', 'row-reverse', 'row']), PropTypes.arrayOf(PropTypes.oneOf(['column-reverse', 'column', 'row-reverse', 'row'])), PropTypes.object]),
    disableEqualOverflow: PropTypes.bool,
    lg: PropTypes.oneOfType([PropTypes.oneOf(['auto']), PropTypes.number, PropTypes.bool]),
    lgOffset: PropTypes.oneOfType([PropTypes.oneOf(['auto']), PropTypes.number]),
    md: PropTypes.oneOfType([PropTypes.oneOf(['auto']), PropTypes.number, PropTypes.bool]),
    mdOffset: PropTypes.oneOfType([PropTypes.oneOf(['auto']), PropTypes.number]),
    rowSpacing: PropTypes.oneOfType([PropTypes.arrayOf(PropTypes.oneOfType([PropTypes.number, PropTypes.string])), PropTypes.number, PropTypes.object, PropTypes.string]),
    sm: PropTypes.oneOfType([PropTypes.oneOf(['auto']), PropTypes.number, PropTypes.bool]),
    smOffset: PropTypes.oneOfType([PropTypes.oneOf(['auto']), PropTypes.number]),
    spacing: PropTypes.oneOfType([PropTypes.arrayOf(PropTypes.oneOfType([PropTypes.number, PropTypes.string])), PropTypes.number, PropTypes.object, PropTypes.string]),
    sx: PropTypes.oneOfType([PropTypes.arrayOf(PropTypes.oneOfType([PropTypes.func, PropTypes.object, PropTypes.bool])), PropTypes.func, PropTypes.object]),
    wrap: PropTypes.oneOf(['nowrap', 'wrap-reverse', 'wrap']),
    xl: PropTypes.oneOfType([PropTypes.oneOf(['auto']), PropTypes.number, PropTypes.bool]),
    xlOffset: PropTypes.oneOfType([PropTypes.oneOf(['auto']), PropTypes.number]),
    xs: PropTypes.oneOfType([PropTypes.oneOf(['auto']), PropTypes.number, PropTypes.bool]),
    xsOffset: PropTypes.oneOfType([PropTypes.oneOf(['auto']), PropTypes.number])
  } : void 0;
  return Grid;
}