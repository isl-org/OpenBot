import _extends from "@babel/runtime/helpers/esm/extends";
import * as React from 'react';
import { unstable_useId as useId, unstable_useForkRef as useForkRef } from '@mui/utils';
import { MenuUnstyledContext } from '../MenuUnstyled';
import { useButton } from '../ButtonUnstyled';
export default function useMenuItem(props) {
  var _itemState$disabled;
  const {
    disabled = false,
    ref,
    label
  } = props;
  const id = useId();
  const menuContext = React.useContext(MenuUnstyledContext);
  const itemRef = React.useRef(null);
  const handleRef = useForkRef(itemRef, ref);
  if (menuContext === null) {
    throw new Error('MenuItemUnstyled must be used within a MenuUnstyled');
  }
  const {
    registerItem,
    unregisterItem,
    open
  } = menuContext;
  React.useEffect(() => {
    if (id === undefined) {
      return undefined;
    }
    registerItem(id, {
      disabled,
      id,
      ref: itemRef,
      label
    });
    return () => unregisterItem(id);
  }, [id, registerItem, unregisterItem, disabled, ref, label]);
  const {
    getRootProps: getButtonProps,
    focusVisible
  } = useButton({
    disabled,
    focusableWhenDisabled: true,
    ref: handleRef
  });

  // Ensure the menu item is focused when highlighted
  const [focusRequested, requestFocus] = React.useState(false);
  const focusIfRequested = React.useCallback(() => {
    if (focusRequested && itemRef.current != null) {
      itemRef.current.focus();
      requestFocus(false);
    }
  }, [focusRequested]);
  React.useEffect(() => {
    focusIfRequested();
  });
  React.useDebugValue({
    id,
    disabled,
    label
  });
  const itemState = menuContext.getItemState(id != null ? id : '');
  const {
    highlighted
  } = itemState != null ? itemState : {
    highlighted: false
  };
  React.useEffect(() => {
    requestFocus(highlighted && open);
  }, [highlighted, open]);
  if (id === undefined) {
    return {
      getRootProps: other => _extends({}, other, getButtonProps(other), {
        role: 'menuitem'
      }),
      disabled: false,
      focusVisible
    };
  }
  return {
    getRootProps: other => {
      const optionProps = menuContext.getItemProps(id, other);
      return _extends({}, other, getButtonProps(other), {
        tabIndex: optionProps.tabIndex,
        id: optionProps.id,
        role: 'menuitem'
      });
    },
    disabled: (_itemState$disabled = itemState == null ? void 0 : itemState.disabled) != null ? _itemState$disabled : false,
    focusVisible
  };
}