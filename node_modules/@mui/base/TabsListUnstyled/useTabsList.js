import _extends from "@babel/runtime/helpers/esm/extends";
import * as React from 'react';
import { unstable_ownerDocument as ownerDocument, unstable_useForkRef as useForkRef } from '@mui/utils';
import { isFragment } from 'react-is';
import { useTabContext } from '../TabsUnstyled';
import extractEventHandlers from '../utils/extractEventHandlers';
const nextItem = (list, item) => {
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
const previousItem = (list, item) => {
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
const moveFocus = (list, currentFocus, traversalFunction) => {
  let wrappedOnce = false;
  let nextFocus = traversalFunction(list, currentFocus);
  while (list && nextFocus) {
    // Prevent infinite loop.
    if (nextFocus === list.firstChild) {
      if (wrappedOnce) {
        return;
      }
      wrappedOnce = true;
    }

    // Same logic as useAutocomplete.js
    const nextFocusDisabled = nextFocus.disabled || nextFocus.getAttribute('aria-disabled') === 'true';
    if (!nextFocus.hasAttribute('tabindex') || nextFocusDisabled) {
      // Move to the next element.
      nextFocus = traversalFunction(list, nextFocus);
    } else {
      nextFocus.focus();
      return;
    }
  }
};
const useTabsList = parameters => {
  const {
    'aria-label': ariaLabel,
    'aria-labelledby': ariaLabelledBy,
    children,
    ref
  } = parameters;
  const tabsListRef = /*#__PURE__*/React.createRef();
  const handleRef = useForkRef(tabsListRef, ref);
  const context = useTabContext();
  if (context === null) {
    throw new Error('No TabContext provided');
  }
  const {
    value,
    orientation = 'horizontal',
    direction = 'ltr'
  } = context;
  const isRtl = direction === 'rtl';
  const handleKeyDown = event => {
    const list = tabsListRef.current;
    const currentFocus = ownerDocument(list).activeElement;
    // Keyboard navigation assumes that [role="tab"] are siblings
    // though we might warn in the future about nested, interactive elements
    // as a a11y violation
    const role = currentFocus == null ? void 0 : currentFocus.getAttribute('role');
    if (role !== 'tab') {
      return;
    }
    let previousItemKey = orientation === 'horizontal' ? 'ArrowLeft' : 'ArrowUp';
    let nextItemKey = orientation === 'horizontal' ? 'ArrowRight' : 'ArrowDown';
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
  const createHandleKeyDown = otherHandlers => event => {
    var _otherHandlers$onKeyD;
    handleKeyDown(event);
    (_otherHandlers$onKeyD = otherHandlers.onKeyDown) == null ? void 0 : _otherHandlers$onKeyD.call(otherHandlers, event);
  };
  const getRootProps = (otherHandlers = {}) => {
    const propsEventHandlers = extractEventHandlers(parameters);
    const externalEventHandlers = _extends({}, propsEventHandlers, otherHandlers);
    const ownEventHandlers = {
      onKeyDown: createHandleKeyDown(externalEventHandlers)
    };
    const mergedEventHandlers = _extends({}, externalEventHandlers, ownEventHandlers);
    return _extends({
      'aria-label': ariaLabel,
      'aria-labelledby': ariaLabelledBy,
      'aria-orientation': orientation === 'vertical' ? 'vertical' : undefined,
      role: 'tablist',
      ref: handleRef
    }, mergedEventHandlers);
  };
  const processChildren = React.useCallback(() => {
    const valueToIndex = new Map();
    let childIndex = 0;
    const processedChildren = React.Children.map(children, child => {
      if (! /*#__PURE__*/React.isValidElement(child)) {
        return null;
      }
      if (process.env.NODE_ENV !== 'production') {
        if (isFragment(child)) {
          console.error(["MUI: The Tabs component doesn't accept a Fragment as a child.", 'Consider providing an array instead.'].join('\n'));
        }
      }
      const childValue = child.props.value === undefined ? childIndex : child.props.value;
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
    isRtl,
    orientation,
    value,
    processChildren,
    getRootProps
  };
};
export default useTabsList;