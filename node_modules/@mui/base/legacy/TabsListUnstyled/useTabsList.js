import _extends from "@babel/runtime/helpers/esm/extends";
import * as React from 'react';
import { unstable_ownerDocument as ownerDocument, unstable_useForkRef as useForkRef } from '@mui/utils';
import { isFragment } from 'react-is';
import { useTabContext } from '../TabsUnstyled';
import extractEventHandlers from '../utils/extractEventHandlers';
var nextItem = function nextItem(list, item) {
  if (!list) {
    return null;
  }
  if (list === item) {
    return list.firstChild;
  }
  if (item && item.nextElementSibling) {
    return item.nextElementSibling;
  }
  return list.firstChild;
};
var previousItem = function previousItem(list, item) {
  if (!list) {
    return null;
  }
  if (list === item) {
    return list.lastChild;
  }
  if (item && item.previousElementSibling) {
    return item.previousElementSibling;
  }
  return list.lastChild;
};
var moveFocus = function moveFocus(list, currentFocus, traversalFunction) {
  var wrappedOnce = false;
  var nextFocus = traversalFunction(list, currentFocus);
  while (list && nextFocus) {
    // Prevent infinite loop.
    if (nextFocus === list.firstChild) {
      if (wrappedOnce) {
        return;
      }
      wrappedOnce = true;
    }

    // Same logic as useAutocomplete.js
    var nextFocusDisabled = nextFocus.disabled || nextFocus.getAttribute('aria-disabled') === 'true';
    if (!nextFocus.hasAttribute('tabindex') || nextFocusDisabled) {
      // Move to the next element.
      nextFocus = traversalFunction(list, nextFocus);
    } else {
      nextFocus.focus();
      return;
    }
  }
};
var useTabsList = function useTabsList(parameters) {
  var ariaLabel = parameters['aria-label'],
    ariaLabelledBy = parameters['aria-labelledby'],
    children = parameters.children,
    ref = parameters.ref;
  var tabsListRef = /*#__PURE__*/React.createRef();
  var handleRef = useForkRef(tabsListRef, ref);
  var context = useTabContext();
  if (context === null) {
    throw new Error('No TabContext provided');
  }
  var value = context.value,
    _context$orientation = context.orientation,
    orientation = _context$orientation === void 0 ? 'horizontal' : _context$orientation,
    _context$direction = context.direction,
    direction = _context$direction === void 0 ? 'ltr' : _context$direction;
  var isRtl = direction === 'rtl';
  var handleKeyDown = function handleKeyDown(event) {
    var list = tabsListRef.current;
    var currentFocus = ownerDocument(list).activeElement;
    // Keyboard navigation assumes that [role="tab"] are siblings
    // though we might warn in the future about nested, interactive elements
    // as a a11y violation
    var role = currentFocus == null ? void 0 : currentFocus.getAttribute('role');
    if (role !== 'tab') {
      return;
    }
    var previousItemKey = orientation === 'horizontal' ? 'ArrowLeft' : 'ArrowUp';
    var nextItemKey = orientation === 'horizontal' ? 'ArrowRight' : 'ArrowDown';
    if (orientation === 'horizontal' && isRtl) {
      // swap previousItemKey with nextItemKey
      previousItemKey = 'ArrowRight';
      nextItemKey = 'ArrowLeft';
    }
    switch (event.key) {
      case previousItemKey:
        event.preventDefault();
        moveFocus(list, currentFocus, previousItem);
        break;
      case nextItemKey:
        event.preventDefault();
        moveFocus(list, currentFocus, nextItem);
        break;
      case 'Home':
        event.preventDefault();
        moveFocus(list, null, nextItem);
        break;
      case 'End':
        event.preventDefault();
        moveFocus(list, null, previousItem);
        break;
      default:
        break;
    }
  };
  var createHandleKeyDown = function createHandleKeyDown(otherHandlers) {
    return function (event) {
      var _otherHandlers$onKeyD;
      handleKeyDown(event);
      (_otherHandlers$onKeyD = otherHandlers.onKeyDown) == null ? void 0 : _otherHandlers$onKeyD.call(otherHandlers, event);
    };
  };
  var getRootProps = function getRootProps() {
    var otherHandlers = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
    var propsEventHandlers = extractEventHandlers(parameters);
    var externalEventHandlers = _extends({}, propsEventHandlers, otherHandlers);
    var ownEventHandlers = {
      onKeyDown: createHandleKeyDown(externalEventHandlers)
    };
    var mergedEventHandlers = _extends({}, externalEventHandlers, ownEventHandlers);
    return _extends({
      'aria-label': ariaLabel,
      'aria-labelledby': ariaLabelledBy,
      'aria-orientation': orientation === 'vertical' ? 'vertical' : undefined,
      role: 'tablist',
      ref: handleRef
    }, mergedEventHandlers);
  };
  var processChildren = React.useCallback(function () {
    var valueToIndex = new Map();
    var childIndex = 0;
    var processedChildren = React.Children.map(children, function (child) {
      if (! /*#__PURE__*/React.isValidElement(child)) {
        return null;
      }
      if (process.env.NODE_ENV !== 'production') {
        if (isFragment(child)) {
          console.error(["MUI: The Tabs component doesn't accept a Fragment as a child.", 'Consider providing an array instead.'].join('\n'));
        }
      }
      var childValue = child.props.value === undefined ? childIndex : child.props.value;
      valueToIndex.set(childValue, childIndex);
      childIndex += 1;
      return /*#__PURE__*/React.cloneElement(child, _extends({
        value: childValue
      }, childIndex === 1 && value === false && !child.props.tabIndex || value === childValue ? {
        tabIndex: 0
      } : {
        tabIndex: -1
      }));
    });
    return processedChildren;
  }, [children, value]);
  return {
    isRtl: isRtl,
    orientation: orientation,
    value: value,
    processChildren: processChildren,
    getRootProps: getRootProps
  };
};
export default useTabsList;