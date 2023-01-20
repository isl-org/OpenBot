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
var _reactIs = require("react-is");
var _clsx = _interopRequireDefault(require("clsx"));
var _utils = require("@mui/utils");
var _base = require("@mui/base");
var _styled = _interopRequireDefault(require("../styles/styled"));
var _useThemeProps = _interopRequireDefault(require("../styles/useThemeProps"));
var _Avatar = _interopRequireWildcard(require("../Avatar"));
var _avatarGroupClasses = _interopRequireWildcard(require("./avatarGroupClasses"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["children", "className", "component", "componentsProps", "max", "slotProps", "spacing", "total", "variant"];
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
const SPACINGS = {
  small: -16,
  medium: null
};
const useUtilityClasses = ownerState => {
  const {
    classes
  } = ownerState;
  const slots = {
    root: ['root'],
    avatar: ['avatar']
  };
  return (0, _base.unstable_composeClasses)(slots, _avatarGroupClasses.getAvatarGroupUtilityClass, classes);
};
const AvatarGroupRoot = (0, _styled.default)('div', {
  name: 'MuiAvatarGroup',
  slot: 'Root',
  overridesResolver: (props, styles) => (0, _extends2.default)({
    [`& .${_avatarGroupClasses.default.avatar}`]: styles.avatar
  }, styles.root)
})(({
  theme
}) => ({
  [`& .${_Avatar.avatarClasses.root}`]: {
    border: `2px solid ${(theme.vars || theme).palette.background.default}`,
    boxSizing: 'content-box',
    marginLeft: -8,
    '&:last-child': {
      marginLeft: 0
    }
  },
  display: 'flex',
  flexDirection: 'row-reverse'
}));
const AvatarGroupAvatar = (0, _styled.default)(_Avatar.default, {
  name: 'MuiAvatarGroup',
  slot: 'Avatar',
  overridesResolver: (props, styles) => styles.avatar
})(({
  theme
}) => ({
  border: `2px solid ${(theme.vars || theme).palette.background.default}`,
  boxSizing: 'content-box',
  marginLeft: -8,
  '&:last-child': {
    marginLeft: 0
  }
}));
const AvatarGroup = /*#__PURE__*/React.forwardRef(function AvatarGroup(inProps, ref) {
  var _slotProps$additional;
  const props = (0, _useThemeProps.default)({
    props: inProps,
    name: 'MuiAvatarGroup'
  });
  const {
      children: childrenProp,
      className,
      component = 'div',
      componentsProps = {},
      max = 5,
      slotProps = {},
      spacing = 'medium',
      total,
      variant = 'circular'
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  let clampedMax = max < 2 ? 2 : max;
  const ownerState = (0, _extends2.default)({}, props, {
    max,
    spacing,
    component,
    variant
  });
  const classes = useUtilityClasses(ownerState);
  const children = React.Children.toArray(childrenProp).filter(child => {
    if (process.env.NODE_ENV !== 'production') {
      if ((0, _reactIs.isFragment)(child)) {
        console.error(["MUI: The AvatarGroup component doesn't accept a Fragment as a child.", 'Consider providing an array instead.'].join('\n'));
      }
    }
    return /*#__PURE__*/React.isValidElement(child);
  });
  const totalAvatars = total || children.length;
  if (totalAvatars === clampedMax) {
    clampedMax += 1;
  }
  clampedMax = Math.min(totalAvatars + 1, clampedMax);
  const maxAvatars = Math.min(children.length, clampedMax - 1);
  const extraAvatars = Math.max(totalAvatars - clampedMax, totalAvatars - maxAvatars, 0);
  const marginLeft = spacing && SPACINGS[spacing] !== undefined ? SPACINGS[spacing] : -spacing;
  const additionalAvatarSlotProps = (_slotProps$additional = slotProps.additionalAvatar) != null ? _slotProps$additional : componentsProps.additionalAvatar;
  return /*#__PURE__*/(0, _jsxRuntime.jsxs)(AvatarGroupRoot, (0, _extends2.default)({
    as: component,
    ownerState: ownerState,
    className: (0, _clsx.default)(classes.root, className),
    ref: ref
  }, other, {
    children: [extraAvatars ? /*#__PURE__*/(0, _jsxRuntime.jsxs)(AvatarGroupAvatar, (0, _extends2.default)({
      ownerState: ownerState,
      variant: variant
    }, additionalAvatarSlotProps, {
      className: (0, _clsx.default)(classes.avatar, additionalAvatarSlotProps == null ? void 0 : additionalAvatarSlotProps.className),
      style: (0, _extends2.default)({
        marginLeft
      }, additionalAvatarSlotProps == null ? void 0 : additionalAvatarSlotProps.style),
      children: ["+", extraAvatars]
    })) : null, children.slice(0, maxAvatars).reverse().map((child, index) => {
      return /*#__PURE__*/React.cloneElement(child, {
        className: (0, _clsx.default)(child.props.className, classes.avatar),
        style: (0, _extends2.default)({
          // Consistent with "&:last-child" styling for the default spacing,
          // we do not apply custom marginLeft spacing on the last child
          marginLeft: index === maxAvatars - 1 ? undefined : marginLeft
        }, child.props.style),
        variant: child.props.variant || variant
      });
    })]
  }));
});
process.env.NODE_ENV !== "production" ? AvatarGroup.propTypes /* remove-proptypes */ = {
  // ----------------------------- Warning --------------------------------
  // | These PropTypes are generated from the TypeScript type definitions |
  // |     To update them edit the d.ts file and run "yarn proptypes"     |
  // ----------------------------------------------------------------------
  /**
   * The avatars to stack.
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
   * The component used for the root node.
   * Either a string to use a HTML element or a component.
   */
  component: _propTypes.default.elementType,
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
    additionalAvatar: _propTypes.default.object
  }),
  /**
   * Max avatars to show before +x.
   * @default 5
   */
  max: (0, _utils.chainPropTypes)(_propTypes.default.number, props => {
    if (props.max < 2) {
      return new Error(['MUI: The prop `max` should be equal to 2 or above.', 'A value below is clamped to 2.'].join('\n'));
    }
    return null;
  }),
  /**
   * The extra props for the slot components.
   * You can override the existing props or add new ones.
   *
   * This prop is an alias for the `componentsProps` prop, which will be deprecated in the future.
   *
   * @default {}
   */
  slotProps: _propTypes.default.shape({
    additionalAvatar: _propTypes.default.object
  }),
  /**
   * Spacing between avatars.
   * @default 'medium'
   */
  spacing: _propTypes.default.oneOfType([_propTypes.default.oneOf(['medium', 'small']), _propTypes.default.number]),
  /**
   * The system prop that allows defining system overrides as well as additional CSS styles.
   */
  sx: _propTypes.default.oneOfType([_propTypes.default.arrayOf(_propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object, _propTypes.default.bool])), _propTypes.default.func, _propTypes.default.object]),
  /**
   * The total number of avatars. Used for calculating the number of extra avatars.
   * @default children.length
   */
  total: _propTypes.default.number,
  /**
   * The variant to use.
   * @default 'circular'
   */
  variant: _propTypes.default /* @typescript-to-proptypes-ignore */.oneOfType([_propTypes.default.oneOf(['circular', 'rounded', 'square']), _propTypes.default.string])
} : void 0;
var _default = AvatarGroup;
exports.default = _default;