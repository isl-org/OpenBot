"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var _objectWithoutPropertiesLoose2 = _interopRequireDefault(require("@babel/runtime/helpers/objectWithoutPropertiesLoose"));
var _TabsUnstyled = require("../TabsUnstyled");
var _ButtonUnstyled = require("../ButtonUnstyled");
const _excluded = ["getRootProps"];
const useTab = parameters => {
  var _getPanelId, _getTabId;
  const {
    value: valueProp,
    onChange,
    onClick,
    onFocus
  } = parameters;
  const _useButton = (0, _ButtonUnstyled.useButton)(parameters),
    {
      getRootProps: getRootPropsButton
    } = _useButton,
    otherButtonProps = (0, _objectWithoutPropertiesLoose2.default)(_useButton, _excluded);
  const context = (0, _TabsUnstyled.useTabContext)();
  if (context === null) {
    throw new Error('No TabContext provided');
  }
  const value = valueProp != null ? valueProp : 0;
  const selected = context.value === value;
  const selectionFollowsFocus = context.selectionFollowsFocus;
  const a11yAttributes = {
    role: 'tab',
    'aria-controls': (_getPanelId = (0, _TabsUnstyled.getPanelId)(context, value)) != null ? _getPanelId : undefined,
    id: (_getTabId = (0, _TabsUnstyled.getTabId)(context, value)) != null ? _getTabId : undefined,
    'aria-selected': selected,
    disabled: otherButtonProps.disabled
  };
  const createHandleFocus = otherHandlers => event => {
    var _otherHandlers$onFocu;
    (_otherHandlers$onFocu = otherHandlers.onFocus) == null ? void 0 : _otherHandlers$onFocu.call(otherHandlers, event);
    if (event.defaultPrevented) {
      return;
    }
    if (selectionFollowsFocus && !selected) {
      if (onChange) {
        onChange(event, value);
      }
      context.onSelected(event, value);
    }
    if (onFocus) {
      onFocus(event);
    }
  };
  const createHandleClick = otherHandlers => event => {
    var _otherHandlers$onClic;
    (_otherHandlers$onClic = otherHandlers.onClick) == null ? void 0 : _otherHandlers$onClic.call(otherHandlers, event);
    if (event.defaultPrevented) {
      return;
    }
    if (!selected) {
      if (onChange) {
        onChange(event, value);
      }
      context.onSelected(event, value);
    }
    if (onClick) {
      onClick(event);
    }
  };
  const getRootProps = (otherHandlers = {}) => {
    const buttonResolvedProps = getRootPropsButton((0, _extends2.default)({}, otherHandlers, {
      onClick: createHandleClick(otherHandlers),
      onFocus: createHandleFocus(otherHandlers)
    }));
    return (0, _extends2.default)({}, buttonResolvedProps, a11yAttributes);
  };
  return (0, _extends2.default)({
    getRootProps
  }, otherButtonProps, {
    selected
  });
};
var _default = useTab;
exports.default = _default;