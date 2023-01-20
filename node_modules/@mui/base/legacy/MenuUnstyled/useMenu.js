import _extends from "@babel/runtime/helpers/esm/extends";
import * as React from 'react';
import { unstable_useForkRef as useForkRef } from '@mui/utils';
import { defaultListboxReducer, useListbox, ActionTypes } from '../ListboxUnstyled';
function stateReducer(state, action) {
  if (action.type === ActionTypes.blur || action.type === ActionTypes.optionHover || action.type === ActionTypes.setValue) {
    return state;
  }
  var newState = defaultListboxReducer(state, action);
  if (action.type !== ActionTypes.setHighlight && newState.highlightedValue === null && action.props.options.length > 0) {
    return _extends({}, newState, {
      highlightedValue: action.props.options[0]
    });
  }
  return newState;
}
export default function useMenu() {
  var parameters = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
  var listboxRefProp = parameters.listboxRef,
    _parameters$open = parameters.open,
    open = _parameters$open === void 0 ? false : _parameters$open,
    onClose = parameters.onClose,
    listboxId = parameters.listboxId;
  var _React$useState = React.useState({}),
    menuItems = _React$useState[0],
    setMenuItems = _React$useState[1];
  var listboxRef = React.useRef(null);
  var handleRef = useForkRef(listboxRef, listboxRefProp);
  var registerItem = React.useCallback(function (id, metadata) {
    setMenuItems(function (previousState) {
      var newState = _extends({}, previousState);
      newState[id] = metadata;
      return newState;
    });
  }, []);
  var unregisterItem = React.useCallback(function (id) {
    setMenuItems(function (previousState) {
      var newState = _extends({}, previousState);
      delete newState[id];
      return newState;
    });
  }, []);
  var _useListbox = useListbox({
      options: Object.keys(menuItems),
      optionStringifier: function optionStringifier(id) {
        var _menuItems$id$ref$cur;
        return menuItems[id].label || ((_menuItems$id$ref$cur = menuItems[id].ref.current) == null ? void 0 : _menuItems$id$ref$cur.innerText);
      },
      isOptionDisabled: function isOptionDisabled(id) {
        var _menuItems$id;
        return (menuItems == null ? void 0 : (_menuItems$id = menuItems[id]) == null ? void 0 : _menuItems$id.disabled) || false;
      },
      listboxRef: handleRef,
      focusManagement: 'DOM',
      id: listboxId,
      stateReducer: stateReducer,
      disabledItemsFocusable: true
    }),
    getOptionState = _useListbox.getOptionState,
    getOptionProps = _useListbox.getOptionProps,
    getRootProps = _useListbox.getRootProps,
    highlightedOption = _useListbox.highlightedOption,
    setListboxHighlight = _useListbox.setHighlightedValue;
  var highlightFirstItem = React.useCallback(function () {
    if (Object.keys(menuItems).length > 0) {
      setListboxHighlight(menuItems[Object.keys(menuItems)[0]].id);
    }
  }, [menuItems, setListboxHighlight]);
  var highlightLastItem = React.useCallback(function () {
    if (Object.keys(menuItems).length > 0) {
      setListboxHighlight(menuItems[Object.keys(menuItems)[Object.keys(menuItems).length - 1]].id);
    }
  }, [menuItems, setListboxHighlight]);
  React.useEffect(function () {
    if (!open) {
      highlightFirstItem();
    }
  }, [open, highlightFirstItem]);
  var createHandleKeyDown = function createHandleKeyDown(otherHandlers) {
    return function (e) {
      var _otherHandlers$onKeyD;
      (_otherHandlers$onKeyD = otherHandlers.onKeyDown) == null ? void 0 : _otherHandlers$onKeyD.call(otherHandlers, e);
      if (e.defaultPrevented) {
        return;
      }
      if (e.key === 'Escape' && open) {
        onClose == null ? void 0 : onClose();
      }
    };
  };
  var createHandleBlur = function createHandleBlur(otherHandlers) {
    return function (e) {
      var _otherHandlers$onBlur, _listboxRef$current;
      (_otherHandlers$onBlur = otherHandlers.onBlur) == null ? void 0 : _otherHandlers$onBlur.call(otherHandlers, e);
      if (!((_listboxRef$current = listboxRef.current) != null && _listboxRef$current.contains(e.relatedTarget))) {
        onClose == null ? void 0 : onClose();
      }
    };
  };
  React.useEffect(function () {
    var _listboxRef$current2;
    // set focus to the highlighted item (but prevent stealing focus from other elements on the page)
    if ((_listboxRef$current2 = listboxRef.current) != null && _listboxRef$current2.contains(document.activeElement) && highlightedOption !== null) {
      var _menuItems$highlighte, _menuItems$highlighte2;
      menuItems == null ? void 0 : (_menuItems$highlighte = menuItems[highlightedOption]) == null ? void 0 : (_menuItems$highlighte2 = _menuItems$highlighte.ref.current) == null ? void 0 : _menuItems$highlighte2.focus();
    }
  }, [highlightedOption, menuItems]);
  var getListboxProps = function getListboxProps() {
    var otherHandlers = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
    var rootProps = getRootProps(_extends({}, otherHandlers, {
      onBlur: createHandleBlur(otherHandlers),
      onKeyDown: createHandleKeyDown(otherHandlers)
    }));
    return _extends({}, otherHandlers, rootProps, {
      role: 'menu'
    });
  };
  var getItemState = function getItemState(id) {
    var _getOptionState = getOptionState(id),
      disabled = _getOptionState.disabled,
      highlighted = _getOptionState.highlighted;
    return {
      disabled: disabled,
      highlighted: highlighted
    };
  };
  React.useDebugValue({
    menuItems: menuItems,
    highlightedOption: highlightedOption
  });
  return {
    registerItem: registerItem,
    unregisterItem: unregisterItem,
    menuItems: menuItems,
    getListboxProps: getListboxProps,
    getItemState: getItemState,
    getItemProps: getOptionProps,
    highlightedOption: highlightedOption,
    highlightFirstItem: highlightFirstItem,
    highlightLastItem: highlightLastItem
  };
}