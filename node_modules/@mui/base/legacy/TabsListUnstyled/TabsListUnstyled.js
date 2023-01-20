import _extends from "@babel/runtime/helpers/esm/extends";
import _objectWithoutProperties from "@babel/runtime/helpers/esm/objectWithoutProperties";
import * as React from 'react';
import PropTypes from 'prop-types';
import composeClasses from '../composeClasses';
import { useSlotProps } from '../utils';
import { getTabsListUnstyledUtilityClass } from './tabsListUnstyledClasses';
import useTabsList from './useTabsList';
import { jsx as _jsx } from "react/jsx-runtime";
var useUtilityClasses = function useUtilityClasses(ownerState) {
  var orientation = ownerState.orientation;
  var slots = {
    root: ['root', orientation]
  };
  return composeClasses(slots, getTabsListUnstyledUtilityClass, {});
};

/**
 *
 * Demos:
 *
 * - [Unstyled Tabs](https://mui.com/base/react-tabs/)
 *
 * API:
 *
 * - [TabsListUnstyled API](https://mui.com/base/api/tabs-list-unstyled/)
 */
var TabsListUnstyled = /*#__PURE__*/React.forwardRef(function (props, ref) {
  var _ref;
  var children = props.children,
    component = props.component,
    _props$slotProps = props.slotProps,
    slotProps = _props$slotProps === void 0 ? {} : _props$slotProps,
    _props$slots = props.slots,
    slots = _props$slots === void 0 ? {} : _props$slots,
    other = _objectWithoutProperties(props, ["children", "component", "slotProps", "slots"]);
  var _useTabsList = useTabsList(_extends({}, props, {
      ref: ref
    })),
    isRtl = _useTabsList.isRtl,
    orientation = _useTabsList.orientation,
    getRootProps = _useTabsList.getRootProps,
    processChildren = _useTabsList.processChildren;
  var ownerState = _extends({}, props, {
    isRtl: isRtl,
    orientation: orientation
  });
  var classes = useUtilityClasses(ownerState);
  var TabsListRoot = (_ref = component != null ? component : slots.root) != null ? _ref : 'div';
  var tabsListRootProps = useSlotProps({
    elementType: TabsListRoot,
    getSlotProps: getRootProps,
    externalSlotProps: slotProps.root,
    externalForwardedProps: other,
    ownerState: ownerState,
    className: classes.root
  });
  var processedChildren = processChildren();
  return /*#__PURE__*/_jsx(TabsListRoot, _extends({}, tabsListRootProps, {
    children: processedChildren
  }));
});
process.env.NODE_ENV !== "production" ? TabsListUnstyled.propTypes /* remove-proptypes */ = {
  // ----------------------------- Warning --------------------------------
  // | These PropTypes are generated from the TypeScript type definitions |
  // |     To update them edit TypeScript types and run "yarn proptypes"  |
  // ----------------------------------------------------------------------
  /**
   * The content of the component.
   */
  children: PropTypes.node,
  /**
   * The component used for the root node.
   * Either a string to use a HTML element or a component.
   */
  component: PropTypes.elementType,
  /**
   * The props used for each slot inside the TabsList.
   * @default {}
   */
  slotProps: PropTypes.shape({
    root: PropTypes.oneOfType([PropTypes.func, PropTypes.object])
  }),
  /**
   * The components used for each slot inside the TabsList.
   * Either a string to use a HTML element or a component.
   * @default {}
   */
  slots: PropTypes.shape({
    root: PropTypes.elementType
  })
} : void 0;
export default TabsListUnstyled;