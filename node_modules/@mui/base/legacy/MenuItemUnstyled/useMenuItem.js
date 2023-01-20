import _extends from "@babel/runtime/helpers/esm/extends";
import * as React from 'react';
import { unstable_useId as useId, unstable_useForkRef as useForkRef } from '@mui/utils';
import { MenuUnstyledContext } from '../MenuUnstyled';
import { useButton } from '../ButtonUnstyled';
export default function useMenuItem(props) {
  var _itemState$disabled;
  var _props$disabled = props.disabled,
    disabled = _props$disabled === void 0 ? false : _props$disabled,
    ref = props.ref,
    label = props.label;
  var id = useId();
  var menuContext = React.useContext(MenuUnstyledContext);
  var itemRef = React.useRef(null);
  var handleRef = useForkRef(itemRef, ref);
  if (menuContext === null) {
    throw new Error('MenuItemUnstyled must be used within a MenuUnstyled');
  }
  var registerItem = menuContext.registerItem,
    unregisterItem = menuContext.unregisterItem,
    open = menuContext.open;
  React.useEffect(function () {
    if (id === undefined) {
      return undefined;
    }
    registerItem(id, {
      disabled: disabled,
      id: id,
      ref: itemRef,
      label: label
    });
    return function () {
      return unregisterItem(id);
    };
  }, [id, registerItem, unregisterItem, disabled, ref, label]);
  var _useButton = useButton({
      disabled: disabled,
      focusableWhenDisabled: true,
      ref: handleRef
    }),
    getButtonProps = _useButton.getRootProps,
    focusVisible = _useButton.focusVisible; // Ensure the menu item is focused when highlighted
  var _React$useState = React.useState(false),
    focusRequested = _React$useState[0],
    requestFocus = _React$useState[1];
  var focusIfRequested = React.useCallback(function () {
    if (focusRequested && itemRef.current != null) {
      itemRef.current.focus();
      requestFocus(false);
    }
  }, [focusRequested]);
  React.useEffect(function () {
    focusIfRequested();
  });
  React.useDebugValue({
    id: id,
    disabled: disabled,
    label: label
  });
  var itemState = menuContext.getItemState(id != null ? id : '');
  var _ref = itemState != null ? itemState : {
      highlighted: false
    },
    highlighted = _ref.highlighted;
  React.useEffect(function () {
    requestFocus(highlighted && open);
  }, [highlighted, open]);
  if (id === undefined) {
    return {
      getRootProps: function getRootProps(other) {
        return _extends({}, other, getButtonProps(other), {
          role: 'menuitem'
        });
      },
      disabled: false,
      focusVisible: focusVisible
    };
  }
  return {
    getRootProps: function getRootProps(other) {
      var optionProps = menuContext.getItemProps(id, other);
      return _extends({}, other, getButtonProps(other), {
        tabIndex: optionProps.tabIndex,
        id: optionProps.id,
        role: 'menuitem'
      });
    },
    disabled: (_itemState$disabled = itemState == null ? void 0 : itemState.disabled) != null ? _itemState$disabled : false,
    focusVisible: focusVisible
  };
}