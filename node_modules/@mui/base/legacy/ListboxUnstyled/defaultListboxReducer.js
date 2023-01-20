import _extends from "@babel/runtime/helpers/esm/extends";
import _toConsumableArray from "@babel/runtime/helpers/esm/toConsumableArray";
import { ActionTypes } from './useListbox.types';
var pageSize = 5;
function findValidOptionToHighlight(index, lookupDirection, options, focusDisabled, isOptionDisabled, wrapAround) {
  if (options.length === 0 || options.every(function (o, i) {
    return isOptionDisabled(o, i);
  })) {
    return -1;
  }
  var nextFocus = index;
  for (;;) {
    // No valid options found
    if (!wrapAround && lookupDirection === 'next' && nextFocus === options.length || !wrapAround && lookupDirection === 'previous' && nextFocus === -1) {
      return -1;
    }
    var nextFocusDisabled = focusDisabled ? false : isOptionDisabled(options[nextFocus], nextFocus);
    if (nextFocusDisabled) {
      nextFocus += lookupDirection === 'next' ? 1 : -1;
      if (wrapAround) {
        nextFocus = (nextFocus + options.length) % options.length;
      }
    } else {
      return nextFocus;
    }
  }
}
function getNewHighlightedOption(options, previouslyHighlightedOption, diff, lookupDirection, highlightDisabled, isOptionDisabled, wrapAround, optionComparer) {
  var _options$nextIndex;
  var maxIndex = options.length - 1;
  var defaultHighlightedIndex = -1;
  var nextIndexCandidate;
  var previouslyHighlightedIndex = previouslyHighlightedOption == null ? -1 : options.findIndex(function (option) {
    return optionComparer(option, previouslyHighlightedOption);
  });
  if (diff === 'reset') {
    var _options$defaultHighl;
    return defaultHighlightedIndex === -1 ? null : (_options$defaultHighl = options[defaultHighlightedIndex]) != null ? _options$defaultHighl : null;
  }
  if (diff === 'start') {
    nextIndexCandidate = 0;
  } else if (diff === 'end') {
    nextIndexCandidate = maxIndex;
  } else {
    var newIndex = previouslyHighlightedIndex + diff;
    if (newIndex < 0) {
      if (!wrapAround && previouslyHighlightedIndex !== -1 || Math.abs(diff) > 1) {
        nextIndexCandidate = 0;
      } else {
        nextIndexCandidate = maxIndex;
      }
    } else if (newIndex > maxIndex) {
      if (!wrapAround || Math.abs(diff) > 1) {
        nextIndexCandidate = maxIndex;
      } else {
        nextIndexCandidate = 0;
      }
    } else {
      nextIndexCandidate = newIndex;
    }
  }
  var nextIndex = findValidOptionToHighlight(nextIndexCandidate, lookupDirection, options, highlightDisabled, isOptionDisabled, wrapAround);
  return (_options$nextIndex = options[nextIndex]) != null ? _options$nextIndex : null;
}
function handleOptionSelection(option, state, props) {
  var multiple = props.multiple,
    _props$optionComparer = props.optionComparer,
    optionComparer = _props$optionComparer === void 0 ? function (o, v) {
      return o === v;
    } : _props$optionComparer,
    _props$isOptionDisabl = props.isOptionDisabled,
    isOptionDisabled = _props$isOptionDisabl === void 0 ? function () {
      return false;
    } : _props$isOptionDisabl;
  var selectedValue = state.selectedValue;
  var optionIndex = props.options.findIndex(function (o) {
    return props.optionComparer(option, o);
  });
  if (isOptionDisabled(option, optionIndex)) {
    return state;
  }
  if (multiple) {
    var _ref, _ref2;
    var selectedValues = (_ref = selectedValue) != null ? _ref : [];
    // if the option is already selected, remove it from the selection, otherwise add it
    var newSelectedValues = selectedValues.some(function (sv) {
      return optionComparer(sv, option);
    }) ? selectedValue.filter(function (v) {
      return !optionComparer(v, option);
    }) : [].concat(_toConsumableArray((_ref2 = selectedValue) != null ? _ref2 : []), [option]);
    return {
      selectedValue: newSelectedValues,
      highlightedValue: option
    };
  }
  if (selectedValue != null && optionComparer(option, selectedValue)) {
    return state;
  }
  return {
    selectedValue: option,
    highlightedValue: option
  };
}
function handleKeyDown(event, state, props) {
  var options = props.options,
    isOptionDisabled = props.isOptionDisabled,
    disableListWrap = props.disableListWrap,
    disabledItemsFocusable = props.disabledItemsFocusable,
    optionComparer = props.optionComparer;
  var moveHighlight = function moveHighlight(diff, direction, wrapAround) {
    return getNewHighlightedOption(options, state.highlightedValue, diff, direction, disabledItemsFocusable != null ? disabledItemsFocusable : false, isOptionDisabled != null ? isOptionDisabled : function () {
      return false;
    }, wrapAround, optionComparer);
  };
  switch (event.key) {
    case 'Home':
      return _extends({}, state, {
        highlightedValue: moveHighlight('start', 'next', false)
      });
    case 'End':
      return _extends({}, state, {
        highlightedValue: moveHighlight('end', 'previous', false)
      });
    case 'PageUp':
      return _extends({}, state, {
        highlightedValue: moveHighlight(-pageSize, 'previous', false)
      });
    case 'PageDown':
      return _extends({}, state, {
        highlightedValue: moveHighlight(pageSize, 'next', false)
      });
    case 'ArrowUp':
      // TODO: extend current selection with Shift modifier
      return _extends({}, state, {
        highlightedValue: moveHighlight(-1, 'previous', !(disableListWrap != null ? disableListWrap : false))
      });
    case 'ArrowDown':
      // TODO: extend current selection with Shift modifier
      return _extends({}, state, {
        highlightedValue: moveHighlight(1, 'next', !(disableListWrap != null ? disableListWrap : false))
      });
    case 'Enter':
    case ' ':
      if (state.highlightedValue === null) {
        return state;
      }
      return handleOptionSelection(state.highlightedValue, state, props);
    default:
      break;
  }
  return state;
}
function handleBlur(state) {
  return _extends({}, state, {
    highlightedValue: null
  });
}
var textCriteriaMatches = function textCriteriaMatches(nextFocus, searchString, stringifyOption) {
  var _stringifyOption;
  var text = (_stringifyOption = stringifyOption(nextFocus)) == null ? void 0 : _stringifyOption.trim().toLowerCase();
  if (!text || text.length === 0) {
    // Make option not navigable if stringification fails or results in empty string.
    return false;
  }
  return text.indexOf(searchString) === 0;
};
function handleTextNavigation(state, searchString, props) {
  var options = props.options,
    isOptionDisabled = props.isOptionDisabled,
    disableListWrap = props.disableListWrap,
    disabledItemsFocusable = props.disabledItemsFocusable,
    optionComparer = props.optionComparer,
    optionStringifier = props.optionStringifier;
  var moveHighlight = function moveHighlight(previouslyHighlightedOption) {
    return getNewHighlightedOption(options, previouslyHighlightedOption, 1, 'next', disabledItemsFocusable != null ? disabledItemsFocusable : false, isOptionDisabled != null ? isOptionDisabled : function () {
      return false;
    }, !(disableListWrap != null ? disableListWrap : false), optionComparer);
  };
  var startWithCurrentOption = searchString.length > 1;
  var nextOption = startWithCurrentOption ? state.highlightedValue : moveHighlight(state.highlightedValue);

  // use `for` instead of `while` prevent infinite loop
  for (var _index = 0; _index < options.length; _index += 1) {
    // Return un-mutated state if looped back to the currently highlighted value
    if (!nextOption || !startWithCurrentOption && state.highlightedValue === nextOption) {
      return state;
    }
    if (textCriteriaMatches(nextOption, searchString, optionStringifier) && (!isOptionDisabled(nextOption, options.indexOf(nextOption)) || disabledItemsFocusable)) {
      // The nextOption is the element to be highlighted
      return _extends({}, state, {
        highlightedValue: nextOption
      });
    }
    // Move to the next element.
    nextOption = moveHighlight(nextOption);
  }

  // No option match text search criteria
  return state;
}
function handleOptionsChange(options, previousOptions, state, props) {
  var _options$find, _options$find2;
  var multiple = props.multiple,
    optionComparer = props.optionComparer;
  var newHighlightedOption = state.highlightedValue == null ? null : (_options$find = options.find(function (option) {
    return optionComparer(option, state.highlightedValue);
  })) != null ? _options$find : null;
  if (multiple) {
    var _ref3;
    // exclude selected values that are no longer in the options
    var selectedValues = (_ref3 = state.selectedValue) != null ? _ref3 : [];
    var newSelectedValues = selectedValues.filter(function (selectedValue) {
      return options.some(function (option) {
        return optionComparer(option, selectedValue);
      });
    });
    return {
      highlightedValue: newHighlightedOption,
      selectedValue: newSelectedValues
    };
  }
  var newSelectedValue = (_options$find2 = options.find(function (option) {
    return optionComparer(option, state.selectedValue);
  })) != null ? _options$find2 : null;
  return {
    highlightedValue: newHighlightedOption,
    selectedValue: newSelectedValue
  };
}
export default function defaultListboxReducer(state, action) {
  var type = action.type;
  switch (type) {
    case ActionTypes.keyDown:
      return handleKeyDown(action.event, state, action.props);
    case ActionTypes.optionClick:
      return handleOptionSelection(action.option, state, action.props);
    case ActionTypes.blur:
      return handleBlur(state);
    case ActionTypes.setValue:
      return _extends({}, state, {
        selectedValue: action.value
      });
    case ActionTypes.setHighlight:
      return _extends({}, state, {
        highlightedValue: action.highlight
      });
    case ActionTypes.textNavigation:
      return handleTextNavigation(state, action.searchString, action.props);
    case ActionTypes.optionsChange:
      return handleOptionsChange(action.options, action.previousOptions, state, action.props);
    default:
      return state;
  }
}