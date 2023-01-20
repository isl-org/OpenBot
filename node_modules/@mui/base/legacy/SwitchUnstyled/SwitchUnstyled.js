import _extends from "@babel/runtime/helpers/esm/extends";
import _objectWithoutProperties from "@babel/runtime/helpers/esm/objectWithoutProperties";
import * as React from 'react';
import PropTypes from 'prop-types';
import composeClasses from '../composeClasses';
import useSwitch from './useSwitch';
import { getSwitchUnstyledUtilityClass } from './switchUnstyledClasses';
import { useSlotProps } from '../utils';
import { jsx as _jsx } from "react/jsx-runtime";
import { jsxs as _jsxs } from "react/jsx-runtime";
var useUtilityClasses = function useUtilityClasses(ownerState) {
  var checked = ownerState.checked,
    disabled = ownerState.disabled,
    focusVisible = ownerState.focusVisible,
    readOnly = ownerState.readOnly;
  var slots = {
    root: ['root', checked && 'checked', disabled && 'disabled', focusVisible && 'focusVisible', readOnly && 'readOnly'],
    thumb: ['thumb'],
    input: ['input'],
    track: ['track']
  };
  return composeClasses(slots, getSwitchUnstyledUtilityClass, {});
};

/**
 * The foundation for building custom-styled switches.
 *
 * Demos:
 *
 * - [Unstyled Switch](https://mui.com/base/react-switch/)
 *
 * API:
 *
 * - [SwitchUnstyled API](https://mui.com/base/api/switch-unstyled/)
 */
var SwitchUnstyled = /*#__PURE__*/React.forwardRef(function SwitchUnstyled(props, ref) {
  var _ref, _slots$thumb, _slots$input, _slots$track;
  var checkedProp = props.checked,
    component = props.component,
    defaultChecked = props.defaultChecked,
    disabledProp = props.disabled,
    onBlur = props.onBlur,
    onChange = props.onChange,
    onFocus = props.onFocus,
    onFocusVisible = props.onFocusVisible,
    readOnlyProp = props.readOnly,
    required = props.required,
    _props$slotProps = props.slotProps,
    slotProps = _props$slotProps === void 0 ? {} : _props$slotProps,
    _props$slots = props.slots,
    slots = _props$slots === void 0 ? {} : _props$slots,
    other = _objectWithoutProperties(props, ["checked", "component", "defaultChecked", "disabled", "onBlur", "onChange", "onFocus", "onFocusVisible", "readOnly", "required", "slotProps", "slots"]);
  var useSwitchProps = {
    checked: checkedProp,
    defaultChecked: defaultChecked,
    disabled: disabledProp,
    onBlur: onBlur,
    onChange: onChange,
    onFocus: onFocus,
    onFocusVisible: onFocusVisible,
    readOnly: readOnlyProp
  };
  var _useSwitch = useSwitch(useSwitchProps),
    getInputProps = _useSwitch.getInputProps,
    checked = _useSwitch.checked,
    disabled = _useSwitch.disabled,
    focusVisible = _useSwitch.focusVisible,
    readOnly = _useSwitch.readOnly;
  var ownerState = _extends({}, props, {
    checked: checked,
    disabled: disabled,
    focusVisible: focusVisible,
    readOnly: readOnly
  });
  var classes = useUtilityClasses(ownerState);
  var Root = (_ref = component != null ? component : slots.root) != null ? _ref : 'span';
  var rootProps = useSlotProps({
    elementType: Root,
    externalSlotProps: slotProps.root,
    externalForwardedProps: other,
    additionalProps: {
      ref: ref
    },
    ownerState: ownerState,
    className: classes.root
  });
  var Thumb = (_slots$thumb = slots.thumb) != null ? _slots$thumb : 'span';
  var thumbProps = useSlotProps({
    elementType: Thumb,
    externalSlotProps: slotProps.thumb,
    ownerState: ownerState,
    className: classes.thumb
  });
  var Input = (_slots$input = slots.input) != null ? _slots$input : 'input';
  var inputProps = useSlotProps({
    elementType: Input,
    getSlotProps: getInputProps,
    externalSlotProps: slotProps.input,
    ownerState: ownerState,
    className: classes.input
  });
  var Track = slots.track === null ? function () {
    return null;
  } : (_slots$track = slots.track) != null ? _slots$track : 'span';
  var trackProps = useSlotProps({
    elementType: Track,
    externalSlotProps: slotProps.track,
    ownerState: ownerState,
    className: classes.track
  });
  return /*#__PURE__*/_jsxs(Root, _extends({}, rootProps, {
    children: [/*#__PURE__*/_jsx(Track, _extends({}, trackProps)), /*#__PURE__*/_jsx(Thumb, _extends({}, thumbProps)), /*#__PURE__*/_jsx(Input, _extends({}, inputProps))]
  }));
});
process.env.NODE_ENV !== "production" ? SwitchUnstyled.propTypes /* remove-proptypes */ = {
  // ----------------------------- Warning --------------------------------
  // | These PropTypes are generated from the TypeScript type definitions |
  // |     To update them edit TypeScript types and run "yarn proptypes"  |
  // ----------------------------------------------------------------------
  /**
   * If `true`, the component is checked.
   */
  checked: PropTypes.bool,
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
   * The default checked state. Use when the component is not controlled.
   */
  defaultChecked: PropTypes.bool,
  /**
   * If `true`, the component is disabled.
   */
  disabled: PropTypes.bool,
  /**
   * @ignore
   */
  onBlur: PropTypes.func,
  /**
   * Callback fired when the state is changed.
   *
   * @param {React.ChangeEvent<HTMLInputElement>} event The event source of the callback.
   * You can pull out the new value by accessing `event.target.value` (string).
   * You can pull out the new checked state by accessing `event.target.checked` (boolean).
   */
  onChange: PropTypes.func,
  /**
   * @ignore
   */
  onFocus: PropTypes.func,
  /**
   * @ignore
   */
  onFocusVisible: PropTypes.func,
  /**
   * If `true`, the component is read only.
   */
  readOnly: PropTypes.bool,
  /**
   * If `true`, the `input` element is required.
   */
  required: PropTypes.bool,
  /**
   * The props used for each slot inside the Switch.
   * @default {}
   */
  slotProps: PropTypes.shape({
    input: PropTypes.oneOfType([PropTypes.func, PropTypes.object]),
    root: PropTypes.oneOfType([PropTypes.func, PropTypes.object]),
    thumb: PropTypes.oneOfType([PropTypes.func, PropTypes.object]),
    track: PropTypes.oneOfType([PropTypes.func, PropTypes.object])
  }),
  /**
   * The components used for each slot inside the Switch.
   * Either a string to use a HTML element or a component.
   * @default {}
   */
  slots: PropTypes /* @typescript-to-proptypes-ignore */.shape({
    input: PropTypes.elementType,
    root: PropTypes.elementType,
    thumb: PropTypes.elementType,
    track: PropTypes.oneOfType([PropTypes.elementType, PropTypes.oneOf([null])])
  })
} : void 0;
export default SwitchUnstyled;