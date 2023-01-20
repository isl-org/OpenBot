"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = defaultListboxReducer;
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var _useListbox = require("./useListbox.types");
const pageSize = 5;
function findValidOptionToHighlight(index, lookupDirection, options, focusDisabled, isOptionDisabled, wrapAround) {
  if (options.length === 0 || options.every((o, i) => isOptionDisabled(o, i))) {
    return -1;
  }
  let nextFocus = index;
  for (;;) {
    // No valid options found
    if (!wrapAround && lookupDirection === 'next' && nextFocus === options.length || !wrapAround && lookupDirection === 'previous' && nextFocus === -1) {
      return -1;
    }
    const nextFocusDisabled = focusDisabled ? false : isOptionDisabled(options[nextFocus], nextFocus);
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
  const maxIndex = options.length - 1;
  const defaultHighlightedIndex = -1;
  let nextIndexCandidate;
  const previouslyHighlightedIndex = previouslyHighlightedOption == null ? -1 : options.findIndex(option => optionComparer(option, previouslyHighlightedOption));
  if (diff === 'reset') {
    var _options$defaultHighl;
    return defaultHighlightedIndex === -1 ? null : (_options$defaultHighl = options[defaultHighlightedIndex]) != null ? _options$defaultHighl : null;
  }
  if (diff === 'start') {
    nextIndexCandidate = 0;
  } else if (diff === 'end') {
    nextIndexCandidate = maxIndex;
  } else {
    const newIndex = previouslyHighlightedIndex + diff;
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
  const nextIndex = findValidOptionToHighlight(nextIndexCandidate, lookupDirection, options, highlightDisabled, isOptionDisabled, wrapAround);
  return (_options$nextIndex = options[nextIndex]) != null ? _options$nextIndex : null;
}
function handleOptionSelection(option, state, props) {
  const {
    multiple,
    optionComparer = (o, v) => o === v,
    isOptionDisabled = () => false
  } = props;
  const {
    selectedValue
  } = state;
  const optionIndex = props.options.findIndex(o => props.optionComparer(option, o));
  if (isOptionDisabled(option, optionIndex)) {
    return state;
  }
  if (multiple) {
    var _ref, _ref2;
    const selectedValues = (_ref = selectedValue) != null ? _ref : [];
    // if the option is already selected, remove it from the selection, otherwise add it
    const newSelectedValues = selectedValues.some(sv => optionComparer(sv, option)) ? selectedValue.filter(v => !optionComparer(v, option)) : [...((_ref2 = selectedValue) != null ? _ref2 : []), option];
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
  const {
    options,
    isOptionDisabled,
    disableListWrap,
    disabledItemsFocusable,
    optionComparer
  } = props;
  const moveHighlight = (diff, direction, wrapAround) => {
    return getNewHighlightedOption(options, state.highlightedValue, diff, direction, disabledItemsFocusable != null ? disabledItemsFocusable : false, isOptionDisabled != null ? isOptionDisabled : () => false, wrapAround, optionComparer);
  };
  switch (event.key) {
    case 'Home':
      return (0, _extends2.default)({}, state, {
        highlightedValue: moveHighlight('start', 'next', false)
      });
    case 'End':
      return (0, _extends2.default)({}, state, {
        highlightedValue: moveHighlight('end', 'previous', false)
      });
    case 'PageUp':
      return (0, _extends2.default)({}, state, {
        highlightedValue: moveHighlight(-pageSize, 'previous', false)
      });
    case 'PageDown':
      return (0, _extends2.default)({}, state, {
        highlightedValue: moveHighlight(pageSize, 'next', false)
      });
    case 'ArrowUp':
      // TODO: extend current selection with Shift modifier
      return (0, _extends2.default)({}, state, {
        highlightedValue: moveHighlight(-1, 'previous', !(disableListWrap != null ? disableListWrap : false))
      });
    case 'ArrowDown':
      // TODO: extend current selection with Shift modifier
      return (0, _extends2.default)({}, state, {
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
  return (0, _extends2.default)({}, state, {
    highlightedValue: null
  });
}
const textCriteriaMatches = (nextFocus, searchString, stringifyOption) => {
  var _stringifyOption;
  const text = (_stringifyOption = stringifyOption(nextFocus)) == null ? void 0 : _stringifyOption.trim().toLowerCase();
  if (!text || text.length === 0) {
    // Make option not navigable if stringification fails or results in empty string.
    return false;
  }
  return text.indexOf(searchString) === 0;
};
function handleTextNavigation(state, searchString, props) {
  const {
    options,
    isOptionDisabled,
    disableListWrap,
    disabledItemsFocusable,
    optionComparer,
    optionStringifier
  } = props;
  const moveHighlight = previouslyHighlightedOption => {
    return getNewHighlightedOption(options, previouslyHighlightedOption, 1, 'next', disabledItemsFocusable != null ? disabledItemsFocusable : false, isOptionDisabled != null ? isOptionDisabled : () => false, !(disableListWrap != null ? disableListWrap : false), optionComparer);
  };
  const startWithCurrentOption = searchString.length > 1;
  let nextOption = startWithCurrentOption ? state.highlightedValue : moveHighlight(state.highlightedValue);

  // use `for` instead of `while` prevent infinite loop
  for (let index = 0; index < options.length; index += 1) {
    // Return un-mutated state if looped back to the currently highlighted value
    if (!nextOption || !startWithCurrentOption && state.highlightedValue === nextOption) {
      return state;
    }
    if (textCriteriaMatches(nextOption, searchString, optionStringifier) && (!isOptionDisabled(nextOption, options.indexOf(nextOption)) || disabledItemsFocusable)) {
      // The nextOption is the element to be highlighted
      return (0, _extends2.default)({}, state, {
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
  const {
    multiple,
    optionComparer
  } = props;
  const newHighlightedOption = state.highlightedValue == null ? null : (_options$find = options.find(option => optionComparer(option, state.highlightedValue))) != null ? _options$find : null;
  if (multiple) {
    var _ref3;
    // exclude selected values that are no longer in the options
    const selectedValues = (_ref3 = state.selectedValue) != null ? _ref3 : [];
    const newSelectedValues = selectedValues.filter(selectedValue => options.some(option => optionComparer(option, selectedValue)));
    return {
      highlightedValue: newHighlightedOption,
      selectedValue: newSelectedValues
    };
  }
  const newSelectedValue = (_options$find2 = options.find(option => optionComparer(option, state.selectedValue))) != null ? _options$find2 : null;
  return {
    highlightedValue: newHighlightedOption,
    selectedValue: newSelectedValue
  };
}
function defaultListboxReducer(state, action) {
  const {
    type
  } = action;
  switch (type) {
    case _useListbox.ActionTypes.keyDown:
      return handleKeyDown(action.event, state, action.props);
    case _useListbox.ActionTypes.optionClick:
      return handleOptionSelection(action.option, state, action.props);
    case _useListbox.ActionTypes.blur:
      return handleBlur(state);
    case _useListbox.ActionTypes.setValue:
      return (0, _extends2.default)({}, state, {
        selectedValue: action.value
      });
    case _useListbox.ActionTypes.setHighlight:
      return (0, _extends2.default)({}, state, {
        highlightedValue: action.highlight
      });
    case _useListbox.ActionTypes.textNavigation:
      return handleTextNavigation(state, action.searchString, action.props);
    case _useListbox.ActionTypes.optionsChange:
      return handleOptionsChange(action.options, action.previousOptions, state, action.props);
    default:
      return state;
  }
}