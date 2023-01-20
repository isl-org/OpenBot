"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var _objectWithoutPropertiesLoose2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutPropertiesLoose"));
var React = _interopRequireWildcard(require("react"));
var _propTypes = _interopRequireDefault(require("prop-types"));
var _utils = require("@mui/utils");
var _utils2 = require("../SelectUnstyled/utils");
var _useSelect = _interopRequireDefault(require("../SelectUnstyled/useSelect"));
var _utils3 = require("../utils");
var _PopperUnstyled = _interopRequireDefault(require("../PopperUnstyled"));
var _SelectUnstyledContext = require("../SelectUnstyled/SelectUnstyledContext");
var _composeClasses = _interopRequireDefault(require("../composeClasses"));
var _selectUnstyledClasses = require("../SelectUnstyled/selectUnstyledClasses");
var _defaultOptionStringifier = _interopRequireDefault(require("../SelectUnstyled/defaultOptionStringifier"));
var _jsxRuntime = require("react/jsx-runtime");
const _excluded = ["autoFocus", "children", "component", "defaultListboxOpen", "defaultValue", "disabled", "getSerializedValue", "listboxId", "listboxOpen", "name", "onChange", "onListboxOpenChange", "optionStringifier", "renderValue", "slotProps", "slots", "value"];
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }
function defaultRenderMultipleValues(selectedOptions) {
  return /*#__PURE__*/(0, _jsxRuntime.jsx)(React.Fragment, {
    children: selectedOptions.map(o => o.label).join(', ')
  });
}
function defaultFormValueProvider(selectedOptions) {
  if (selectedOptions.length === 0) {
    return '';
  }
  if (selectedOptions.every(o => typeof o.value === 'string' || typeof o.value === 'number' || typeof o.value === 'boolean')) {
    return selectedOptions.map(o => String(o.value));
  }
  return JSON.stringify(selectedOptions.map(o => o.value));
}
function useUtilityClasses(ownerState) {
  const {
    active,
    disabled,
    open,
    focusVisible
  } = ownerState;
  const slots = {
    root: ['root', disabled && 'disabled', focusVisible && 'focusVisible', active && 'active', open && 'expanded'],
    listbox: ['listbox', disabled && 'disabled'],
    popper: ['popper']
  };
  return (0, _composeClasses.default)(slots, _selectUnstyledClasses.getSelectUnstyledUtilityClass, {});
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
const MultiSelectUnstyled = /*#__PURE__*/React.forwardRef(function MultiSelectUnstyled(props, forwardedRef) {
  var _ref, _slots$listbox, _slots$popper;
  const {
      autoFocus,
      children,
      component,
      defaultListboxOpen = false,
      defaultValue = [],
      disabled: disabledProp,
      getSerializedValue = defaultFormValueProvider,
      listboxId,
      listboxOpen: listboxOpenProp,
      name,
      onChange,
      onListboxOpenChange,
      optionStringifier = _defaultOptionStringifier.default,
      renderValue: renderValueProp,
      slotProps = {},
      slots = {},
      value: valueProp
    } = props,
    other = (0, _objectWithoutPropertiesLoose2.default)(props, _excluded);
  const renderValue = renderValueProp != null ? renderValueProp : defaultRenderMultipleValues;
  const [groupedOptions, setGroupedOptions] = React.useState([]);
  const options = React.useMemo(() => (0, _utils2.flattenOptionGroups)(groupedOptions), [groupedOptions]);
  const [listboxOpen, setListboxOpen] = (0, _utils.unstable_useControlled)({
    controlled: listboxOpenProp,
    default: defaultListboxOpen,
    name: 'MultiSelectUnstyled',
    state: 'listboxOpen'
  });
  React.useEffect(() => {
    setGroupedOptions((0, _utils2.getOptionsFromChildren)(children));
  }, [children]);
  const [buttonDefined, setButtonDefined] = React.useState(false);
  const buttonRef = React.useRef(null);
  const listboxRef = React.useRef(null);
  const Button = (_ref = component != null ? component : slots.root) != null ? _ref : 'button';
  const ListboxRoot = (_slots$listbox = slots.listbox) != null ? _slots$listbox : 'ul';
  const Popper = (_slots$popper = slots.popper) != null ? _slots$popper : _PopperUnstyled.default;
  const handleButtonRefChange = React.useCallback(element => {
    setButtonDefined(element != null);
  }, []);
  const handleButtonRef = (0, _utils.unstable_useForkRef)(forwardedRef, buttonRef, handleButtonRefChange);
  React.useEffect(() => {
    if (autoFocus) {
      buttonRef.current.focus();
    }
  }, [autoFocus]);
  const handleOpenChange = isOpen => {
    setListboxOpen(isOpen);
    onListboxOpenChange == null ? void 0 : onListboxOpenChange(isOpen);
  };
  const {
    buttonActive,
    buttonFocusVisible,
    disabled,
    getButtonProps,
    getListboxProps,
    getOptionProps,
    getOptionState,
    value
  } = (0, _useSelect.default)({
    buttonRef: handleButtonRef,
    defaultValue,
    disabled: disabledProp,
    listboxId,
    multiple: true,
    onChange,
    onOpenChange: handleOpenChange,
    open: listboxOpen,
    options,
    optionStringifier,
    value: valueProp
  });
  const ownerState = (0, _extends2.default)({}, props, {
    active: buttonActive,
    defaultListboxOpen,
    disabled,
    focusVisible: buttonFocusVisible,
    open: listboxOpen,
    renderValue,
    value
  });
  const classes = useUtilityClasses(ownerState);
  const selectedOptions = React.useMemo(() => {
    if (value == null) {
      return [];
    }
    return options.filter(o => value.includes(o.value));
  }, [options, value]);
  const buttonProps = (0, _utils3.useSlotProps)({
    elementType: Button,
    getSlotProps: getButtonProps,
    externalSlotProps: slotProps.root,
    externalForwardedProps: other,
    ownerState,
    className: classes.root
  });
  const listboxProps = (0, _utils3.useSlotProps)({
    elementType: ListboxRoot,
    getSlotProps: getListboxProps,
    externalSlotProps: slotProps.listbox,
    additionalProps: {
      ref: listboxRef
    },
    ownerState,
    className: classes.listbox
  });
  const popperProps = (0, _utils3.useSlotProps)({
    elementType: Popper,
    externalSlotProps: slotProps.popper,
    additionalProps: {
      anchorEl: buttonRef.current,
      disablePortal: true,
      open: listboxOpen,
      placement: 'bottom-start',
      role: undefined
    },
    ownerState,
    className: classes.popper
  });
  const context = React.useMemo(() => ({
    getOptionProps,
    getOptionState,
    listboxRef
  }), [getOptionProps, getOptionState]);
  return /*#__PURE__*/(0, _jsxRuntime.jsxs)(React.Fragment, {
    children: [/*#__PURE__*/(0, _jsxRuntime.jsx)(Button, (0, _extends2.default)({}, buttonProps, {
      children: renderValue(selectedOptions)
    })), buttonDefined && /*#__PURE__*/(0, _jsxRuntime.jsx)(Popper, (0, _extends2.default)({}, popperProps, {
      children: /*#__PURE__*/(0, _jsxRuntime.jsx)(ListboxRoot, (0, _extends2.default)({}, listboxProps, {
        children: /*#__PURE__*/(0, _jsxRuntime.jsx)(_SelectUnstyledContext.SelectUnstyledContext.Provider, {
          value: context,
          children: children
        })
      }))
    })), name && /*#__PURE__*/(0, _jsxRuntime.jsx)("input", {
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
  autoFocus: _propTypes.default.bool,
  /**
   * @ignore
   */
  children: _propTypes.default.node,
  /**
   * The component used for the root node.
   * Either a string to use a HTML element or a component.
   */
  component: _propTypes.default.elementType,
  /**
   * If `true`, the select will be initially open.
   * @default false
   */
  defaultListboxOpen: _propTypes.default.bool,
  /**
   * The default selected values. Use when the component is not controlled.
   * @default []
   */
  defaultValue: _propTypes.default.array,
  /**
   * If `true`, the select is disabled.
   * @default false
   */
  disabled: _propTypes.default.bool,
  /**
   * A function to convert the currently selected values to a type accepted by HTML input.
   * Used to set a value of a hidden input associated with the select,
   * so that the selected values can be posted with a form.
   */
  getSerializedValue: _propTypes.default.func,
  /**
   * `id` attribute of the listbox element.
   * Also used to derive the `id` attributes of options.
   */
  listboxId: _propTypes.default.string,
  /**
   * Controls the open state of the select's listbox.
   * @default undefined
   */
  listboxOpen: _propTypes.default.bool,
  /**
   * Name of the element. For example used by the server to identify the fields in form submits.
   * If the name is provided, the component will render a hidden input element that can be submitted to a server.
   */
  name: _propTypes.default.string,
  /**
   * Callback fired when an option is selected.
   */
  onChange: _propTypes.default.func,
  /**
   * Callback fired when the component requests to be opened.
   * Use in controlled mode (see listboxOpen).
   */
  onListboxOpenChange: _propTypes.default.func,
  /**
   * A function used to convert the option label to a string.
   * It's useful when labels are elements and need to be converted to plain text
   * to enable navigation using character keys on a keyboard.
   *
   * @default defaultOptionStringifier
   */
  optionStringifier: _propTypes.default.func,
  /**
   * Function that customizes the rendering of the selected values.
   */
  renderValue: _propTypes.default.func,
  /**
   * The props used for each slot inside the MultiSelect.
   * @default {}
   */
  slotProps: _propTypes.default.shape({
    listbox: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    popper: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object]),
    root: _propTypes.default.oneOfType([_propTypes.default.func, _propTypes.default.object])
  }),
  /**
   * The components used for each slot inside the MultiSelect.
   * Either a string to use a HTML element or a component.
   * @default {}
   */
  slots: _propTypes.default /* @typescript-to-proptypes-ignore */.shape({
    listbox: _propTypes.default.elementType,
    popper: _propTypes.default.elementType,
    root: _propTypes.default.elementType
  }),
  /**
   * The selected values.
   * Set to an empty array to deselect all options.
   */
  value: _propTypes.default.array
} : void 0;
var _default = MultiSelectUnstyled;
exports.default = _default;