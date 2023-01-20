import _extends from "@babel/runtime/helpers/esm/extends";
import * as React from 'react';
import { unstable_useForkRef as useForkRef, unstable_useId as useId } from '@mui/utils';
import { ActionTypes } from './useListbox.types';
import defaultReducer from './defaultListboxReducer';
import useControllableReducer from './useControllableReducer';
import areArraysEqual from '../utils/areArraysEqual';
const TEXT_NAVIGATION_RESET_TIMEOUT = 500; // milliseconds

const defaultOptionComparer = (optionA, optionB) => optionA === optionB;
const defaultIsOptionDisabled = () => false;
const defaultOptionStringifier = option => typeof option === 'string' ? option : String(option);
export default function useListbox(props) {
  const {
    disabledItemsFocusable = false,
    disableListWrap = false,
    focusManagement = 'activeDescendant',
    id: idProp,
    isOptionDisabled = defaultIsOptionDisabled,
    listboxRef: externalListboxRef,
    multiple = false,
    optionComparer = defaultOptionComparer,
    optionStringifier = defaultOptionStringifier,
    options,
    stateReducer: externalReducer
  } = props;
  const id = useId(idProp);
  function defaultIdGenerator(_, index) {
    return `${id}-option-${index}`;
  }
  const optionIdGenerator = props.optionIdGenerator ?? defaultIdGenerator;
  const propsWithDefaults = _extends({}, props, {
    disabledItemsFocusable,
    disableListWrap,
    focusManagement,
    isOptionDisabled,
    multiple,
    optionComparer,
    optionStringifier
  });
  const listboxRef = React.useRef(null);
  const handleRef = useForkRef(externalListboxRef, listboxRef);
  const textCriteriaRef = React.useRef({
    searchString: '',
    lastTime: null
  });
  const [{
    highlightedValue,
    selectedValue
  }, dispatch] = useControllableReducer(defaultReducer, externalReducer, propsWithDefaults);
  const highlightedIndex = React.useMemo(() => {
    return highlightedValue == null ? -1 : options.findIndex(option => optionComparer(option, highlightedValue));
  }, [highlightedValue, options, optionComparer]);
  const previousOptions = React.useRef([]);
  React.useEffect(() => {
    if (areArraysEqual(previousOptions.current, options, optionComparer)) {
      return;
    }
    dispatch({
      type: ActionTypes.optionsChange,
      event: null,
      options,
      previousOptions: previousOptions.current,
      props: propsWithDefaults
    });
    previousOptions.current = options;

    // No need to re-run this effect if props change
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [options, optionComparer, dispatch]);
  const setSelectedValue = React.useCallback(option => {
    dispatch({
      type: ActionTypes.setValue,
      event: null,
      value: option
    });
  }, [dispatch]);
  const setHighlightedValue = React.useCallback(option => {
    dispatch({
      type: ActionTypes.setHighlight,
      event: null,
      highlight: option
    });
  }, [dispatch]);
  const createHandleOptionClick = (option, other) => event => {
    other.onClick?.(event);
    if (event.defaultPrevented) {
      return;
    }
    event.preventDefault();
    dispatch({
      type: ActionTypes.optionClick,
      option,
      event,
      props: propsWithDefaults
    });
  };
  const createHandleOptionPointerOver = (option, other) => event => {
    other.onMouseOver?.(event);
    if (event.defaultPrevented) {
      return;
    }
    dispatch({
      type: ActionTypes.optionHover,
      option,
      event,
      props: propsWithDefaults
    });
  };
  const createHandleKeyDown = other => event => {
    other.onKeyDown?.(event);
    if (event.defaultPrevented) {
      return;
    }
    const keysToPreventDefault = ['ArrowUp', 'ArrowDown', 'Home', 'End', 'PageUp', 'PageDown'];
    if (focusManagement === 'activeDescendant') {
      // When the child element is focused using the activeDescendant attribute,
      // the listbox handles keyboard events on its behalf.
      // We have to `preventDefault()` is this case to prevent the browser from
      // scrolling the view when space is pressed or submitting forms when enter is pressed.
      keysToPreventDefault.push(' ', 'Enter');
    }
    if (keysToPreventDefault.includes(event.key)) {
      event.preventDefault();
    }
    dispatch({
      type: ActionTypes.keyDown,
      event,
      props: propsWithDefaults
    });

    // Handle text navigation
    if (event.key.length === 1 && event.key !== ' ') {
      const textCriteria = textCriteriaRef.current;
      const lowerKey = event.key.toLowerCase();
      const currentTime = performance.now();
      if (textCriteria.searchString.length > 0 && textCriteria.lastTime && currentTime - textCriteria.lastTime > TEXT_NAVIGATION_RESET_TIMEOUT) {
        textCriteria.searchString = lowerKey;
      } else if (textCriteria.searchString.length !== 1 || lowerKey !== textCriteria.searchString) {
        // If there is just one character in the buffer and the key is the same, do not append
        textCriteria.searchString += lowerKey;
      }
      textCriteria.lastTime = currentTime;
      dispatch({
        type: ActionTypes.textNavigation,
        event,
        searchString: textCriteria.searchString,
        props: propsWithDefaults
      });
    }
  };
  const createHandleBlur = other => event => {
    other.onBlur?.(event);
    if (event.defaultPrevented) {
      return;
    }
    if (listboxRef.current?.contains(document.activeElement)) {
      // focus is within the listbox
      return;
    }
    dispatch({
      type: ActionTypes.blur,
      event,
      props: propsWithDefaults
    });
  };
  const getRootProps = (otherHandlers = {}) => {
    return _extends({}, otherHandlers, {
      'aria-activedescendant': focusManagement === 'activeDescendant' && highlightedValue != null ? optionIdGenerator(highlightedValue, highlightedIndex) : undefined,
      id,
      onBlur: createHandleBlur(otherHandlers),
      onKeyDown: createHandleKeyDown(otherHandlers),
      role: 'listbox',
      tabIndex: focusManagement === 'DOM' ? -1 : 0,
      ref: handleRef
    });
  };
  const getOptionState = option => {
    let selected;
    const index = options.findIndex(opt => optionComparer(opt, option));
    if (multiple) {
      selected = (selectedValue ?? []).some(value => value != null && optionComparer(option, value));
    } else {
      selected = optionComparer(option, selectedValue);
    }
    const disabled = isOptionDisabled(option, index);
    return {
      selected,
      disabled,
      highlighted: highlightedIndex === index
    };
  };
  const getOptionTabIndex = optionState => {
    if (focusManagement === 'activeDescendant') {
      return undefined;
    }
    if (!optionState.highlighted) {
      return -1;
    }
    if (optionState.disabled && !disabledItemsFocusable) {
      return -1;
    }
    return 0;
  };
  const getOptionProps = (option, otherHandlers = {}) => {
    const optionState = getOptionState(option);
    const index = options.findIndex(opt => optionComparer(opt, option));
    return _extends({}, otherHandlers, {
      'aria-disabled': optionState.disabled || undefined,
      'aria-selected': optionState.selected,
      id: optionIdGenerator(option, index),
      onClick: createHandleOptionClick(option, otherHandlers),
      onPointerOver: createHandleOptionPointerOver(option, otherHandlers),
      role: 'option',
      tabIndex: getOptionTabIndex(optionState)
    });
  };
  React.useDebugValue({
    highlightedOption: options[highlightedIndex],
    selectedOption: selectedValue
  });
  return {
    getRootProps,
    getOptionProps,
    getOptionState,
    highlightedOption: options[highlightedIndex] ?? null,
    selectedOption: selectedValue,
    setSelectedValue,
    setHighlightedValue
  };
}