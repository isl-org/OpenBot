import _extends from "@babel/runtime/helpers/esm/extends";
import _objectWithoutProperties from "@babel/runtime/helpers/esm/objectWithoutProperties";
import { useTabContext, getTabId, getPanelId } from '../TabsUnstyled';
import { useButton } from '../ButtonUnstyled';
var useTab = function useTab(parameters) {
  var _getPanelId, _getTabId;
  var valueProp = parameters.value,
    onChange = parameters.onChange,
    onClick = parameters.onClick,
    onFocus = parameters.onFocus;
  var _useButton = useButton(parameters),
    getRootPropsButton = _useButton.getRootProps,
    otherButtonProps = _objectWithoutProperties(_useButton, ["getRootProps"]);
  var context = useTabContext();
  if (context === null) {
    throw new Error('No TabContext provided');
  }
  var value = valueProp != null ? valueProp : 0;
  var selected = context.value === value;
  var selectionFollowsFocus = context.selectionFollowsFocus;
  var a11yAttributes = {
    role: 'tab',
    'aria-controls': (_getPanelId = getPanelId(context, value)) != null ? _getPanelId : undefined,
    id: (_getTabId = getTabId(context, value)) != null ? _getTabId : undefined,
    'aria-selected': selected,
    disabled: otherButtonProps.disabled
  };
  var createHandleFocus = function createHandleFocus(otherHandlers) {
    return function (event) {
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
  };
  var createHandleClick = function createHandleClick(otherHandlers) {
    return function (event) {
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
  };
  var getRootProps = function getRootProps() {
    var otherHandlers = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
    var buttonResolvedProps = getRootPropsButton(_extends({}, otherHandlers, {
      onClick: createHandleClick(otherHandlers),
      onFocus: createHandleFocus(otherHandlers)
    }));
    return _extends({}, buttonResolvedProps, a11yAttributes);
  };
  return _extends({
    getRootProps: getRootProps
  }, otherButtonProps, {
    selected: selected
  });
};
export default useTab;