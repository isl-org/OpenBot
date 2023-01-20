"use strict";

var _interopRequireDefault = require("@babel/runtime/helpers/interopRequireDefault");
Object.defineProperty(exports, "__esModule", {
  value: true
});
var _exportNames = {};
Object.defineProperty(exports, "default", {
  enumerable: true,
  get: function () {
    return _NoSsr.default;
  }
});
var _NoSsr = _interopRequireDefault(require("./NoSsr"));
var _NoSsr2 = require("./NoSsr.types");
Object.keys(_NoSsr2).forEach(function (key) {
  if (key === "default" || key === "__esModule") return;
  if (Object.prototype.hasOwnProperty.call(_exportNames, key)) return;
  if (key in exports && exports[key] === _NoSsr2[key]) return;
  Object.defineProperty(exports, key, {
    enumerable: true,
    get: function () {
      return _NoSsr2[key];
    }
  });
});