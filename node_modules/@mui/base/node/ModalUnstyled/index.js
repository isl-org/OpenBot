"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
var _exportNames = {
  ModalManager: true,
  modalUnstyledClasses: true,
  getModalUtilityClass: true
};
Object.defineProperty(exports, "ModalManager", {
  enumerable: true,
  get: function () {
    return _ModalManager.default;
  }
});
Object.defineProperty(exports, "default", {
  enumerable: true,
  get: function () {
    return _ModalUnstyled.default;
  }
});
Object.defineProperty(exports, "getModalUtilityClass", {
  enumerable: true,
  get: function () {
    return _modalUnstyledClasses.getModalUtilityClass;
  }
});
Object.defineProperty(exports, "modalUnstyledClasses", {
  enumerable: true,
  get: function () {
    return _modalUnstyledClasses.default;
  }
});
var _ModalUnstyled = _interopRequireDefault(require("./ModalUnstyled"));
var _ModalUnstyled2 = require("./ModalUnstyled.types");
Object.keys(_ModalUnstyled2).forEach(function (key) {
  if (key === "default" || key === "__esModule") return;
  if (Object.prototype.hasOwnProperty.call(_exportNames, key)) return;
  if (key in exports && exports[key] === _ModalUnstyled2[key]) return;
  Object.defineProperty(exports, key, {
    enumerable: true,
    get: function () {
      return _ModalUnstyled2[key];
    }
  });
});
var _ModalManager = _interopRequireWildcard(require("./ModalManager"));
Object.keys(_ModalManager).forEach(function (key) {
  if (key === "default" || key === "__esModule") return;
  if (Object.prototype.hasOwnProperty.call(_exportNames, key)) return;
  if (key in exports && exports[key] === _ModalManager[key]) return;
  Object.defineProperty(exports, key, {
    enumerable: true,
    get: function () {
      return _ModalManager[key];
    }
  });
});
var _modalUnstyledClasses = _interopRequireWildcard(require("./modalUnstyledClasses"));
function _getRequireWildcardCache(nodeInterop) { if (typeof WeakMap !== "function") return null; var cacheBabelInterop = new WeakMap(); var cacheNodeInterop = new WeakMap(); return (_getRequireWildcardCache = function (nodeInterop) { return nodeInterop ? cacheNodeInterop : cacheBabelInterop; })(nodeInterop); }
function _interopRequireWildcard(obj, nodeInterop) { if (!nodeInterop && obj && obj.__esModule) { return obj; } if (obj === null || typeof obj !== "object" && typeof obj !== "function") { return { default: obj }; } var cache = _getRequireWildcardCache(nodeInterop); if (cache && cache.has(obj)) { return cache.get(obj); } var newObj = {}; var hasPropertyDescriptor = Object.defineProperty && Object.getOwnPropertyDescriptor; for (var key in obj) { if (key !== "default" && Object.prototype.hasOwnProperty.call(obj, key)) { var desc = hasPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : null; if (desc && (desc.get || desc.set)) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } newObj.default = obj; if (cache) { cache.set(obj, newObj); } return newObj; }