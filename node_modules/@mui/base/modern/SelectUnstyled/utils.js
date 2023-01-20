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
  const selectChildren = [];
  React.Children.forEach(children, node => {
    const nodeChildren = node?.props?.children;
    if (node?.props?.value === undefined) {
      if (nodeChildren != null) {
        const element = node;
        const group = {
          options: getOptionsFromChildren(nodeChildren),
          label: element.props.label,
          disabled: element.props.disabled ?? false
        };
        selectChildren.push(group);
      }
      return;
    }
    const element = node;
    const option = {
      value: element.props.value,
      label: element.props.label || element.props.children,
      disabled: element.props.disabled ?? false
    };
    selectChildren.push(option);
  });
  return selectChildren ?? [];
}
export function flattenOptionGroups(groupedOptions, isGroupDisabled = false) {
  let flatOptions = [];
  groupedOptions.forEach(optionOrGroup => {
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