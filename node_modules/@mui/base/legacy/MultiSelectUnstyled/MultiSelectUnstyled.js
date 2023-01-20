import _extends from "@babel/runtime/helpers/esm/extends";
import _slicedToArray from "@babel/runtime/helpers/esm/slicedToArray";
import _objectWithoutProperties from "@babel/runtime/helpers/esm/objectWithoutProperties";
import * as React from 'react';
import PropTypes from 'prop-types';
import { unstable_useForkRef as useForkRef, unstable_useControlled as useControlled } from '@mui/utils';
import { flattenOptionGroups, getOptionsFromChildren } from '../SelectUnstyled/utils';
import useSelect from '../SelectUnstyled/useSelect';
import { useSlotProps } from '../utils';
import PopperUnstyled from '../PopperUnstyled';
import { SelectUnstyledContext } from '../SelectUnstyled/SelectUnstyledContext';
import composeClasses from '../composeClasses';
import { getSelectUnstyledUtilityClass } from '../SelectUnstyled/selectUnstyledClasses';
import defaultOptionStringifier from '../SelectUnstyled/defaultOptionStringifier';
import { jsx as _jsx } from "react/jsx-runtime";
import { jsxs as _jsxs } from "react/jsx-runtime";
function defaultRenderMultipleValues(selectedOptions) {
  return /*#__PURE__*/_jsx(React.Fragment, {
    children: selectedOptions.map(function (o) {
      return o.label;
    }).join(', ')
  });
}
function defaultFormValueProvider(selectedOptions) {
  if (selectedOptions.length === 0) {
    return '';
  }
  if (selectedOptions.every(function (o) {
    return typeof o.value === 'string' || typeof o.value === 'number' || typeof o.value === 'boolean';
  })) {
    return selectedOptions.map(function (o) {
      return String(o.value);
    });
  }
  return JSON.stringify(selectedOptions.map(function (o) {
    return o.value;
  }));
}
function useUtilityClasses(ownerState) {
  var active = ownerState.active,
    disabled = ownerState.disabled,
    open = ownerState.open,
    focusVisible = ownerState.focusVisible;
  var slots = {
    root: ['root', disabled && 'disabled', focusVisible && 'focusVisible', active && 'active', open && 'expanded'],
    listbox: ['listbox', disabled && 'disabled'],
    popper: ['popper']
  };
  return composeClasses(slots, getSelectUnstyledUtilityClass, {});
}

/**
 * The foundation for building custom-styled multi-selection select components.
 *
 * Demos:
 *
 * - [Unstyled Select](https://mui.com/base/react-select/)
 *
 * API:
 *
 * - [MultiSelectUnstyled API](https://mui.com/base/api/multi-select-unstyled/)
 */
