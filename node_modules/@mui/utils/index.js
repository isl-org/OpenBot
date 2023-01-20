/** @license MUI v5.11.2
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
var _exportNames = {
  chainPropTypes: true,
  deepmerge: true,
  isPlainObject: true,
  elementAcceptingRef: true,
  elementTypeAcceptingRef: true,
  exactProp: true,
  formatMuiErrorMessage: true,
  getDisplayName: true,
  HTMLElementType: true,
  ponyfillGlobal: true,
  refType: true,
  unstable_capitalize: true,
  unstable_createChainedFunction: true,
  unstable_debounce: true,
  unstable_deprecatedPropType: true,
  unstable_isMuiElement: true,
  unstable_ownerDocument: true,
  unstable_ownerWindow: true,
  unstable_requirePropFactory: true,
  unstable_setRef: true,
  unstable_useEnhancedEffect: true,
  unstable_useId: true,
  unstable_unsupportedProp: true,
  unstable_useControlled: true,
  unstable_useEventCallback: true,
  unstable_useForkRef: true,
  unstable_useIsFocusVisible: true,
  unstable_getScrollbarSize: true,
  unstable_detectScrollType: true,
  unstable_getNormalizedScrollLeft: true,
  usePreviousProps: true,
  visuallyHidden: true,
  integerPropType: true,
  internal_resolveProps: true,
  unstable_composeClasses: true,
  unstable_generateUtilityClass: true,
  unstable_generateUtilityClasses: true,
  unstable_ClassNameGenerator: true
};
Object.defineProperty(exports, "HTMLElementType", {
  enumerable: true,
  get: function () {
    return _HTMLElementType.default;
  }
});
Object.defineProperty(exports, "chainPropTypes", {
  enumerable: true,
  get: function () {
    return _chainPropTypes.default;
  }
});
Object.defineProperty(exports, "deepmerge", {
  enumerable: true,
  get: function () {
    return _deepmerge.default;
  }
});
Object.defineProperty(exports, "elementAcceptingRef", {
  enumerable: true,
  get: function () {
    return _elementAcceptingRef.default;
  }
});
Object.defineProperty(exports, "elementTypeAcceptingRef", {
  enumerable: true,
  get: function () {
    return _elementTypeAcceptingRef.default;
  }
});
Object.defineProperty(exports, "exactProp", {
  enumerable: true,
  get: function () {
    return _exactProp.default;
  }
});
Object.defineProperty(exports, "formatMuiErrorMessage", {
  enumerable: true,
  get: function () {
    return _formatMuiErrorMessage.default;
  }
});
Object.defineProperty(exports, "getDisplayName", {
  enumerable: true,
  get: function () {
    return _getDisplayName.default;
  }
});
Object.defineProperty(exports, "integerPropType", {
  enumerable: true,
  get: function () {
    return _integerPropType.default;
  }
});
Object.defineProperty(exports, "internal_resolveProps", {
  enumerable: true,
  get: function () {
    return _resolveProps.default;
  }
});
Object.defineProperty(exports, "isPlainObject", {
  enumerable: true,
  get: function () {
    return _deepmerge.isPlainObject;
  }
});
Object.defineProperty(exports, "ponyfillGlobal", {
  enumerable: true,
  get: function () {
    return _ponyfillGlobal.default;
  }
});
Object.defineProperty(exports, "refType", {
  enumerable: true,
  get: function () {
    return _refType.default;
  }
});
Object.defineProperty(exports, "unstable_ClassNameGenerator", {
  enumerable: true,
  get: function () {
    return _ClassNameGenerator.default;
  }
});
Object.defineProperty(exports, "unstable_capitalize", {
  enumerable: true,
  get: function () {
    return _capitalize.default;
  }
});
Object.defineProperty(exports, "unstable_composeClasses", {
  enumerable: true,
  get: function () {
    return _composeClasses.default;
  }
});
Object.defineProperty(exports, "unstable_createChainedFunction", {
  enumerable: true,
  get: function () {
    return _createChainedFunction.default;
  }
});
Object.defineProperty(exports, "unstable_debounce", {
  enumerable: true,
  get: function () {
    return _debounce.default;
  }
});
Object.defineProperty(exports, "unstable_deprecatedPropType", {
  enumerable: true,
  get: function () {
    return _deprecatedPropType.default;
  }
});
Object.defineProperty(exports, "unstable_detectScrollType", {
  enumerable: true,
  get: function () {
    return _scrollLeft.detectScrollType;
  }
});
Object.defineProperty(exports, "unstable_generateUtilityClass", {
  enumerable: true,
  get: function () {
    return _generateUtilityClass.default;
  }
});
Object.defineProperty(exports, "unstable_generateUtilityClasses", {
  enumerable: true,
  get: function () {
    return _generateUtilityClasses.default;
  }
});
Object.defineProperty(exports, "unstable_getNormalizedScrollLeft", {
  enumerable: true,
  get: function () {
    return _scrollLeft.getNormalizedScrollLeft;
  }
});
Object.defineProperty(exports, "unstable_getScrollbarSize", {
  enumerable: true,
  get: function () {
    return _getScrollbarSize.default;
  }
});
Object.defineProperty(exports, "unstable_isMuiElement", {
  enumerable: true,
  get: function () {
    return _isMuiElement.default;
  }
});
Object.defineProperty(exports, "unstable_ownerDocument", {
  enumerable: true,
  get: function () {
    return _ownerDocument.default;
  }
});
Object.defineProperty(exports, "unstable_ownerWindow", {
  enumerable: true,
  get: function () {
    return _ownerWindow.default;
  }
});
Object.defineProperty(exports, "unstable_requirePropFactory", {
  enumerable: true,
  get: function () {
    return _requirePropFactory.default;
  }
});
Object.defineProperty(exports, "unstable_setRef", {
  enumerable: true,
  get: function () {
    return _setRef.default;
  }
});
Object.defineProperty(exports, "unstable_unsupportedProp", {
  enumerable: true,
  get: function () {
    return _unsupportedProp.default;
  }
});
Object.defineProperty(exports, "unstable_useControlled", {
  enumerable: true,
  get: function () {
    return _useControlled.default;
  }
});
Object.defineProperty(exports, "unstable_useEnhancedEffect", {
  enumerable: true,
  get: function () {
    return _useEnhancedEffect.default;
  }
});
Object.defineProperty(exports, "unstable_useEventCallback", {
  enumerable: true,
  get: function () {
    return _useEventCallback.default;
  }
});
Object.defineProperty(exports, "unstable_useForkRef", {
  enumerable: true,
  get: function () {
    return _useForkRef.default;
  }
});
Object.defineProperty(exports, "unstable_useId", {
  enumerable: true,
  get: function () {
    return _useId.default;
  }
});
Object.defineProperty(exports, "unstable_useIsFocusVisible", {
  enumerable: true,
  get: function () {
    return _useIsFocusVisible.default;
  }
});
Object.defineProperty(exports, "usePreviousProps", {
  enumerable: true,
  get: function () {
    return _usePreviousProps.default;
  }
});
Object.defineProperty(exports, "visuallyHidden", {
  enumerable: true,
  get: function () {
    return _visuallyHidden.default;
  }
});
var _chainPropTypes = _interopRequireDefault(require("./chainPropTypes"));
var _deepmerge = _interopRequireWildcard(require("./deepmerge"));
var _elementAcceptingRef = _interopRequireDefault(require("./elementAcceptingRef"));
var _elementTypeAcceptingRef = _interopRequireDefault(require("./elementTypeAcceptingRef"));
var _exactProp = _interopRequireDefault(require("./exactProp"));
var _formatMuiErrorMessage = _interopRequireDefault(require("./formatMuiErrorMessage"));
var _getDisplayName = _interopRequireDefault(require("./getDisplayName"));
var _HTMLElementType = _interopRequireDefault(require("./HTMLElementType"));
var _ponyfillGlobal = _interopRequireDefault(require("./ponyfillGlobal"));
var _refType = _interopRequireDefault(require("./refType"));
var _capitalize = _interopRequireDefault(require("./capitalize"));
var _createChainedFunction = _interopRequireDefault(require("./createChainedFunction"));
var _debounce = _interopRequireDefault(require("./debounce"));
var _deprecatedPropType = _interopRequireDefault(require("./deprecatedPropType"));
var _isMuiElement = _interopRequireDefault(require("./isMuiElement"));
var _ownerDocument = _interopRequireDefault(require("./ownerDocument"));
var _ownerWindow = _interopRequireDefault(require("./ownerWindow"));
var _requirePropFactory = _interopRequireDefault(require("./requirePropFactory"));
var _setRef = _interopRequireDefault(require("./setRef"));
var _useEnhancedEffect = _interopRequireDefault(require("./useEnhancedEffect"));
var _useId = _interopRequireDefault(require("./useId"));
var _unsupportedProp = _interopRequireDefault(require("./unsupportedProp"));
var _useControlled = _interopRequireDefault(require("./useControlled"));
var _useEventCallback = _interopRequireDefault(require("./useEventCallback"));
var _useForkRef = _interopRequireDefault(require("./useForkRef"));
var _useIsFocusVisible = _interopRequireDefault(require("./useIsFocusVisible"));
var _getScrollbarSize = _interopRequireDefault(require("./getScrollbarSize"));
var _scrollLeft = require("./scrollLeft");
var _usePreviousProps = _interopRequireDefault(require("./usePreviousProps"));
var _visuallyHidden = _interopRequireDefault(require("./visuallyHidden"));
var _integerPropType = _interopRequireDefault(require("./integerPropType"));
var _resolveProps = _interopRequireDefault(require("./resolveProps"));
var _composeClasses = _interopRequireDefault(require("./composeClasses"));
var _generateUtilityClass = _interopRequireWildcard(require("./generateUtilityClass"));
Object.keys(_generateUtilityClass).forEach(function (key) {
  if (key === "default" || key === "__esModule") return;
  if (Object.prototype.hasOwnProperty.call(_exportNames, key)) return;
  if (key in exports && exports[key] === _generateUtilityClass[key]) return;
  Object.defineProperty(exports, key, {
    enumerable: true,
    get: function () {
      return _generateUtilityClass[key];
    }
  });
});
var _generateUtilityClasses = _interopRequireDefault(require("./generateUtilityClasses"));
var _ClassNameGenerator = _interopRequireDefault(require("./ClassNameGenerator"));
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }