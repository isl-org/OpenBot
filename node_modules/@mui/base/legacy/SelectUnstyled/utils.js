import _extends from "@babel/runtime/helpers/esm/extends";
import * as React from 'react';
import { isOptionGroup } from './useSelect.types';
export function areOptionsEqual(option1, option2) {
  return option1.label === option2.label && option1.value === option2.value && option1.disabled === option2.disabled;
}
export function getOptionsFromChildren(children) {
  if (children == null) {
    return [];
  }
  var selectChildren = [];
  React.Children.forEach(children, function (node) {
    var _props, _props2, _element$props$disabl2;
    var nodeChildren = node == null ? void 0 : (_props = node.props) == null ? void 0 : _props.children;
    if ((node == null ? void 0 : (_props2 = node.props) == null ? void 0 : _props2.value) === undefined) {
      if (nodeChildren != null) {
        var _element$props$disabl;
        var _element = node;
        var group = {
          options: getOptionsFromChildren(nodeChildren),
          label: _element.props.label,
          disabled: (_element$props$disabl = _element.props.disabled) != null ? _element$props$disabl : false
        };
        selectChildren.push(group);
      }
      return;
    }
    var element = node;
    var option = {
      value: element.props.value,
      label: element.props.label || element.props.children,
      disabled: (_element$props$disabl2 = element.props.disabled) != null ? _element$props$disabl2 : false
    };
    selectChildren.push(option);
  });
  return selectChildren != null ? selectChildren : [];
}
export function flattenOptionGroups(groupedOptions) {
  var isGroupDisabled = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : false;
  var flatOptions = [];
  groupedOptions.forEach(function (optionOrGroup) {
    if (isOptionGroup(optionOrGroup)) {
      flatOptions = flatOptions.concat(flattenOptionGroups(optionOrGroup.options, optionOrGroup.disabled));
    } else {
      flatOptions.push(_extends({}, optionOrGroup, {
        disabled: isGroupDisabled || optionOrGroup.disabled
      }));
    }
  });
  return flatOptions;
}