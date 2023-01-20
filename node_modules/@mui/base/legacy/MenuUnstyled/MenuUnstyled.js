import _extends from "@babel/runtime/helpers/esm/extends";
import _objectWithoutProperties from "@babel/runtime/helpers/esm/objectWithoutProperties";
import * as React from 'react';
import PropTypes from 'prop-types';
import { HTMLElementType, refType } from '@mui/utils';
import MenuUnstyledContext from './MenuUnstyledContext';
import { getMenuUnstyledUtilityClass } from './menuUnstyledClasses';
import useMenu from './useMenu';
import composeClasses from '../composeClasses';
import PopperUnstyled from '../PopperUnstyled';
import useSlotProps from '../utils/useSlotProps';
import { jsx as _jsx } from "react/jsx-runtime";
function getUtilityClasses(ownerState) {
  var open = ownerState.open;
  var slots = {
    root: ['root', open && 'expanded'],
    listbox: ['listbox', open && 'expanded']
  };
  return composeClasses(slots, getMenuUnstyledUtilityClass, {});
}
/**
 *
 * Demos:
 *
 * - [Unstyled Menu](https://mui.com/base/react-menu/)
 *
 * API:
 *
 * - [MenuUnstyled API](https://mui.com/base/api/menu-unstyled/)
 */
var MenuUnstyled = /*#__PURE__*/React.forwardRef(function MenuUnstyled(props, forwardedRef) {
  var _ref, _slots$listbox;
  var actions = props.actions,
    anchorEl = props.anchorEl,
    children = props.children,
    component = props.component,
    _props$keepMounted = props.keepMounted,
    keepMounted = _props$keepMounted === void 0 ? false : _props$keepMounted,
    listboxId = props.listboxId,
    onClose = props.onClose,
    _props$open = props.open,
    open = _props$open === void 0 ? false : _props$open,
    _props$slotProps = props.slotProps,
    slotProps = _props$slotProps === void 0 ? {} : _props$slotProps,
    _props$slots = props.slots,
    slots = _props$slots === void 0 ? {} : _props$slots,
    other = _objectWithoutProperties(props, ["actions", "anchorEl", "children", "component", "keepMounted", "listboxId", "onClose", "open", "slotProps", "slots"]);
  var _useMenu = useMenu({
      open: open,
      onClose: onClose,
      listboxId: listboxId
    }),
    registerItem = _useMenu.registerItem,
    unregisterItem = _useMenu.unregisterItem,
    getListboxProps = _useMenu.getListboxProps,
    getItemProps = _useMenu.getItemProps,
    getItemState = _useMenu.getItemState,
    highlightFirstItem = _useMenu.highlightFirstItem,
    highlightLastItem = _useMenu.highlightLastItem;
  React.useImperativeHandle(actions, function () {
    return {
      highlightFirstItem: highlightFirstItem,
      highlightLastItem: highlightLastItem
    };
  }, [highlightFirstItem, highlightLastItem]);
  var ownerState = _extends({}, props, {
    open: open
  });
  var classes = getUtilityClasses(ownerState);
  var Root = (_ref = component != null ? component : slots.root) != null ? _ref : PopperUnstyled;
  var rootProps = useSlotProps({
    elementType: Root,
    externalForwardedProps: other,
    externalSlotProps: slotProps.root,
    additionalProps: {
      anchorEl: anchorEl,
      open: open,
      keepMounted: keepMounted,
      role: undefined,
      ref: forwardedRef
    },
    className: classes.root,
    ownerState: ownerState
  });
  var Listbox = (_slots$listbox = slots.listbox) != null ? _slots$listbox : 'ul';
  var listboxProps = useSlotProps({
    elementType: Listbox,
    getSlotProps: getListboxProps,
    externalSlotProps: slotProps.listbox,
    ownerState: ownerState,
    className: classes.listbox
  });
  var contextValue = React.useMemo(function () {
    return {
      registerItem: registerItem,
      unregisterItem: unregisterItem,
      getItemState: getItemState,
      getItemProps: getItemProps,
      open: open
    };
  }, [getItemProps, getItemState, open, registerItem, unregisterItem]);
  return /*#__PURE__*/_jsx(Root, _extends({}, rootProps, {
    children: /*#__PURE__*/_jsx(Listbox, _extends({}, listboxProps, {
      children: /*#__PURE__*/_jsx(MenuUnstyledContext.Provider, {
        value: contextValue,
        children: children
      })
    }))
  }));
});
process.env.NODE_ENV !== "production" ? MenuUnstyled.propTypes /* remove-proptypes */ = {
  // ----------------------------- Warning --------------------------------
  // | These PropTypes are generated from the TypeScript type definitions |
  // |     To update them edit TypeScript types and run "yarn proptypes"  |
  // ----------------------------------------------------------------------
  /**
   * A ref with imperative actions.
   * It allows to select the first or last menu item.
   */
  actions: refType,
  /**
   * An HTML element, [virtualElement](https://popper.js.org/docs/v2/virtual-elements/),
   * or a function that returns either.
   * It's used to set the position of the popper.
   */
  anchorEl: PropTypes /* @typescript-to-proptypes-ignore */.oneOfType([HTMLElementType, PropTypes.object, PropTypes.func]),
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
   * Always keep the menu in the DOM.
   * This prop can be useful in SEO situation or when you want to maximize the responsiveness of the Menu.
   *
   * @default false
   */
  keepMounted: PropTypes.bool,
  /**
   * @ignore
   */
  listboxId: PropTypes.string,
  /**
   * Triggered when focus leaves the menu and the menu should close.
   */
  onClose: PropTypes.func,
  /**
   * Controls whether the menu is displayed.
   * @default false
   */
  open: PropTypes.bool,
  /**
   * The props used for each slot inside the Menu.
   * @default {}
   */
  slotProps: PropTypes.shape({
    listbox: PropTypes.oneOfType([PropTypes.func, PropTypes.object]),
    root: PropTypes.oneOfType([PropTypes.func, PropTypes.object])
  }),
  /**
   * The components used for each slot inside the Menu.
   * Either a string to use a HTML element or a component.
   * @default {}
   */
  slots: PropTypes.shape({
    listbox: PropTypes.elementType,
    root: PropTypes.elementType
  })
} : void 0;
export default MenuUnstyled;