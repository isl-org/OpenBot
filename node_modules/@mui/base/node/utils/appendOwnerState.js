"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = appendOwnerState;
var _extends2 = _interopRequireDefault(require("@babel/runtime/helpers/extends"));
var _isHostComponent = _interopRequireDefault(require("./isHostComponent"));
/**
 * Appends the ownerState object to the props, merging with the existing one if necessary.
 *
 * @param elementType Type of the element that owns the `existingProps`. If the element is a DOM node or undefined, `ownerState` is not applied.
 * @param otherProps Props of the element.
 * @param ownerState
 */
function appendOwnerState(elementType, otherProps, ownerState) {
  if (elementType === undefined || (0, _isHostComponent.default)(elementType)) {
    return otherProps;
  }
  return (0, _extends2.default)({}, otherProps, {
    ownerState: (0, _extends2.default)({}, otherProps.ownerState, ownerState)
  });
}