var MultiSelectUnstyled = /*#__PURE__*/React.forwardRef(function MultiSelectUnstyled(props, forwardedRef) {
  var _ref, _slots$listbox, _slots$popper;
  var autoFocus = props.autoFocus,
    children = props.children,
    component = props.component,
    _props$defaultListbox = props.defaultListboxOpen,
    defaultListboxOpen = _props$defaultListbox === void 0 ? false : _props$defaultListbox,
    _props$defaultValue = props.defaultValue,
    defaultValue = _props$defaultValue === void 0 ? [] : _props$defaultValue,
    disabledProp = props.disabled,
    _props$getSerializedV = props.getSerializedValue,
    getSerializedValue = _props$getSerializedV === void 0 ? defaultFormValueProvider : _props$getSerializedV,
    listboxId = props.listboxId,
    listboxOpenProp = props.listboxOpen,
    name = props.name,
    onChange = props.onChange,
    onListboxOpenChange = props.onListboxOpenChange,
    _props$optionStringif = props.optionStringifier,
    optionStringifier = _props$optionStringif === void 0 ? defaultOptionStringifier : _props$optionStringif,
    renderValueProp = props.renderValue,
    _props$slotProps = props.slotProps,
    slotProps = _props$slotProps === void 0 ? {} : _props$slotProps,
    _props$slots = props.slots,
    slots = _props$slots === void 0 ? {} : _props$slots,
    valueProp = props.value,
    other = _objectWithoutProperties(props, ["autoFocus", "children", "component", "defaultListboxOpen", "defaultValue", "disabled", "getSerializedValue", "listboxId", "listboxOpen", "name", "onChange", "onListboxOpenChange", "optionStringifier", "renderValue", "slotProps", "slots", "value"]);
  var renderValue = renderValueProp != null ? renderValueProp : defaultRenderMultipleValues;
  var _React$useState = React.useState([]),
    groupedOptions = _React$useState[0],
    setGroupedOptions = _React$useState[1];
  var options = React.useMemo(function () {
    return flattenOptionGroups(groupedOptions);
  }, [groupedOptions]);
  var _useControlled = useControlled({
      controlled: listboxOpenProp,
      default: defaultListboxOpen,
      name: 'MultiSelectUnstyled',
      state: 'listboxOpen'
    }),
    _useControlled2 = _slicedToArray(_useControlled, 2),
    listboxOpen = _useControlled2[0],
    setListboxOpen = _useControlled2[1];
  React.useEffect(function () {
    setGroupedOptions(getOptionsFromChildren(children));
  }, [children]);
  var _React$useState2 = React.useState(false),
    buttonDefined = _React$useState2[0],
    setButtonDefined = _React$useState2[1];
  var buttonRef = React.useRef(null);
  var listboxRef = React.useRef(null);
  var Button = (_ref = component != null ? component : slots.root) != null ? _ref : 'button';
  var ListboxRoot = (_slots$listbox = slots.listbox) != null ? _slots$listbox : 'ul';
  var Popper = (_slots$popper = slots.popper) != null ? _slots$popper : PopperUnstyled;
  var handleButtonRefChange = React.useCallback(function (element) {
    setButtonDefined(element != null);
  }, []);
  var handleButtonRef = useForkRef(forwardedRef, buttonRef, handleButtonRefChange);
  React.useEffect(function () {
    if (autoFocus) {
      buttonRef.current.focus();
    }
  }, [autoFocus]);
  var handleOpenChange = function handleOpenChange(isOpen) {
    setListboxOpen(isOpen);
    onListboxOpenChange == null ? void 0 : onListboxOpenChange(isOpen);
  };
  var _useSelect = useSelect({
      buttonRef: handleButtonRef,
      defaultValue: defaultValue,
      disabled: disabledProp,
      listboxId: listboxId,
      multiple: true,
      onChange: onChange,
      onOpenChange: handleOpenChange,
      open: listboxOpen,
      options: options,
      optionStringifier: optionStringifier,
      value: valueProp
    }),
    buttonActive = _useSelect.buttonActive,
    buttonFocusVisible = _useSelect.buttonFocusVisible,
    disabled = _useSelect.disabled,
    getButtonProps = _useSelect.getButtonProps,
    getListboxProps = _useSelect.getListboxProps,
    getOptionProps = _useSelect.getOptionProps,
    getOptionState = _useSelect.getOptionState,
    value = _useSelect.value;
  var ownerState = _extends({}, props, {
    active: buttonActive,
    defaultListboxOpen: defaultListboxOpen,
    disabled: disabled,
    focusVisible: buttonFocusVisible,
    open: listboxOpen,
    renderValue: renderValue,
    value: value
  });
  var classes = useUtilityClasses(ownerState);
  var selectedOptions = React.useMemo(function () {
    if (value == null) {
      return [];
    }
    return options.filter(function (o) {
      return value.includes(o.value);
    });
  }, [options, value]);
  var buttonProps = useSlotProps({
    elementType: Button,
    getSlotProps: getButtonProps,
    externalSlotProps: slotProps.root,
    externalForwardedProps: other,
    ownerState: ownerState,
    className: classes.root
  });
  var listboxProps = useSlotProps({
    elementType: ListboxRoot,
    getSlotProps: getListboxProps,
    externalSlotProps: slotProps.listbox,
    additionalProps: {
      ref: listboxRef
    },
    ownerState: ownerState,
    className: classes.listbox
  });
  var popperProps = useSlotProps({
    elementType: Popper,
    externalSlotProps: slotProps.popper,
    additionalProps: {
      anchorEl: buttonRef.current,
      disablePortal: true,
      open: listboxOpen,
      placement: 'bottom-start',
      role: undefined
    },
    ownerState: ownerState,
    className: classes.popper
  });
  var context = React.useMemo(function () {
    return {
      getOptionProps: getOptionProps,
      getOptionState: getOptionState,
      listboxRef: listboxRef
    };
  }, [getOptionProps, getOptionState]);
  return /*#__PURE__*/_jsxs(React.Fragment, {
    children: [/*#__PURE__*/_jsx(Button, _extends({}, buttonProps, {
      children: renderValue(selectedOptions)
    })), buttonDefined && /*#__PURE__*/_jsx(Popper, _extends({}, popperProps, {
      children: /*#__PURE__*/_jsx(ListboxRoot, _extends({}, listboxProps, {
        children: /*#__PURE__*/_jsx(SelectUnstyledContext.Provider, {
          value: context,
          children: children
        })
      }))
    })), name && /*#__PURE__*/_jsx("input", {
      type: "hidden",
      name: name,
      value: getSerializedValue(selectedOptions)
    })]
  });
});
process.env.NODE_ENV !== "production" ? MultiSelectUnstyled.propTypes /* remove-proptypes */ = {
  // ----------------------------- Warning --------------------------------
  // | These PropTypes are generated from the TypeScript type definitions |
  // |     To update them edit TypeScript types and run "yarn proptypes"  |
  // ----------------------------------------------------------------------
  /**
   * If `true`, the select element is focused during the first mount
   * @default false
   */
  autoFocus: PropTypes.bool,
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
   * If `true`, the select will be initially open.
   * @default false
   */
  defaultListboxOpen: PropTypes.bool,
  /**
   * The default selected values. Use when the component is not controlled.
   * @default []
   */
  defaultValue: PropTypes.array,
  /**
   * If `true`, the select is disabled.
   * @default false
   */
  disabled: PropTypes.bool,
  /**
   * A function to convert the currently selected values to a type accepted by HTML input.
   * Used to set a value of a hidden input associated with the select,
   * so that the selected values can be posted with a form.
   */
  getSerializedValue: PropTypes.func,
  /**
   * `id` attribute of the listbox element.
   * Also used to derive the `id` attributes of options.
   */
  listboxId: PropTypes.string,
  /**
   * Controls the open state of the select's listbox.
   * @default undefined
   */
  listboxOpen: PropTypes.bool,
  /**
   * Name of the element. For example used by the server to identify the fields in form submits.
   * If the name is provided, the component will render a hidden input element that can be submitted to a server.
   */
  name: PropTypes.string,
  /**
   * Callback fired when an option is selected.
   */
  onChange: PropTypes.func,
  /**
   * Callback fired when the component requests to be opened.
   * Use in controlled mode (see listboxOpen).
   */
  onListboxOpenChange: PropTypes.func,
  /**
   * A function used to convert the option label to a string.
   * It's useful when labels are elements and need to be converted to plain text
   * to enable navigation using character keys on a keyboard.
   *
   * @default defaultOptionStringifier
   */
  optionStringifier: PropTypes.func,
  /**
   * Function that customizes the rendering of the selected values.
   */
  renderValue: PropTypes.func,
  /**
   * The props used for each slot inside the MultiSelect.
   * @default {}
   */
  slotProps: PropTypes.shape({
    listbox: PropTypes.oneOfType([PropTypes.func, PropTypes.object]),
    popper: PropTypes.oneOfType([PropTypes.func, PropTypes.object]),
    root: PropTypes.oneOfType([PropTypes.func, PropTypes.object])
  }),
  /**
   * The components used for each slot inside the MultiSelect.
   * Either a string to use a HTML element or a component.
   * @default {}
   */
  slots: PropTypes /* @typescript-to-proptypes-ignore */.shape({
    listbox: PropTypes.elementType,
    popper: PropTypes.elementType,
    root: PropTypes.elementType
  }),
  /**
   * The selected values.
   * Set to an empty array to deselect all options.
   */
  value: PropTypes.array
} : void 0;
export default MultiSelectUnstyled;