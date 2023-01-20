"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
Object.defineProperty(exports, "default", {
  enumerable: true,
  get: function () {
    return _styleFunctionSx.default;
  }
});
Object.defineProperty(exports, "extendSxProp", {
  enumerable: true,
  get: function () {
    return _extendSxProp.default;
  }
});
Object.defineProperty(exports, "unstable_createStyleFunctionSx", {
  enumerable: true,
  get: function () {
    return _styleFunctionSx.unstable_createStyleFunctionSx;
  }
});
Object.defineProperty(exports, "unstable_defaultSxConfig", {
  enumerable: true,
  get: function () {
    return _defaultSxConfig.default;
  }
});
var _styleFunctionSx = _interopRequireWildcard(require("./styleFunctionSx"));
var _extendSxProp = _interopRequireDefault(require("./extendSxProp"));
var _defaultSxConfig = _interopRequireDefault(require("./defaultSxConfig"));
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }