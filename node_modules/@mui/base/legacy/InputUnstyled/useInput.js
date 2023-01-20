import _extends from "@babel/runtime/helpers/esm/extends";
import { formatMuiErrorMessage as _formatMuiErrorMessage } from "@mui/utils";
import * as React from 'react';
import { unstable_useForkRef as useForkRef } from '@mui/utils';
import { useFormControlUnstyledContext } from '../FormControlUnstyled';
import extractEventHandlers from '../utils/extractEventHandlers';
export default function useInput(parameters) {
  var defaultValueProp = parameters.defaultValue,
    _parameters$disabled = parameters.disabled,
    disabledProp = _parameters$disabled === void 0 ? false : _parameters$disabled,
    _parameters$error = parameters.error,
    errorProp = _parameters$error === void 0 ? false : _parameters$error,
    onBlur = parameters.onBlur,
    onChange = parameters.onChange,
    onFocus = parameters.onFocus,
    _parameters$required = parameters.required,
    requiredProp = _parameters$required === void 0 ? false : _parameters$required,
    valueProp = parameters.value;
  var formControlContext = useFormControlUnstyledContext();
  var defaultValue;
  var disabled;
  var error;
  var required;
  var value;
  if (formControlContext) {
    var _formControlContext$d, _formControlContext$e, _formControlContext$r;
    defaultValue = undefined;
    disabled = (_formControlContext$d = formControlContext.disabled) != null ? _formControlContext$d : false;
    error = (_formControlContext$e = formControlContext.error) != null ? _formControlContext$e : false;
    required = (_formControlContext$r = formControlContext.required) != null ? _formControlContext$r : false;
    value = formControlContext.value;
    if (process.env.NODE_ENV !== 'production') {
      var definedLocalProps = ['defaultValue', 'disabled', 'error', 'required', 'value'].filter(function (prop) {
        return parameters[prop] !== undefined;
      });
      if (definedLocalProps.length > 0) {
        console.warn(['MUI: You have set props on an input that is inside a FormControlUnstyled.', 'Set these props on a FormControlUnstyled instead. Otherwise they will be ignored.', "Ignored props: ".concat(definedLocalProps.join(', '))].join('\n'));
      }
    }
  } else {
    defaultValue = defaultValueProp;
    disabled = disabledProp;
    error = errorProp;
    required = requiredProp;
    value = valueProp;
  }
  var _React$useRef = React.useRef(value != null),
    isControlled = _React$useRef.current;
  var handleInputRefWarning = React.useCallback(function (instance) {
    if (process.env.NODE_ENV !== 'production') {
      if (instance && instance.nodeName !== 'INPUT' && !instance.focus) {
        console.error(['MUI: You have provided a `slots.input` to the input component', 'that does not correctly handle the `ref` prop.', 'Make sure the `ref` prop is called with a HTMLInputElement.'].join('\n'));
      }
    }
  }, []);
  var inputRef = React.useRef(null);
  var handleInputRef = useForkRef(inputRef, handleInputRefWarning);
  var _React$useState = React.useState(false),
    focused = _React$useState[0],
    setFocused = _React$useState[1]; // The blur won't fire when the disabled state is set on a focused input.
  // We need to book keep the focused state manually.
  React.useEffect(function () {
    if (!formControlContext && disabled && focused) {
      setFocused(false);

      // @ts-ignore
      onBlur == null ? void 0 : onBlur();
    }
  }, [formControlContext, disabled, focused, onBlur]);
  var handleFocus = function handleFocus(otherHandlers) {
    return function (event) {
      var _otherHandlers$onFocu;
      // Fix a bug with IE11 where the focus/blur events are triggered
      // while the component is disabled.
      if (formControlContext != null && formControlContext.disabled) {
        event.stopPropagation();
        return;
      }
      (_otherHandlers$onFocu = otherHandlers.onFocus) == null ? void 0 : _otherHandlers$onFocu.call(otherHandlers, event);
      if (formControlContext && formControlContext.onFocus) {
        var _formControlContext$o;
        formControlContext == null ? void 0 : (_formControlContext$o = formControlContext.onFocus) == null ? void 0 : _formControlContext$o.call(formControlContext);
      } else {
        setFocused(true);
      }
    };
  };
  var handleBlur = function handleBlur(otherHandlers) {
    return function (event) {
      var _otherHandlers$onBlur;
      (_otherHandlers$onBlur = otherHandlers.onBlur) == null ? void 0 : _otherHandlers$onBlur.call(otherHandlers, event);
      if (formControlContext && formControlContext.onBlur) {
        formControlContext.onBlur();
      } else {
        setFocused(false);
      }
    };
  };
  var handleChange = function handleChange(otherHandlers) {
    return function (event) {
      var _formControlContext$o2, _otherHandlers$onChan;
      if (!isControlled) {
        var element = event.target || inputRef.current;
        if (element == null) {
          throw new Error(process.env.NODE_ENV !== "production" ? "MUI: Expected valid input target. Did you use a custom `slots.input` and forget to forward refs? See https://mui.com/r/input-component-ref-interface for more info." : _formatMuiErrorMessage(17));
        }
      }
      formControlContext == null ? void 0 : (_formControlContext$o2 = formControlContext.onChange) == null ? void 0 : _formControlContext$o2.call(formControlContext, event);

      // @ts-ignore
      for (var _len = arguments.length, args = new Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {
        args[_key - 1] = arguments[_key];
      }
      (_otherHandlers$onChan = otherHandlers.onChange) == null ? void 0 : _otherHandlers$onChan.call.apply(_otherHandlers$onChan, [otherHandlers, event].concat(args));
    };
  };
  var handleClick = function handleClick(otherHandlers) {
    return function (event) {
      var _otherHandlers$onClic;
      if (inputRef.current && event.currentTarget === event.target) {
        inputRef.current.focus();
      }
      (_otherHandlers$onClic = otherHandlers.onClick) == null ? void 0 : _otherHandlers$onClic.call(otherHandlers, event);
    };
  };
  var getRootProps = function getRootProps() {
    var externalProps = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
    // onBlur, onChange and onFocus are forwarded to the input slot.
    var propsEventHandlers = extractEventHandlers(parameters, ['onBlur', 'onChange', 'onFocus']);
    var externalEventHandlers = _extends({}, propsEventHandlers, extractEventHandlers(externalProps));
    return _extends({}, externalProps, externalEventHandlers, {
      onClick: handleClick(externalEventHandlers)
    });
  };
  var getInputProps = function getInputProps() {
    var externalProps = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
    var propsEventHandlers = {
      onBlur: onBlur,
      onChange: onChange,
      onFocus: onFocus
    };
    var externalEventHandlers = _extends({}, propsEventHandlers, extractEventHandlers(externalProps));
    var mergedEventHandlers = _extends({}, externalProps, externalEventHandlers, {
      onBlur: handleBlur(externalEventHandlers),
      onChange: handleChange(externalEventHandlers),
      onFocus: handleFocus(externalEventHandlers)
    });
    return _extends({}, mergedEventHandlers, {
      'aria-invalid': error || undefined,
      defaultValue: defaultValue,
      ref: handleInputRef,
      value: value,
      required: required,
      disabled: disabled
    });
  };
  return {
    disabled: disabled,
    error: error,
    focused: focused,
    formControlContext: formControlContext,
    getInputProps: getInputProps,
    getRootProps: getRootProps,
    required: required,
    value: value
  };
}