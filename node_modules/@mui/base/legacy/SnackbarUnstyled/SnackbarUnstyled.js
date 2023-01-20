import _extends from "@babel/runtime/helpers/esm/extends";
import _objectWithoutProperties from "@babel/runtime/helpers/esm/objectWithoutProperties";
import * as React from 'react';
import PropTypes from 'prop-types';
import ClickAwayListener from '../ClickAwayListener';
import composeClasses from '../composeClasses';
import { getSnackbarUnstyledUtilityClass } from './snackbarUnstyledClasses';
import useSnackbar from './useSnackbar';
import { useSlotProps } from '../utils';
import { jsx as _jsx } from "react/jsx-runtime";
var useUtilityClasses = function useUtilityClasses() {
  var slots = {
    root: ['root']
  };
  return composeClasses(slots, getSnackbarUnstyledUtilityClass, undefined);
};
/**
 *
 * Demos:
 *
 * - [Unstyled Snackbar](https://mui.com/base/react-snackbar/)
 *
 * API:
 *
 * - [SnackbarUnstyled API](https://mui.com/base/api/snackbar-unstyled/)
 */
var SnackbarUnstyled = /*#__PURE__*/React.forwardRef(function SnackbarUnstyled(props, ref) {
  var _props$autoHideDurati = props.autoHideDuration,
    autoHideDuration = _props$autoHideDurati === void 0 ? null : _props$autoHideDurati,
    children = props.children,
    component = props.component,
    _props$disableWindowB = props.disableWindowBlurListener,
    disableWindowBlurListener = _props$disableWindowB === void 0 ? false : _props$disableWindowB,
    _props$exited = props.exited,
    exited = _props$exited === void 0 ? true : _props$exited,
    onBlur = props.onBlur,
    onClose = props.onClose,
    onFocus = props.onFocus,
    onMouseEnter = props.onMouseEnter,
    onMouseLeave = props.onMouseLeave,
    open = props.open,
    resumeHideDuration = props.resumeHideDuration,
    _props$slotProps = props.slotProps,
    slotProps = _props$slotProps === void 0 ? {} : _props$slotProps,
    _props$slots = props.slots,
    slots = _props$slots === void 0 ? {} : _props$slots,
    other = _objectWithoutProperties(props, ["autoHideDuration", "children", "component", "disableWindowBlurListener", "exited", "onBlur", "onClose", "onFocus", "onMouseEnter", "onMouseLeave", "open", "resumeHideDuration", "slotProps", "slots"]);
  var classes = useUtilityClasses();
  var _useSnackbar = useSnackbar(_extends({}, props, {
      autoHideDuration: autoHideDuration,
      disableWindowBlurListener: disableWindowBlurListener,
      onClose: onClose,
      open: open,
      resumeHideDuration: resumeHideDuration,
      ref: ref
    })),
    getRootProps = _useSnackbar.getRootProps,
    onClickAway = _useSnackbar.onClickAway;
  var ownerState = props;
  var Root = component || slots.root || 'div';
  var rootProps = useSlotProps({
    elementType: Root,
    getSlotProps: getRootProps,
    externalForwardedProps: other,
    externalSlotProps: slotProps.root,
    additionalProps: {
      ref: ref
    },
    ownerState: ownerState,
    className: classes.root
  });
  var clickAwayListenerProps = useSlotProps({
    elementType: ClickAwayListener,
    externalSlotProps: slotProps.clickAwayListener,
    additionalProps: {
      onClickAway: onClickAway
    },
    ownerState: ownerState
  });

  // ClickAwayListener doesn't support ownerState
  delete clickAwayListenerProps.ownerState;

  // So that we only render active snackbars.
  if (!open && exited) {
    return null;
  }
  return /*#__PURE__*/_jsx(ClickAwayListener, _extends({}, clickAwayListenerProps, {
    children: /*#__PURE__*/_jsx(Root, _extends({}, rootProps, {
      children: children
    }))
  }));
});
process.env.NODE_ENV !== "production" ? SnackbarUnstyled.propTypes /* remove-proptypes */ = {
  // ----------------------------- Warning --------------------------------
  // | These PropTypes are generated from the TypeScript type definitions |
  // |     To update them edit TypeScript types and run "yarn proptypes"  |
  // ----------------------------------------------------------------------
  /**
   * The number of milliseconds to wait before automatically calling the
   * `onClose` function. `onClose` should then set the state of the `open`
   * prop to hide the Snackbar. This behavior is disabled by default with
   * the `null` value.
   * @default null
   */
  autoHideDuration: PropTypes.number,
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
   * If `true`, the `autoHideDuration` timer will expire even if the window is not focused.
   * @default false
   */
  disableWindowBlurListener: PropTypes.bool,
  /**
   * The prop used to handle exited transition and unmount the component.
   * @default true
   */
  exited: PropTypes.bool,
  /**
   * @ignore
   */
  onBlur: PropTypes.func,
  /**
   * Callback fired when the component requests to be closed.
   * Typically `onClose` is used to set state in the parent component,
   * which is used to control the `Snackbar` `open` prop.
   * The `reason` parameter can optionally be used to control the response to `onClose`,
   * for example ignoring `clickaway`.
   *
   * @param {React.SyntheticEvent<any> | Event} event The event source of the callback.
   * @param {string} reason Can be: `"timeout"` (`autoHideDuration` expired), `"clickaway"`, or `"escapeKeyDown"`.
   */
  onClose: PropTypes.func,
  /**
   * @ignore
   */
  onFocus: PropTypes.func,
  /**
   * @ignore
   */
  onMouseEnter: PropTypes.func,
  /**
   * @ignore
   */
  onMouseLeave: PropTypes.func,
  /**
   * If `true`, the component is shown.
   */
  open: PropTypes.bool,
  /**
   * The number of milliseconds to wait before dismissing after user interaction.
   * If `autoHideDuration` prop isn't specified, it does nothing.
   * If `autoHideDuration` prop is specified but `resumeHideDuration` isn't,
   * we default to `autoHideDuration / 2` ms.
   */
  resumeHideDuration: PropTypes.number,
  /**
   * The props used for each slot inside the Snackbar.
   * @default {}
   */
  slotProps: PropTypes.shape({
    clickAwayListener: PropTypes.oneOfType([PropTypes.func, PropTypes.shape({
      children: PropTypes.element.isRequired,
      disableReactTree: PropTypes.bool,
      mouseEvent: PropTypes.oneOf(['onClick', 'onMouseDown', 'onMouseUp', 'onPointerDown', 'onPointerUp', false]),
      onClickAway: PropTypes.func,
      touchEvent: PropTypes.oneOf(['onTouchEnd', 'onTouchStart', false])
    })]),
    root: PropTypes.oneOfType([PropTypes.func, PropTypes.object])
  }),
  /**
   * The components used for each slot inside the Snackbar.
   * Either a string to use a HTML element or a component.
   * @default {}
   */
  slots: PropTypes.shape({
    root: PropTypes.elementType
  })
} : void 0;
export default SnackbarUnstyled;