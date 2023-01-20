import _extends from "@babel/runtime/helpers/esm/extends";
import _objectWithoutProperties from "@babel/runtime/helpers/esm/objectWithoutProperties";
import * as React from 'react';
import PropTypes from 'prop-types';
import { unstable_useForkRef as useForkRef } from '@mui/utils';
import composeClasses from '../composeClasses';
import { getTabUnstyledUtilityClass } from './tabUnstyledClasses';
import useTab from './useTab';
import { useSlotProps } from '../utils';
import { jsx as _jsx } from "react/jsx-runtime";
var useUtilityClasses = function useUtilityClasses(ownerState) {
  var selected = ownerState.selected,
    disabled = ownerState.disabled;
  var slots = {
    root: ['root', selected && 'selected', disabled && 'disabled']
  };
  return composeClasses(slots, getTabUnstyledUtilityClass, {});
};
/**
 *
 * Demos:
 *
 * - [Unstyled Tabs](https://mui.com/base/react-tabs/)
 *
 * API:
 *
 * - [TabUnstyled API](https://mui.com/base/api/tab-unstyled/)
 */
var TabUnstyled = /*#__PURE__*/React.forwardRef(function TabUnstyled(props, ref) {
  var _ref;
  var action = props.action,
    children = props.children,
    valueProp = props.value,
    _props$disabled = props.disabled,
    disabled = _props$disabled === void 0 ? false : _props$disabled,
    onChange = props.onChange,
    onClick = props.onClick,
    onFocus = props.onFocus,
    component = props.component,
    _props$slotProps = props.slotProps,
    slotProps = _props$slotProps === void 0 ? {} : _props$slotProps,
    _props$slots = props.slots,
    slots = _props$slots === void 0 ? {} : _props$slots,
    other = _objectWithoutProperties(props, ["action", "children", "value", "disabled", "onChange", "onClick", "onFocus", "component", "slotProps", "slots"]);
  var tabRef = React.useRef();
  var handleRef = useForkRef(tabRef, ref);
  var _useTab = useTab(_extends({}, props, {
      ref: handleRef
    })),
    active = _useTab.active,
    focusVisible = _useTab.focusVisible,
    setFocusVisible = _useTab.setFocusVisible,
    selected = _useTab.selected,
    getRootProps = _useTab.getRootProps;
  React.useImperativeHandle(action, function () {
    return {
      focusVisible: function focusVisible() {
        setFocusVisible(true);
        tabRef.current.focus();
      }
    };
  }, [setFocusVisible]);
  var ownerState = _extends({}, props, {
    active: active,
    focusVisible: focusVisible,
    disabled: disabled,
    selected: selected
  });
  var classes = useUtilityClasses(ownerState);
  var TabRoot = (_ref = component != null ? component : slots.root) != null ? _ref : 'button';
  var tabRootProps = useSlotProps({
    elementType: TabRoot,
    getSlotProps: getRootProps,
    externalSlotProps: slotProps.root,
    externalForwardedProps: other,
    additionalProps: {
      ref: ref
    },
    ownerState: ownerState,
    className: classes.root
  });
  return /*#__PURE__*/_jsx(TabRoot, _extends({}, tabRootProps, {
    children: children
  }));
});
process.env.NODE_ENV !== "production" ? TabUnstyled.propTypes /* remove-proptypes */ = {
  // ----------------------------- Warning --------------------------------
  // | These PropTypes are generated from the TypeScript type definitions |
  // |     To update them edit TypeScript types and run "yarn proptypes"  |
  // ----------------------------------------------------------------------
  /**
   * A ref for imperative actions. It currently only supports `focusVisible()` action.
   */
  action: PropTypes.oneOfType([PropTypes.func, PropTypes.shape({
    current: PropTypes.shape({
      focusVisible: PropTypes.func.isRequired
    })
  })]),
  /**
   * @ignore
   */
  children: PropTypes.node,
  /**
   * The component used for the root node.
   * Either a string to use a HTML element or a component.
   */
  component: PropTypes.elementType,
  /**
   * If `true`, the component is disabled.
   * @default false
   */
  disabled: PropTypes.bool,
  /**
   * Callback invoked when new value is being set.
   */
  onChange: PropTypes.func,
  /**
   * @ignore
   */
  onClick: PropTypes.func,
  /**
   * @ignore
   */
  onFocus: PropTypes.func,
  /**
   * The props used for each slot inside the Tab.
   * @default {}
   */
  slotProps: PropTypes.shape({
    root: PropTypes.oneOfType([PropTypes.func, PropTypes.object])
  }),
  /**
   * The components used for each slot inside the Tab.
   * Either a string to use a HTML element or a component.
   * @default {}
   */
  slots: PropTypes.shape({
    root: PropTypes.elementType
  }),
  /**
   * You can provide your own value. Otherwise, we fall back to the child position index.
   */
  value: PropTypes.oneOfType([PropTypes.number, PropTypes.string])
} : void 0;
export default TabUnstyled;