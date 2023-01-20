import _objectWithoutProperties from "@babel/runtime/helpers/esm/objectWithoutProperties";
import _defineProperty from "@babel/runtime/helpers/esm/defineProperty";
import _extends from "@babel/runtime/helpers/esm/extends";
import * as React from 'react';
import PropTypes from 'prop-types';
import clsx from 'clsx';
import { usePreviousProps } from '@mui/utils';
import composeClasses from '@mui/base/composeClasses';
import BadgeUnstyled from '@mui/base/BadgeUnstyled';
import styled from '../styles/styled';
import useThemeProps from '../styles/useThemeProps';
import shouldSpreadAdditionalProps from '../utils/shouldSpreadAdditionalProps';
import capitalize from '../utils/capitalize';
import badgeClasses, { getBadgeUtilityClass } from './badgeClasses';
import { jsx as _jsx } from "react/jsx-runtime";
var RADIUS_STANDARD = 10;
var RADIUS_DOT = 4;
var useUtilityClasses = function useUtilityClasses(ownerState) {
  var color = ownerState.color,
    anchorOrigin = ownerState.anchorOrigin,
    invisible = ownerState.invisible,
    overlap = ownerState.overlap,
    variant = ownerState.variant,
    _ownerState$classes = ownerState.classes,
    classes = _ownerState$classes === void 0 ? {} : _ownerState$classes;
  var slots = {
    root: ['root'],
    badge: ['badge', variant, invisible && 'invisible', "anchorOrigin".concat(capitalize(anchorOrigin.vertical)).concat(capitalize(anchorOrigin.horizontal)), "anchorOrigin".concat(capitalize(anchorOrigin.vertical)).concat(capitalize(anchorOrigin.horizontal)).concat(capitalize(overlap)), "overlap".concat(capitalize(overlap)), color !== 'default' && "color".concat(capitalize(color))]
  };
  return composeClasses(slots, getBadgeUtilityClass, classes);
};
var BadgeRoot = styled('span', {
  name: 'MuiBadge',
  slot: 'Root',
  overridesResolver: function overridesResolver(props, styles) {
    return styles.root;
  }
})({
  position: 'relative',
  display: 'inline-flex',
  // For correct alignment with the text.
  verticalAlign: 'middle',
  flexShrink: 0
});
var BadgeBadge = styled('span', {
  name: 'MuiBadge',
  slot: 'Badge',
  overridesResolver: function overridesResolver(props, styles) {
    var ownerState = props.ownerState;
    return [styles.badge, styles[ownerState.variant], styles["anchorOrigin".concat(capitalize(ownerState.anchorOrigin.vertical)).concat(capitalize(ownerState.anchorOrigin.horizontal)).concat(capitalize(ownerState.overlap))], ownerState.color !== 'default' && styles["color".concat(capitalize(ownerState.color))], ownerState.invisible && styles.invisible];
  }
})(function (_ref) {
  var theme = _ref.theme,
    ownerState = _ref.ownerState;
  return _extends({
    display: 'flex',
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'center',
    alignContent: 'center',
    alignItems: 'center',
    position: 'absolute',
    boxSizing: 'border-box',
    fontFamily: theme.typography.fontFamily,
    fontWeight: theme.typography.fontWeightMedium,
    fontSize: theme.typography.pxToRem(12),
    minWidth: RADIUS_STANDARD * 2,
    lineHeight: 1,
    padding: '0 6px',
    height: RADIUS_STANDARD * 2,
    borderRadius: RADIUS_STANDARD,
    zIndex: 1,
    // Render the badge on top of potential ripples.
    transition: theme.transitions.create('transform', {
      easing: theme.transitions.easing.easeInOut,
      duration: theme.transitions.duration.enteringScreen
    })
  }, ownerState.color !== 'default' && {
    backgroundColor: (theme.vars || theme).palette[ownerState.color].main,
    color: (theme.vars || theme).palette[ownerState.color].contrastText
  }, ownerState.variant === 'dot' && {
    borderRadius: RADIUS_DOT,
    height: RADIUS_DOT * 2,
    minWidth: RADIUS_DOT * 2,
    padding: 0
  }, ownerState.anchorOrigin.vertical === 'top' && ownerState.anchorOrigin.horizontal === 'right' && ownerState.overlap === 'rectangular' && _defineProperty({
    top: 0,
    right: 0,
    transform: 'scale(1) translate(50%, -50%)',
    transformOrigin: '100% 0%'
  }, "&.".concat(badgeClasses.invisible), {
    transform: 'scale(0) translate(50%, -50%)'
  }), ownerState.anchorOrigin.vertical === 'bottom' && ownerState.anchorOrigin.horizontal === 'right' && ownerState.overlap === 'rectangular' && _defineProperty({
    bottom: 0,
    right: 0,
    transform: 'scale(1) translate(50%, 50%)',
    transformOrigin: '100% 100%'
  }, "&.".concat(badgeClasses.invisible), {
    transform: 'scale(0) translate(50%, 50%)'
  }), ownerState.anchorOrigin.vertical === 'top' && ownerState.anchorOrigin.horizontal === 'left' && ownerState.overlap === 'rectangular' && _defineProperty({
    top: 0,
    left: 0,
    transform: 'scale(1) translate(-50%, -50%)',
    transformOrigin: '0% 0%'
  }, "&.".concat(badgeClasses.invisible), {
    transform: 'scale(0) translate(-50%, -50%)'
  }), ownerState.anchorOrigin.vertical === 'bottom' && ownerState.anchorOrigin.horizontal === 'left' && ownerState.overlap === 'rectangular' && _defineProperty({
    bottom: 0,
    left: 0,
    transform: 'scale(1) translate(-50%, 50%)',
    transformOrigin: '0% 100%'
  }, "&.".concat(badgeClasses.invisible), {
    transform: 'scale(0) translate(-50%, 50%)'
  }), ownerState.anchorOrigin.vertical === 'top' && ownerState.anchorOrigin.horizontal === 'right' && ownerState.overlap === 'circular' && _defineProperty({
    top: '14%',
    right: '14%',
    transform: 'scale(1) translate(50%, -50%)',
    transformOrigin: '100% 0%'
  }, "&.".concat(badgeClasses.invisible), {
    transform: 'scale(0) translate(50%, -50%)'
  }), ownerState.anchorOrigin.vertical === 'bottom' && ownerState.anchorOrigin.horizontal === 'right' && ownerState.overlap === 'circular' && _defineProperty({
    bottom: '14%',
    right: '14%',
    transform: 'scale(1) translate(50%, 50%)',
    transformOrigin: '100% 100%'
  }, "&.".concat(badgeClasses.invisible), {
    transform: 'scale(0) translate(50%, 50%)'
  }), ownerState.anchorOrigin.vertical === 'top' && ownerState.anchorOrigin.horizontal === 'left' && ownerState.overlap === 'circular' && _defineProperty({
    top: '14%',
    left: '14%',
    transform: 'scale(1) translate(-50%, -50%)',
    transformOrigin: '0% 0%'
  }, "&.".concat(badgeClasses.invisible), {
    transform: 'scale(0) translate(-50%, -50%)'
  }), ownerState.anchorOrigin.vertical === 'bottom' && ownerState.anchorOrigin.horizontal === 'left' && ownerState.overlap === 'circular' && _defineProperty({
    bottom: '14%',
    left: '14%',
    transform: 'scale(1) translate(-50%, 50%)',
    transformOrigin: '0% 100%'
  }, "&.".concat(badgeClasses.invisible), {
    transform: 'scale(0) translate(-50%, 50%)'
  }), ownerState.invisible && {
    transition: theme.transitions.create('transform', {
      easing: theme.transitions.easing.easeInOut,
      duration: theme.transitions.duration.leavingScreen
    })
  });
});
var Badge = /*#__PURE__*/React.forwardRef(function Badge(inProps, ref) {
  var _ref11, _slots$root, _ref12, _slots$badge, _slotProps$root, _slotProps$badge;
  var props = useThemeProps({
    props: inProps,
    name: 'MuiBadge'
  });
  var _props$anchorOrigin = props.anchorOrigin,
    anchorOriginProp = _props$anchorOrigin === void 0 ? {
      vertical: 'top',
      horizontal: 'right'
    } : _props$anchorOrigin,
    className = props.className,
    _props$component = props.component,
    component = _props$component === void 0 ? 'span' : _props$component,
    _props$components = props.components,
    components = _props$components === void 0 ? {} : _props$components,
    _props$componentsProp = props.componentsProps,
    componentsProps = _props$componentsProp === void 0 ? {} : _props$componentsProp,
    _props$overlap = props.overlap,
    overlapProp = _props$overlap === void 0 ? 'rectangular' : _props$overlap,
    _props$color = props.color,
    colorProp = _props$color === void 0 ? 'default' : _props$color,
    _props$invisible = props.invisible,
    invisibleProp = _props$invisible === void 0 ? false : _props$invisible,
    max = props.max,
    badgeContentProp = props.badgeContent,
    slots = props.slots,
    slotProps = props.slotProps,
    _props$showZero = props.showZero,
    showZero = _props$showZero === void 0 ? false : _props$showZero,
    _props$variant = props.variant,
    variantProp = _props$variant === void 0 ? 'standard' : _props$variant,
    other = _objectWithoutProperties(props, ["anchorOrigin", "className", "component", "components", "componentsProps", "overlap", "color", "invisible", "max", "badgeContent", "slots", "slotProps", "showZero", "variant"]);
  var prevProps = usePreviousProps({
    anchorOrigin: anchorOriginProp,
    color: colorProp,
    overlap: overlapProp,
    variant: variantProp
  });
  var invisible = invisibleProp;
  if (invisibleProp === false && (badgeContentProp === 0 && !showZero || badgeContentProp == null && variantProp !== 'dot')) {
    invisible = true;
  }
  var _ref10 = invisible ? prevProps : props,
    _ref10$color = _ref10.color,
    color = _ref10$color === void 0 ? colorProp : _ref10$color,
    _ref10$overlap = _ref10.overlap,
    overlap = _ref10$overlap === void 0 ? overlapProp : _ref10$overlap,
    _ref10$anchorOrigin = _ref10.anchorOrigin,
    anchorOrigin = _ref10$anchorOrigin === void 0 ? anchorOriginProp : _ref10$anchorOrigin,
    _ref10$variant = _ref10.variant,
    variant = _ref10$variant === void 0 ? variantProp : _ref10$variant;
  var ownerState = _extends({}, props, {
    anchorOrigin: anchorOrigin,
    invisible: invisible,
    color: color,
    overlap: overlap,
    variant: variant
  });
  var classes = useUtilityClasses(ownerState);
  var displayValue;
  if (variant !== 'dot') {
    displayValue = badgeContentProp && Number(badgeContentProp) > max ? "".concat(max, "+") : badgeContentProp;
  }

  // support both `slots` and `components` for backward compatibility
  var RootSlot = (_ref11 = (_slots$root = slots == null ? void 0 : slots.root) != null ? _slots$root : components.Root) != null ? _ref11 : BadgeRoot;
  var BadgeSlot = (_ref12 = (_slots$badge = slots == null ? void 0 : slots.badge) != null ? _slots$badge : components.Badge) != null ? _ref12 : BadgeBadge;
  var rootSlotProps = (_slotProps$root = slotProps == null ? void 0 : slotProps.root) != null ? _slotProps$root : componentsProps.root;
  var badgeSlotProps = (_slotProps$badge = slotProps == null ? void 0 : slotProps.badge) != null ? _slotProps$badge : componentsProps.badge;
  return /*#__PURE__*/_jsx(BadgeUnstyled, _extends({
    invisible: invisibleProp,
    badgeContent: displayValue,
    showZero: showZero,
    max: max
  }, other, {
    slots: {
      root: RootSlot,
      badge: BadgeSlot
    },
    className: clsx(rootSlotProps == null ? void 0 : rootSlotProps.className, classes.root, className),
    slotProps: {
      root: _extends({}, rootSlotProps, shouldSpreadAdditionalProps(RootSlot) && {
        as: component,
        ownerState: _extends({}, rootSlotProps == null ? void 0 : rootSlotProps.ownerState, {
          anchorOrigin: anchorOrigin,
          color: color,
          overlap: overlap,
          variant: variant
        })
      }),
      badge: _extends({}, badgeSlotProps, {
        className: clsx(classes.badge, badgeSlotProps == null ? void 0 : badgeSlotProps.className)
      }, shouldSpreadAdditionalProps(BadgeSlot) && {
        ownerState: _extends({}, badgeSlotProps == null ? void 0 : badgeSlotProps.ownerState, {
          anchorOrigin: anchorOrigin,
          color: color,
          overlap: overlap,
          variant: variant
        })
      })
    },
    ref: ref
  }));
});
process.env.NODE_ENV !== "production" ? Badge.propTypes /* remove-proptypes */ = {
  // ----------------------------- Warning --------------------------------
  // | These PropTypes are generated from the TypeScript type definitions |
  // |     To update them edit the d.ts file and run "yarn proptypes"     |
  // ----------------------------------------------------------------------
  /**
   * The anchor of the badge.
   * @default {
   *   vertical: 'top',
   *   horizontal: 'right',
   * }
   */
  anchorOrigin: PropTypes.shape({
    horizontal: PropTypes.oneOf(['left', 'right']).isRequired,
    vertical: PropTypes.oneOf(['bottom', 'top']).isRequired
  }),
  /**
   * The content rendered within the badge.
   */
  badgeContent: PropTypes.node,
  /**
   * The badge will be added relative to this node.
   */
  children: PropTypes.node,
  /**
   * Override or extend the styles applied to the component.
   */
  classes: PropTypes.object,
  /**
   * @ignore
   */
  className: PropTypes.string,
  /**
   * The color of the component.
   * It supports both default and custom theme colors, which can be added as shown in the
   * [palette customization guide](https://mui.com/material-ui/customization/palette/#adding-new-colors).
   * @default 'default'
   */
  color: PropTypes /* @typescript-to-proptypes-ignore */.oneOfType([PropTypes.oneOf(['default', 'primary', 'secondary', 'error', 'info', 'success', 'warning']), PropTypes.string]),
  /**
   * The component used for the root node.
   * Either a string to use a HTML element or a component.
   */
  component: PropTypes.elementType,
  /**
   * The components used for each slot inside.
   *
   * This prop is an alias for the `slots` prop.
   * It's recommended to use the `slots` prop instead.
   *
   * @default {}
   */
  components: PropTypes.shape({
    Badge: PropTypes.elementType,
    Root: PropTypes.elementType
  }),
  /**
   * The extra props for the slot components.
   * You can override the existing props or add new ones.
   *
   * This prop is an alias for the `slotProps` prop.
   * It's recommended to use the `slotProps` prop instead, as `componentsProps` will be deprecated in the future.
   *
   * @default {}
   */
  componentsProps: PropTypes.shape({
    badge: PropTypes.oneOfType([PropTypes.func, PropTypes.object]),
    root: PropTypes.oneOfType([PropTypes.func, PropTypes.object])
  }),
  /**
   * If `true`, the badge is invisible.
   * @default false
   */
  invisible: PropTypes.bool,
  /**
   * Max count to show.
   * @default 99
   */
  max: PropTypes.number,
  /**
   * Wrapped shape the badge should overlap.
   * @default 'rectangular'
   */
  overlap: PropTypes.oneOf(['circular', 'rectangular']),
  /**
   * Controls whether the badge is hidden when `badgeContent` is zero.
   * @default false
   */
  showZero: PropTypes.bool,
  /**
   * The props used for each slot inside the Badge.
   * @default {}
   */
  slotProps: PropTypes.shape({
    badge: PropTypes.oneOfType([PropTypes.func, PropTypes.object]),
    root: PropTypes.oneOfType([PropTypes.func, PropTypes.object])
  }),
  /**
   * The components used for each slot inside the Badge.
   * Either a string to use a HTML element or a component.
   * @default {}
   */
  slots: PropTypes.shape({
    badge: PropTypes.elementType,
    root: PropTypes.elementType
  }),
  /**
   * The system prop that allows defining system overrides as well as additional CSS styles.
   */
  sx: PropTypes.oneOfType([PropTypes.arrayOf(PropTypes.oneOfType([PropTypes.func, PropTypes.object, PropTypes.bool])), PropTypes.func, PropTypes.object]),
  /**
   * The variant to use.
   * @default 'standard'
   */
  variant: PropTypes /* @typescript-to-proptypes-ignore */.oneOfType([PropTypes.oneOf(['dot', 'standard']), PropTypes.string])
} : void 0;
export default Badge;