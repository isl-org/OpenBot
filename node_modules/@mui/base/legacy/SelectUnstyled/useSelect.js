import _extends from "@babel/runtime/helpers/esm/extends";
import _slicedToArray from "@babel/runtime/helpers/esm/slicedToArray";
import * as React from 'react';
import { unstable_useControlled as useControlled, unstable_useForkRef as useForkRef, unstable_useId as useId } from '@mui/utils';
import { useButton } from '../ButtonUnstyled';
import { useListbox, defaultListboxReducer, ActionTypes } from '../ListboxUnstyled';
import defaultOptionStringifier from './defaultOptionStringifier';
function useSelect(props) {
  var buttonRefProp = props.buttonRef,
    defaultValue = props.defaultValue,
    _props$disabled = props.disabled,
    disabled = _props$disabled === void 0 ? false : _props$disabled,
    listboxIdProp = props.listboxId,
    listboxRefProp = props.listboxRef,
    _props$multiple = props.multiple,
    multiple = _props$multiple === void 0 ? false : _props$multiple,
    onChange = props.onChange,
    onOpenChange = props.onOpenChange,
    _props$open = props.open,
    open = _props$open === void 0 ? false : _props$open,
    options = props.options,
    _props$optionStringif = props.optionStringifier,
    optionStringifier = _props$optionStringif === void 0 ? defaultOptionStringifier : _props$optionStringif,
    valueProp = props.value;
  var buttonRef = React.useRef(null);
  var handleButtonRef = useForkRef(buttonRefProp, buttonRef);
  var listboxRef = React.useRef(null);
  var listboxId = useId(listboxIdProp);
  var _useControlled = useControlled({
      controlled: valueProp,
      default: defaultValue,
      name: 'SelectUnstyled',
      state: 'value'
    }),
    _useControlled2 = _slicedToArray(_useControlled, 2),
    value = _useControlled2[0],
    setValue = _useControlled2[1]; // prevents closing the listbox on keyUp right after opening it
  var ignoreEnterKeyUp = React.useRef(false);

  // prevents reopening the listbox when button is clicked
  // (listbox closes on lost focus, then immediately reopens on click)
  var ignoreClick = React.useRef(false);

  // Ensure the listbox is focused after opening
  var _React$useState = React.useState(false),
    listboxFocusRequested = _React$useState[0],
    requestListboxFocus = _React$useState[1];
  var focusListboxIfRequested = React.useCallback(function () {
    if (listboxFocusRequested && listboxRef.current != null) {
      listboxRef.current.focus();
      requestListboxFocus(false);
    }
  }, [listboxFocusRequested]);
  var handleListboxRef = useForkRef(listboxRefProp, listboxRef, focusListboxIfRequested);
  React.useEffect(function () {
    focusListboxIfRequested();
  }, [focusListboxIfRequested]);
  React.useEffect(function () {
    requestListboxFocus(open);
  }, [open]);
  var createHandleMouseDown = function createHandleMouseDown(otherHandlers) {
    return function (event) {
      var _otherHandlers$onMous;
      otherHandlers == null ? void 0 : (_otherHandlers$onMous = otherHandlers.onMouseDown) == null ? void 0 : _otherHandlers$onMous.call(otherHandlers, event);
      if (!event.defaultPrevented && open) {
        ignoreClick.current = true;
      }
    };
  };
  var createHandleButtonClick = function createHandleButtonClick(otherHandlers) {
    return function (event) {
      var _otherHandlers$onClic;
      otherHandlers == null ? void 0 : (_otherHandlers$onClic = otherHandlers.onClick) == null ? void 0 : _otherHandlers$onClic.call(otherHandlers, event);
      if (!event.defaultPrevented && !ignoreClick.current) {
        onOpenChange == null ? void 0 : onOpenChange(!open);
      }
      ignoreClick.current = false;
    };
  };
  var createHandleButtonKeyDown = function createHandleButtonKeyDown(otherHandlers) {
    return function (event) {
      var _otherHandlers$onKeyD;
      otherHandlers == null ? void 0 : (_otherHandlers$onKeyD = otherHandlers.onKeyDown) == null ? void 0 : _otherHandlers$onKeyD.call(otherHandlers, event);
      if (event.defaultPrevented) {
        return;
      }
      if (event.key === 'Enter') {
        ignoreEnterKeyUp.current = true;
      }
      if (event.key === 'ArrowDown' || event.key === 'ArrowUp') {
        event.preventDefault();
        onOpenChange == null ? void 0 : onOpenChange(true);
      }
    };
  };
  var createHandleListboxKeyUp = function createHandleListboxKeyUp(otherHandlers) {
    return function (event) {
      var _otherHandlers$onKeyU;
      otherHandlers == null ? void 0 : (_otherHandlers$onKeyU = otherHandlers.onKeyUp) == null ? void 0 : _otherHandlers$onKeyU.call(otherHandlers, event);
      if (event.defaultPrevented) {
        return;
      }
      var closingKeys = multiple ? ['Escape'] : ['Escape', 'Enter', ' '];
      if (open && !ignoreEnterKeyUp.current && closingKeys.includes(event.key)) {
        var _buttonRef$current;
        buttonRef == null ? void 0 : (_buttonRef$current = buttonRef.current) == null ? void 0 : _buttonRef$current.focus();
      }
      ignoreEnterKeyUp.current = false;
    };
  };
  var createHandleListboxItemClick = function createHandleListboxItemClick(otherHandlers) {
    return function (event) {
      var _otherHandlers$onClic2;
      otherHandlers == null ? void 0 : (_otherHandlers$onClic2 = otherHandlers.onClick) == null ? void 0 : _otherHandlers$onClic2.call(otherHandlers, event);
      if (event.defaultPrevented) {
        return;
      }
      if (!multiple) {
        onOpenChange == null ? void 0 : onOpenChange(false);
      }
    };
  };
  var createHandleListboxBlur = function createHandleListboxBlur(otherHandlers) {
    return function (event) {
      var _otherHandlers$onBlur;
      otherHandlers == null ? void 0 : (_otherHandlers$onBlur = otherHandlers.onBlur) == null ? void 0 : _otherHandlers$onBlur.call(otherHandlers, event);
      if (!event.defaultPrevented) {
        onOpenChange == null ? void 0 : onOpenChange(false);
      }
    };
  };
  var listboxReducer = function listboxReducer(state, action) {
    var newState = defaultListboxReducer(state, action);

    // change selection when listbox is closed
    if (action.type === ActionTypes.keyDown && !open && (action.event.key === 'ArrowUp' || action.event.key === 'ArrowDown')) {
      return _extends({}, newState, {
        selectedValue: newState.highlightedValue
      });
    }
    if (action.type === ActionTypes.blur || action.type === ActionTypes.setValue || action.type === ActionTypes.optionsChange) {
      return _extends({}, newState, {
        highlightedValue: newState.selectedValue
      });
    }
    return newState;
  };
  var _useButton = useButton({
      disabled: disabled,
      ref: handleButtonRef
    }),
    getButtonRootProps = _useButton.getRootProps,
    buttonActive = _useButton.active,
    buttonFocusVisible = _useButton.focusVisible;
  var selectedOption = React.useMemo(function () {
    var _props$options$find;
    return props.multiple ? props.options.filter(function (o) {
      return value.includes(o.value);
    }) : (_props$options$find = props.options.find(function (o) {
      return o.value === value;
    })) != null ? _props$options$find : null;
  }, [props.multiple, props.options, value]);
  var useListboxParameters;
  if (props.multiple) {
    var onChangeMultiple = onChange;
    useListboxParameters = {
      id: listboxId,
      isOptionDisabled: function isOptionDisabled(o) {
        var _o$disabled;
        return (_o$disabled = o == null ? void 0 : o.disabled) != null ? _o$disabled : false;
      },
      optionComparer: function optionComparer(o, v) {
        return (o == null ? void 0 : o.value) === (v == null ? void 0 : v.value);
      },
      listboxRef: handleListboxRef,
      multiple: true,
      onChange: function onChange(e, newOptions) {
        var newValues = newOptions.map(function (o) {
          return o.value;
        });
        setValue(newValues);
        onChangeMultiple == null ? void 0 : onChangeMultiple(e, newValues);
      },
      options: options,
      optionStringifier: optionStringifier,
      value: selectedOption
    };
  } else {
    var onChangeSingle = onChange;
    useListboxParameters = {
      id: listboxId,
      isOptionDisabled: function isOptionDisabled(o) {
        var _o$disabled2;
        return (_o$disabled2 = o == null ? void 0 : o.disabled) != null ? _o$disabled2 : false;
      },
      optionComparer: function optionComparer(o, v) {
        return (o == null ? void 0 : o.value) === (v == null ? void 0 : v.value);
      },
      listboxRef: handleListboxRef,
      multiple: false,
      onChange: function onChange(e, option) {
        var _option$value, _option$value2;
        setValue((_option$value = option == null ? void 0 : option.value) != null ? _option$value : null);
        onChangeSingle == null ? void 0 : onChangeSingle(e, (_option$value2 = option == null ? void 0 : option.value) != null ? _option$value2 : null);
      },
      options: options,
      optionStringifier: optionStringifier,
      stateReducer: listboxReducer,
      value: selectedOption
    };
  }
  var _useListbox = useListbox(useListboxParameters),
    getListboxRootProps = _useListbox.getRootProps,
    getListboxOptionProps = _useListbox.getOptionProps,
    getOptionState = _useListbox.getOptionState,
    highlightedOption = _useListbox.highlightedOption,
    listboxSelectedOption = _useListbox.selectedOption;
  var getButtonProps = function getButtonProps() {
    var otherHandlers = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
    return _extends({}, getButtonRootProps(_extends({}, otherHandlers, {
      onClick: createHandleButtonClick(otherHandlers),
      onMouseDown: createHandleMouseDown(otherHandlers),
      onKeyDown: createHandleButtonKeyDown(otherHandlers)
    })), {
      role: 'combobox',
      'aria-expanded': open,
      'aria-haspopup': 'listbox',
      'aria-controls': listboxId
    });
  };
  var getListboxProps = function getListboxProps() {
    var otherHandlers = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
    return getListboxRootProps(_extends({}, otherHandlers, {
      onBlur: createHandleListboxBlur(otherHandlers),
      onKeyUp: createHandleListboxKeyUp(otherHandlers)
    }));
  };
  var getOptionProps = function getOptionProps(option) {
    var otherHandlers = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    return getListboxOptionProps(option, _extends({}, otherHandlers, {
      onClick: createHandleListboxItemClick(otherHandlers)
    }));
  };
  React.useDebugValue({
    selectedOption: listboxSelectedOption,
    highlightedOption: highlightedOption,
    open: open
  });
  return {
    buttonActive: buttonActive,
    buttonFocusVisible: buttonFocusVisible,
    disabled: disabled,
    getButtonProps: getButtonProps,
    getListboxProps: getListboxProps,
    getOptionProps: getOptionProps,
    getOptionState: getOptionState,
    open: open,
    value: value
  };
}
export default useSelect;