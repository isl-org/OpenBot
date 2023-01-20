import _extends from "@babel/runtime/helpers/esm/extends";
import _objectWithoutPropertiesLoose from "@babel/runtime/helpers/esm/objectWithoutPropertiesLoose";
const _excluded = ["getRootProps"];
import { useTabContext, getTabId, getPanelId } from '../TabsUnstyled';
import { useButton } from '../ButtonUnstyled';
const useTab = parameters => {
  const {
    value: valueProp,
    onChange,
    onClick,
    onFocus
  } = parameters;
  const _useButton = useButton(parameters),
    {
      getRootProps: getRootPropsButton
    } = _useButton,
    otherButtonProps = _objectWithoutPropertiesLoose(_useButton, _excluded);
  const context = useTabContext();
  if (context === null) {
    throw new Error('No TabContext provided');
  }
  const value = valueProp ?? 0;
  const selected = context.value === value;
  const selectionFollowsFocus = context.selectionFollowsFocus;
  const a11yAttributes = {
    role: 'tab',
    'aria-controls': getPanelId(context, value) ?? undefined,
    id: getTabId(context, value) ?? undefined,
    'aria-selected': selected,
    disabled: otherButtonProps.disabled
  };
  const createHandleFocus = otherHandlers => event => {
    otherHandlers.onFocus?.(event);
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
    otherHandlers.onClick?.(event);
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
    const buttonResolvedProps = getRootPropsButton(_extends({}, otherHandlers, {
      onClick: createHandleClick(otherHandlers),
      onFocus: createHandleFocus(otherHandlers)
    }));
    return _extends({}, buttonResolvedProps, a11yAttributes);
  };
  return _extends({
    getRootProps
  }, otherButtonProps, {
    selected
  });
};
export default useTab;