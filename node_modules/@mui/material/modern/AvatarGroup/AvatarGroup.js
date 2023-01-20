import _objectWithoutPropertiesLoose from "@babel/runtime/helpers/esm/objectWithoutPropertiesLoose";
import _extends from "@babel/runtime/helpers/esm/extends";
const _excluded = ["children", "className", "component", "componentsProps", "max", "slotProps", "spacing", "total", "variant"];
import * as React from 'react';
import PropTypes from 'prop-types';
import { isFragment } from 'react-is';
import clsx from 'clsx';
import { chainPropTypes } from '@mui/utils';
import { unstable_composeClasses as composeClasses } from '@mui/base';
import styled from '../styles/styled';
import useThemeProps from '../styles/useThemeProps';
import Avatar, { avatarClasses } from '../Avatar';
import avatarGroupClasses, { getAvatarGroupUtilityClass } from './avatarGroupClasses';
import { jsxs as _jsxs } from "react/jsx-runtime";
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
  return composeClasses(slots, getAvatarGroupUtilityClass, classes);
};
const AvatarGroupRoot = styled('div', {
  name: 'MuiAvatarGroup',
  slot: 'Root',
  overridesResolver: (props, styles) => _extends({
    [`& .${avatarGroupClasses.avatar}`]: styles.avatar
  }, styles.root)
})(({
  theme
}) => ({
  [`& .${avatarClasses.root}`]: {
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
const AvatarGroupAvatar = styled(Avatar, {
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
  const props = useThemeProps({
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
    other = _objectWithoutPropertiesLoose(props, _excluded);
  let clampedMax = max < 2 ? 2 : max;
  const ownerState = _extends({}, props, {
    max,
    spacing,
    component,
    variant
  });
  const classes = useUtilityClasses(ownerState);
  const children = React.Children.toArray(childrenProp).filter(child => {
    if (process.env.NODE_ENV !== 'production') {
      if (isFragment(child)) {
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
  const additionalAvatarSlotProps = slotProps.additionalAvatar ?? componentsProps.additionalAvatar;
  return /*#__PURE__*/_jsxs(AvatarGroupRoot, _extends({
    as: component,
    ownerState: ownerState,
    className: clsx(classes.root, className),
    ref: ref
  }, other, {
    children: [extraAvatars ? /*#__PURE__*/_jsxs(AvatarGroupAvatar, _extends({
      ownerState: ownerState,
      variant: variant
    }, additionalAvatarSlotProps, {
      className: clsx(classes.avatar, additionalAvatarSlotProps?.className),
      style: _extends({
        marginLeft
      }, additionalAvatarSlotProps?.style),
      children: ["+", extraAvatars]
    })) : null, children.slice(0, maxAvatars).reverse().map((child, index) => {
      return /*#__PURE__*/React.cloneElement(child, {
        className: clsx(child.props.className, classes.avatar),
        style: _extends({
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
   * The component used for the root node.
   * Either a string to use a HTML element or a component.
   */
  component: PropTypes.elementType,
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
    additionalAvatar: PropTypes.object
  }),
  /**
   * Max avatars to show before +x.
   * @default 5
   */
  max: chainPropTypes(PropTypes.number, props => {
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
  slotProps: PropTypes.shape({
    additionalAvatar: PropTypes.object
  }),
  /**
   * Spacing between avatars.
   * @default 'medium'
   */
  spacing: PropTypes.oneOfType([PropTypes.oneOf(['medium', 'small']), PropTypes.number]),
  /**
   * The system prop that allows defining system overrides as well as additional CSS styles.
   */
  sx: PropTypes.oneOfType([PropTypes.arrayOf(PropTypes.oneOfType([PropTypes.func, PropTypes.object, PropTypes.bool])), PropTypes.func, PropTypes.object]),
  /**
   * The total number of avatars. Used for calculating the number of extra avatars.
   * @default children.length
   */
  total: PropTypes.number,
  /**
   * The variant to use.
   * @default 'circular'
   */
  variant: PropTypes /* @typescript-to-proptypes-ignore */.oneOfType([PropTypes.oneOf(['circular', 'rounded', 'square']), PropTypes.string])
} : void 0;
export default AvatarGroup;