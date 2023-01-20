import _extends from "@babel/runtime/helpers/esm/extends";
import * as React from 'react';
import { unstable_useEventCallback as useEventCallback } from '@mui/utils';
import extractEventHandlers from '../utils/extractEventHandlers';

/**
 * The basic building block for creating custom snackbar.
 *
 * Demos:
 *
 * - [Snackbar](https://mui.com/base/react-snackbar/)
 */
export default function useSnackbar(parameters) {
  var _parameters$autoHideD = parameters.autoHideDuration,
    autoHideDuration = _parameters$autoHideD === void 0 ? null : _parameters$autoHideD,
    _parameters$disableWi = parameters.disableWindowBlurListener,
    disableWindowBlurListener = _parameters$disableWi === void 0 ? false : _parameters$disableWi,
    onClose = parameters.onClose,
    open = parameters.open,
    ref = parameters.ref,
    resumeHideDuration = parameters.resumeHideDuration;
  var timerAutoHide = React.useRef();
  React.useEffect(function () {
    if (!open) {
      return undefined;
    }

    /**
     * @param {KeyboardEvent} nativeEvent
     */
    function handleKeyDown(nativeEvent) {
      if (!nativeEvent.defaultPrevented) {
        // IE11, Edge (prior to using Blink?) use 'Esc'
        if (nativeEvent.key === 'Escape' || nativeEvent.key === 'Esc') {
          // not calling `preventDefault` since we don't know if people may ignore this event e.g. a permanently open snackbar
          onClose == null ? void 0 : onClose(nativeEvent, 'escapeKeyDown');
        }
      }
    }
    document.addEventListener('keydown', handleKeyDown);
    return function () {
      document.removeEventListener('keydown', handleKeyDown);
    };
  }, [open, onClose]);
  var handleClose = useEventCallback(function (event, reason) {
    onClose == null ? void 0 : onClose(event, reason);
  });
  var setAutoHideTimer = useEventCallback(function (autoHideDurationParam) {
    if (!onClose || autoHideDurationParam == null) {
      return;
    }
    clearTimeout(timerAutoHide.current);
    timerAutoHide.current = setTimeout(function () {
      handleClose(null, 'timeout');
    }, autoHideDurationParam);
  });
  React.useEffect(function () {
    if (open) {
      setAutoHideTimer(autoHideDuration);
    }
    return function () {
      clearTimeout(timerAutoHide.current);
    };
  }, [open, autoHideDuration, setAutoHideTimer]);
  var handleClickAway = function handleClickAway(event) {
    onClose == null ? void 0 : onClose(event, 'clickaway');
  };

  // Pause the timer when the user is interacting with the Snackbar
  // or when the user hide the window.
  var handlePause = function handlePause() {
    clearTimeout(timerAutoHide.current);
  };

  // Restart the timer when the user is no longer interacting with the Snackbar
  // or when the window is shown back.
  var handleResume = React.useCallback(function () {
    if (autoHideDuration != null) {
      setAutoHideTimer(resumeHideDuration != null ? resumeHideDuration : autoHideDuration * 0.5);
    }
  }, [autoHideDuration, resumeHideDuration, setAutoHideTimer]);
  var createHandleBlur = function createHandleBlur(otherHandlers) {
    return function (event) {
      var onBlurCallback = otherHandlers.onBlur;
      onBlurCallback == null ? void 0 : onBlurCallback(event);
      handleResume();
    };
  };
  var createHandleFocus = function createHandleFocus(otherHandlers) {
    return function (event) {
      var onFocusCallback = otherHandlers.onFocus;
      onFocusCallback == null ? void 0 : onFocusCallback(event);
      handlePause();
    };
  };
  var createMouseEnter = function createMouseEnter(otherHandlers) {
    return function (event) {
      var onMouseEnterCallback = otherHandlers.onMouseEnter;
      onMouseEnterCallback == null ? void 0 : onMouseEnterCallback(event);
      handlePause();
    };
  };
  var createMouseLeave = function createMouseLeave(otherHandlers) {
    return function (event) {
      var onMouseLeaveCallback = otherHandlers.onMouseLeave;
      onMouseLeaveCallback == null ? void 0 : onMouseLeaveCallback(event);
      handleResume();
    };
  };
  React.useEffect(function () {
    // TODO: window global should be refactored here
    if (!disableWindowBlurListener && open) {
      window.addEventListener('focus', handleResume);
      window.addEventListener('blur', handlePause);
      return function () {
        window.removeEventListener('focus', handleResume);
        window.removeEventListener('blur', handlePause);
      };
    }
    return undefined;
  }, [disableWindowBlurListener, handleResume, open]);
  var getRootProps = function getRootProps() {
    var otherHandlers = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
    var propsEventHandlers = extractEventHandlers(parameters);
    var externalEventHandlers = _extends({}, propsEventHandlers, otherHandlers);
    return _extends({
      ref: ref,
      // ClickAwayListener adds an `onClick` prop which results in the alert not being announced.
      // See https://github.com/mui/material-ui/issues/29080
      role: 'presentation'
    }, externalEventHandlers, {
      onBlur: createHandleBlur(externalEventHandlers),
      onFocus: createHandleFocus(externalEventHandlers),
      onMouseEnter: createMouseEnter(externalEventHandlers),
      onMouseLeave: createMouseLeave(externalEventHandlers)
    });
  };
  return {
    getRootProps: getRootProps,
    onClickAway: handleClickAway
  };
}