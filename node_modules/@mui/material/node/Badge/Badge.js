"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
var _objectWithoutPropertiesLoose2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutPropertiesLoose"));
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var React = _interopRequireWildcard(require("react"));
var _propTypes = _interopRequireDefault(require("prop-types"));
var _clsx = _interopRequireDefault(require("clsx"));
var _utils = require("@mui/utils");
var _composeClasses = _interopRequireDefault(require("@mui/base/composeClasses"));
var _BadgeUnstyled = _interopRequireDefault(require("@mui/base/BadgeUnstyled"));
var _styled = _interopRequireDefault(require("../styles/styled"));
var _useThemeProps = _interopRequireDefault(require("../styles/useThemeProps"));
var _shouldSpreadAdditionalProps = _interopRequireDefault(require("../utils/shouldSpreadAdditionalProps"));
var _capitalize = _interopRequireDefault(require("../utils/capitalize"));
var _badgeClasses = _interopRequireWildcard(require("./badgeClasses"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["anchorOrigin", "className", "component", "components", "componentsProps", "overlap", "color", "invisible", "max", "badgeContent", "slots", "slotProps", "showZero", "variant"];
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
const RADIUS_STANDARD = 10;
const RADIUS_DOT = 4;
const useUtilityClasses = ownerState => {
  const {
    color,
    anchorOrigin,
    invisible,
    overlap,
    variant,
    classes = {}
  } = ownerState;
  const slots = {
    root: ['root'],
    badge: ['badge', variant, invisible && 'invisible', `anchorOrigin${(0, _capitalize.default)(anchorOrigin.vertical)}${(0, _capitalize.default)(anchorOrigin.horizontal)}`, `anchorOrigin${(0, _capitalize.default)(anchorOrigin.vertical)}${(0, _capitalize.default)(anchorOrigin.horizontal)}${(0, _capitalize.default)(overlap)}`, `overlap${(0, _capitalize.default)(overlap)}`, color !== 'default' && `color${(0, _capitalize.default)(color)}`]
  };
  return (0, _composeClasses.default)(slots, _badgeClasses.getBadgeUtilityClass, classes);
};
const BadgeRoot = (0, _styled.default)('span', {
  name: 'MuiBadge',
  slot: 'Root',
  overridesResolver: (props, styles) => styles.root
})({
  position: 'relative',
  display: 'inline-flex',
  // For correct alignment with the text.
  verticalAlign: 'middle',
  flexShrink: 0
});
const BadgeBadge = (0, _styled.default)('span', {
  name: 'MuiBadge',
  slot: 'Badge',
  overridesResolver: (props, styles) => {
    const {
      ownerState
    } = props;
    return [styles.badge, styles[ownerState.variant], styles[`anchorOrigin${(0, _capitalize.default)(ownerState.anchorOrigin.vertical)}${(0, _capitalize.default)(ownerState.anchorOrigin.horizontal)}${(0, _capitalize.default)(ownerState.overlap)}`], ownerState.color !== 'default' && styles[`color${(0, _capitalize.default)(ownerState.color)}`], ownerState.invisible && styles.invisible];
  }
})(({
  theme,
  ownerState
}) => (0, _extends2.default)({
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
}, ownerState.anchorOrigin.vertical === 'top' && ownerState.anchorOrigin.horizontal === 'right' && ownerState.overlap === 'rectangular' && {
  top: 0,
  right: 0,
  transform: 'scale(1) translate(50%, -50%)',
  transformOrigin: '100% 0%',
  [`&.${_badgeClasses.default.invisible}`]: {
    transform: 'scale(0) translate(50%, -50%)'
  }
}, ownerState.anchorOrigin.vertical === 'bottom' && ownerState.anchorOrigin.horizontal === 'right' && ownerState.overlap === 'rectangular' && {
  bottom: 0,
  right: 0,
  transform: 'scale(1) translate(50%, 50%)',
  transformOrigin: '100% 100%',
  [`&.${_badgeClasses.default.invisible}`]: {
    transform: 'scale(0) translate(50%, 50%)'
  }
}, ownerState.anchorOrigin.vertical === 'top' && ownerState.anchorOrigin.horizontal === 'left' && ownerState.overlap === 'rectangular' && {
  top: 0,
  left: 0,
  transform: 'scale(1) translate(-50%, -50%)',
  transformOrigin: '0% 0%',
  [`&.${_badgeClasses.default.invisible}`]: {
    transform: 'scale(0) translate(-50%, -50%)'
  }
}, ownerState.anchorOrigin.vertical === 'bottom' && ownerState.anchorOrigin.horizontal === 'left' && ownerState.overlap === 'rectangular' && {
  bottom: 0,
  left: 0,
  transform: 'scale(1) translate(-50%, 50%)',
  transformOrigin: '0% 100%',
  [`&.${_badgeClasses.default.invisible}`]: {
    transform: 'scale(0) translate(-50%, 50%)'
  }
}, ownerState.anchorOrigin.vertical === 'top' && ownerState.anchorOrigin.horizontal === 'right' && ownerState.overlap === 'circular' && {
  top: '14%',
  right: '14%',
  transform: 'scale(1) translate(50%, -50%)',
  transformOrigin: '100% 0%',
  [`&.${_badgeClasses.default.invisible}`]: {
    transform: 'scale(0) translate(50%, -50%)'
  }
}, ownerState.anchorOrigin.vertical === 'bottom' && ownerState.anchorOrigin.horizontal === 'right' && ownerState.overlap === 'circular' && {
  bottom: '14%',
  right: '14%',
  transform: 'scale(1) translate(50%, 50%)',
  transformOrigin: '100% 100%',
  [`&.${_badgeClasses.default.invisible}`]: {
    transform: 'scale(0) translate(50%, 50%)'
  }
}, ownerState.anchorOrigin.vertical === 'top' && ownerState.anchorOrigin.horizontal === 'left' && ownerState.overlap === 'circular' && {
  top: '14%',
  left: '14%',
  transform: 'scale(1) translate(-50%, -50%)',
  transformOrigin: '0% 0%',
  [`&.${_badgeClasses.default.invisible}`]: {
    transform: 'scale(0) translate(-50%, -50%)'
  }
}, ownerState.anchorOrigin.vertical === 'bottom' && ownerState.anchorOrigin.horizontal === 'left' && ownerState.overlap === 'circular' && {
  bottom: '14%',
  left: '14%',
  transform: 'scale(1) translate(-50%, 50%)',
  transformOrigin: '0% 100%',
  [`&.${_badgeClasses.default.invisible}`]: {
    transform: 'scale(0) translate(-50%, 50%)'
  }
}, ownerState.invisible && {
  transition: theme.transitions.create('transform', {
    easing: theme.transitions.easing.easeInOut,
    duration: theme.transitions.duration.leavingScreen
  })
}));
const Badge = /*#__PURE__*/React.forwardRef(function Badge(inProps, ref) {
  var _ref, _slots$root, _ref2, _slots$badge, _slotProps$root, _slotProps$badge;
  const props = (0, _useThemeProps.default)({
    props: inProps,
    name: 'MuiBadge'
  });
  const {
      anchorOrigin: anchorOriginProp = {
        vertical: 'top',
        horizontal: 'right'
      },
      className,
      component = 'span',
      components = {},
      componentsProps = {},
      overlap: overlapProp = 'rectangular',
      color: colorProp = 'default',
      invisible: invisibleProp = false,
      max,
      badgeContent: badgeContentProp,
      slots,
      slotProps,
      showZero = false,
      variant: variantProp = 'standard'
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const prevProps = (0, _utils.usePreviousProps)({
    anchorOrigin: anchorOriginProp,
    color: colorProp,
    overlap: overlapProp,
    variant: variantProp
  });
  let invisible = invisibleProp;
  if (invisibleProp === false && (badgeContentProp === 0 && !showZero || badgeContentProp == null && variantProp !== 'dot')) {
    invisible = true;
  }
  const {
    color = colorProp,
    overlap = overlapProp,
    anchorOrigin = anchorOriginProp,
    variant = variantProp
  } = invisible ? prevProps : props;
  const ownerState = (0, _extends2.default)({}, props, {
    anchorOrigin,
    invisible,
    color,
    overlap,
    variant
  });
  const classes = useUtilityClasses(ownerState);
  let displayValue;
  if (variant !== 'dot') {
    displayValue = badgeContentProp && Number(badgeContentProp) > max ? `${max}+` : badgeContentProp;
  }

  // support both `slots` and `components` for backward compatibility
  const RootSlot = (_ref = (_slots$root = slots == null ? void 0 : slots.root) != null ? _slots$root : components.Root) != null ? _ref : BadgeRoot;
  const BadgeSlot = (_ref2 = (_slots$badge = slots == null ? void 0 : slots.badge) != null ? _slots$badge : components.Badge) != null ? _ref2 : BadgeBadge;
  const rootSlotProps = (_slotProps$root = slotProps == null ? void 0 : slotProps.root) != null ? _slotProps$root : componentsProps.root;
  const badgeSlotProps = (_slotProps$badge = slotProps == null ? void 0 : slotProps.badge) != null ? _slotProps$badge : componentsProps.badge;
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(_BadgeUnstyled.default, (0, _extends2.default)({
    invisible: invisibleProp,
    badgeContent: displayValue,
    showZero: showZero,
    max: max
  }, other, {
    slots: {
      root: RootSlot,
      badge: BadgeSlot
    },
    className: (0, _clsx.default)(rootSlotProps == null ? void 0 : rootSlotProps.className, classes.root, className),
    slotProps: {
      root: (0, _extends2.default)({}, rootSlotProps, (0, _shouldSpreadAdditionalProps.default)(RootSlot) && {
        as: component,
        ownerState: (0, _extends2.default)({}, rootSlotProps == null ? void 0 : rootSlotProps.ownerState, {
          anchorOrigin,
          color,
          overlap,
          variant
        })
      }),
      badge: (0, _extends2.default)({}, badgeSlotProps, {
        className: (0, _clsx.default)(classes.badge, badgeSlotProps == null ? void 0 : badgeSlotProps.className)
      }, (0, _shouldSpreadAdditionalProps.default)(BadgeSlot) && {
        ownerState: (0, _extends2.default)({}, badgeSlotProps == null ? void 0 : badgeSlotProps.ownerState, {
          anchorOrigin,
          color,
          overlap,
          variant
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
  anchorOrigin: _propTypes.default.shape({
    horizontal: _propTypes.default.oneOf(['left', 'right']).isRequired,
    vertical: _propTypes.default.oneOf(['bottom', 'top']).isRequired
  }),
  /**
   * The content rendered within the badge.
   */
  badgeContent: _propTypes.default.node,
  /**
   * The badge will be added relative to this node.
   */
  children: _propTypes.default.node,
  /**
   * Override or extend the styles applied to the component.
   */
  classes: _propTypes.default.object,
  /**
   * @ignore
   */
  className: _propTypes.default.string,
  /**
   * The color of the component.
   * It supports both default and custom theme colors, which can be added as shown in the
   * [palette customization guide](https://mui.com/material-ui/customization/palette/#adding-new-colors).
   * @default 'default'
   */
  color: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['default', 'primary', 'secondary', 'error', 'info', 'success', 'warning']), _propTypes.default.string]),
  /**
   * The component used for the root node.
   * Either a string to use a HTML element or a component.
   */
  component: _propTypes.default.elementType,
  /**
   * The components used for each slot inside.
   *
   * This prop is an alias for the `slots` prop.
   * It's recommended to use the `slots` prop instead.
   *
   * @default {}
   */
  components: _propTypes.default.shape({
    Badge: _propTypes.default.elementType,
    Root: _propTypes.default.elementType
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
  componentsProps: _propTypes.default.shape({
    badge: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    root: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object])
  }),
  /**
   * If `true`, the badge is invisible.
   * @default false
   */
  invisible: _propTypes.default.bool,
  /**
   * Max count to show.
   * @default 99
   */
  max: _propTypes.default.number,
  /**
   * Wrapped shape the badge should overlap.
   * @default 'rectangular'
   */
  overlap: _propTypes.default.oneOf(['circular', 'rectangular']),
  /**
   * Controls whether the badge is hidden when `badgeContent` is zero.
   * @default false
   */
  showZero: _propTypes.default.bool,
  /**
   * The props used for each slot inside the Badge.
   * @default {}
   */
  slotProps: _propTypes.default.shape({
    badge: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    root: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object])
  }),
  /**
   * The components used for each slot inside the Badge.
   * Either a string to use a HTML element or a component.
   * @default {}
   */
  slots: _propTypes.default.shape({
    badge: _propTypes.default.elementType,
    root: _propTypes.default.elementType
  }),
  /**
   * The system prop that allows defining system overrides as well as additional CSS styles.
   */
  sx: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object, _propTypes.default.bool])), _propTypes.default.func, _propTypes.default.object]),
  /**
   * The variant to use.
   * @default 'standard'
   */
  variant: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['dot', 'standard']), _propTypes.default.string])
} : void 0;
var _default = Badge;
exports.default = _default;