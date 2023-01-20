"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
var _exportNames = {
  optionGroupUnstyledClasses: true
};
Object.defineProperty(exports, "default", {
  enumerable: true,
  get: function () {
    return _OptionGroupUnstyled.default;
  }
});
Object.defineProperty(exports, "optionGroupUnstyledClasses", {
  enumerable: true,
  get: function () {
    return _optionGroupUnstyledClasses.default;
  }
});
var _OptionGroupUnstyled = _interopRequireDefault(require("./OptionGroupUnstyled"));
var _OptionGroupUnstyled2 = require("./OptionGroupUnstyled.types");
Object.keys(_OptionGroupUnstyled2).forEach(function (key) {
  if (key === "default" || key === "__esModule") return;
  if (Object.prototype.hasOwnProperty.call(_exportNames, key)) return;
  if (key in exports && exports[key] === _OptionGroupUnstyled2[key]) return;
  Object.defineProperty(exports, key, {
    enumerable: true,
    get: function () {
      return _OptionGroupUnstyled2[key];
    }
  });
});
var _optionGroupUnstyledClasses = _interopRequireWildcard(require("./optionGroupUnstyledClasses"));
Object.keys(_optionGroupUnstyledClasses).forEach(function (key) {
  if (key === "default" || key === "__esModule") return;
  if (Object.prototype.hasOwnProperty.call(_exportNames, key)) return;
  if (key in exports && exports[key] === _optionGroupUnstyledClasses[key]) return;
  Object.defineProperty(exports, key, {
    enumerable: true,
    get: function () {
      return _optionGroupUnstyledClasses[key];
    }
  });
});
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }