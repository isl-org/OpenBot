import _extends from "@babel/runtime/helpers/esm/extends";
import _objectWithoutProperties from "@babel/runtime/helpers/esm/objectWithoutProperties";
import * as React from 'react';
import PropTypes from 'prop-types';
import { unstable_useForkRef as useForkRef } from '@mui/utils';
import composeClasses from '../composeClasses';
import { SelectUnstyledContext } from '../SelectUnstyled/SelectUnstyledContext';
import { getOptionUnstyledUtilityClass } from './optionUnstyledClasses';
import { useSlotProps } from '../utils';
import { jsx as _jsx } from "react/jsx-runtime";
function useUtilityClasses(ownerState) {
  var disabled = ownerState.disabled,
    highlighted = ownerState.highlighted,
    selected = ownerState.selected;
  var slots = {
    root: ['root', disabled && 'disabled', highlighted && 'highlighted', selected && 'selected']
  };
  return composeClasses(slots, getOptionUnstyledUtilityClass, {});
}

/**
 * An unstyled option to be used within a SelectUnstyled.
 */
var OptionUnstyled = /*#__PURE__*/React.forwardRef(function OptionUnstyled(props, ref) {
  var children = props.children,
    component = props.component,
    disabled = props.disabled,
    label = props.label,
    _props$slotProps = props.slotProps,
    slotProps = _props$slotProps === void 0 ? {} : _props$slotProps,
    _props$slots = props.slots,
    slots = _props$slots === void 0 ? {} : _props$slots,
    value = props.value,
    other = _objectWithoutProperties(props, ["children", "component", "disabled", "label", "slotProps", "slots", "value"]);
  var selectContext = React.useContext(SelectUnstyledContext);
  if (!selectContext) {
    throw new Error('OptionUnstyled must be used within a SelectUnstyled');
  }
  var Root = component || slots.root || 'li';
  var selectOption = {
    value: value,
    label: label || children,
    disabled: disabled
  };
  var optionState = selectContext.getOptionState(selectOption);
  var optionProps = selectContext.getOptionProps(selectOption);
  var listboxRef = selectContext.listboxRef;
  var ownerState = _extends({}, props, optionState);
  var optionRef = React.useRef(null);
  var handleRef = useForkRef(ref, optionRef);
  React.useEffect(function () {
    // Scroll to the currently highlighted option
    if (optionState.highlighted) {
      if (!listboxRef.current || !optionRef.current) {
        return;
      }
      var listboxClientRect = listboxRef.current.getBoundingClientRect();
      var optionClientRect = optionRef.current.getBoundingClientRect();
      if (optionClientRect.top < listboxClientRect.top) {
        listboxRef.current.scrollTop -= listboxClientRect.top - optionClientRect.top;
      } else if (optionClientRect.bottom > listboxClientRect.bottom) {
        listboxRef.current.scrollTop += optionClientRect.bottom - listboxClientRect.bottom;
      }
    }
  }, [optionState.highlighted, listboxRef]);
  var classes = useUtilityClasses(ownerState);
  var rootProps = useSlotProps({
    elementType: Root,
    externalSlotProps: slotProps.root,
    externalForwardedProps: other,
    additionalProps: _extends({}, optionProps, {
      ref: handleRef
    }),
    className: classes.root,
    ownerState: ownerState
  });
  return /*#__PURE__*/_jsx(Root, _extends({}, rootProps, {
    children: children
  }));
});
process.env.NODE_ENV !== "production" ? OptionUnstyled.propTypes /* remove-proptypes */ = {
  // ----------------------------- Warning --------------------------------
  // | These PropTypes are generated from the TypeScript type definitions |
  // |     To update them edit TypeScript types and run "yarn proptypes"  |
  // ----------------------------------------------------------------------
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
   * If `true`, the option will be disabled.
   * @default false
   */
  disabled: PropTypes.bool,
  /**
   * A text representation of the option's content.
   * Used for keyboard text navigation matching.
   */
  label: PropTypes.string,
  /**
   * The props used for each slot inside the OptionUnstyled.
   * @default {}
   */
  slotProps: PropTypes.shape({
    root: PropTypes.oneOfType([PropTypes.func, PropTypes.object])
  }),
  /**
   * The components used for each slot inside the OptionUnstyled.
   * Either a string to use a HTML element or a component.
   * @default {}
   */
  slots: PropTypes.shape({
    root: PropTypes.elementType
  }),
  /**
   * The value of the option.
   */
  value: PropTypes.any.isRequired
} : void 0;

/**
 * An unstyled option to be used within a SelectUnstyled.
 *
 * Demos:
 *
 * - [Unstyled Select](https://mui.com/base/react-select/)
 *
 * API:
 *
 * - [OptionUnstyled API](https://mui.com/base/api/option-unstyled/)
 */
export default /*#__PURE__*/React.memo(OptionUnstyled);